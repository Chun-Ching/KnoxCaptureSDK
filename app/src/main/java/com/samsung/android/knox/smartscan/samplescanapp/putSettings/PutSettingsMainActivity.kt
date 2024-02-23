package com.samsung.android.knox.smartscan.samplescanapp.putSettings

import android.content.Intent
import android.os.Bundle
import android.util.Size
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.samsung.android.knox.smartscan.SDKConstants.CameraResolutions
import com.samsung.android.knox.smartscan.preview.BarcodeScanPreviewView
import com.samsung.android.knox.smartscan.preview.PreviewResultHandlingCallback
import com.samsung.android.knox.smartscan.preview.control.CameraFacingControl
import com.samsung.android.knox.smartscan.preview.control.FlashLightControl
import com.samsung.android.knox.smartscan.preview.control.FocusModelControl
import com.samsung.android.knox.smartscan.preview.data.BarcodeResult
import com.samsung.android.knox.smartscan.preview.data.EnvironmentData
import com.samsung.android.knox.smartscan.preview.data.PreviewConfig
import com.samsung.android.knox.smartscan.preview.feedback.BeepFeedback
import com.samsung.android.knox.smartscan.preview.feedback.VibrationFeedback
import com.samsung.android.knox.smartscan.preview.graphic.ViewFinderLaser
import com.samsung.android.knox.smartscan.preview.graphic.ViewFinderRectangle
import com.samsung.android.knox.smartscan.samplescanapp.CameraPermissionActivity
import com.samsung.android.knox.smartscan.samplescanapp.R

class PutSettingsMainActivity: CameraPermissionActivity() {

    private var previewView: BarcodeScanPreviewView? = null
    private var scanResultContainer: ViewGroup? = null
    private var scanResult: TextView? = null
    private var scanAgainButton: Button? = null

    private val resultHandlingCallback: PreviewResultHandlingCallback = object : PreviewResultHandlingCallback {

        override fun handleBarcodes(barcodeResult: BarcodeResult, environmentData: EnvironmentData) {
            barcodeResult.barcodes.firstOrNull()?.let { firstBarcode ->
                val stringBuilder = StringBuilder()
                stringBuilder.append("條碼: ").append(firstBarcode.data).append("\n")
                stringBuilder.append("長度: ").append(firstBarcode.data.length.toString()).append("\n")
                stringBuilder.append("條碼類型: ").append(firstBarcode.symbology)

                scanResultContainer?.visibility = View.VISIBLE
                scanResult?.text = stringBuilder.toString()
                scanAgainButton?.visibility = View.VISIBLE

                // pause a camera preview and stop barcode analyzing
                previewView?.pausePreview()
                previewView?.stopBarcodeAnalyzing()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.put_settings_main)
        title = "設定配置"
        ConfigurationManager.init(this)

        previewView = findViewById(R.id.preview_view)
        scanResultContainer = findViewById(R.id.scan_result)
        scanResult = findViewById(R.id.text_area)
        scanAgainButton = findViewById(R.id.scan_again_button)

        scanAgainButton?.setOnClickListener {
            scanAgainButton?.visibility = View.INVISIBLE
            scanResultContainer?.visibility = View.INVISIBLE

            previewView?.resumePreview()
            previewView?.startBarcodeAnalyzing()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (super.onOptionsItemSelected(item)) {
            return true
        }

        if (item.itemId == R.id.menu_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        return false
    }

    override fun onResume() {
        super.onResume()
        requestCameraPermission()
    }

    override fun onPause() {
        previewView?.let {
            it.stopBarcodeAnalyzing()
            it.finishPreview()
        }
        super.onPause()
    }

    override fun onDestroy() {
        ConfigurationManager.release(this)
        super.onDestroy()
    }

    override fun onCameraOpened() {
        previewView?.let { previewView ->
            val barcodeScanArea = when(ConfigurationManager.viewfinder){
                ConfigurationManager.ViewfinderType.RECTANGLE ->{
                    Size(ConfigurationManager.rectangularWidth, ConfigurationManager.rectangularHeight)
                }
                ConfigurationManager.ViewfinderType.LASER ->{
                    Size(ConfigurationManager.rectangularWidth, 10)
                }
            }
            val config : PreviewConfig = PreviewConfig.Builder()
                .setBarcodeScanArea(barcodeScanArea)
                .setCameraResolution(CameraResolutions.RESOLUTION_UHD)
                .setZoomRatio(ConfigurationManager.zoomRatio)
                .setSymbology(ConfigurationManager.symbologies)
                .setLogoPosition(ConfigurationManager.logoAnchor, ConfigurationManager.logoOffsetX, ConfigurationManager.logoOffsetY)
                .build()
            previewView.init(config, resultHandlingCallback)

            previewView.startPreview()
            previewView.startBarcodeAnalyzing()

            when (ConfigurationManager.viewfinder){
                ConfigurationManager.ViewfinderType.RECTANGLE ->{
                    previewView.setViewFinder(ViewFinderRectangle(barcodeScanArea))
                }
                ConfigurationManager.ViewfinderType.LASER ->{
                    previewView.setViewFinder(ViewFinderLaser(ConfigurationManager.rectangularWidth))
                }
            }

            val focusControl = FocusModelControl()
            focusControl.setMode(ConfigurationManager.focusMode)
            previewView.addControl(focusControl)

            // give enough offset not to overlap with actionbar.
            val cameraFacingControl = CameraFacingControl()
            cameraFacingControl.createButton()
                .setAnchor(ConfigurationManager.cameraFacingAnchor)
                .setMargin(ConfigurationManager.cameraFacingMarginX, ConfigurationManager.cameraFacingMarginY)
            previewView.addControl(cameraFacingControl)

            val flashLightControl = FlashLightControl()
            flashLightControl.createButton()
                .setAnchor(ConfigurationManager.flashlightAnchor)
                .setMargin(ConfigurationManager.flashlightMarginX, ConfigurationManager.flashlightMarginY)
            previewView.addControl(flashLightControl)

            if (ConfigurationManager.beep) {
                previewView.addScanResultFeedback(BeepFeedback())
            }

            if (ConfigurationManager.vibrator) {
                previewView.addScanResultFeedback(VibrationFeedback())
            }
        }
    }
}