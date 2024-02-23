package com.samsung.android.knox.smartscan.samplescanapp.fullScreen

import android.os.Bundle
import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.samsung.android.knox.smartscan.SDKConstants.CameraResolutions
import com.samsung.android.knox.smartscan.preview.BarcodeScanPreviewView
import com.samsung.android.knox.smartscan.preview.PreviewResultHandlingCallback
import com.samsung.android.knox.smartscan.preview.control.CameraFacingControl
import com.samsung.android.knox.smartscan.preview.control.FlashLightControl
import com.samsung.android.knox.smartscan.preview.data.BarcodeResult
import com.samsung.android.knox.smartscan.preview.data.EnvironmentData
import com.samsung.android.knox.smartscan.preview.data.PreviewConfig
import com.samsung.android.knox.smartscan.preview.graphic.ViewFinderRectangle
import com.samsung.android.knox.smartscan.samplescanapp.CameraPermissionActivity
import com.samsung.android.knox.smartscan.samplescanapp.R

class FullScreenActivity : CameraPermissionActivity() {

    private var previewView: BarcodeScanPreviewView? = null
    private var scanResultContainer: ViewGroup? = null
    private var scanResult: TextView? = null
    private var scanAgainButton: Button? = null

    private val resultHandlingCallback = object : PreviewResultHandlingCallback {
        override fun handleBarcodes(barcodeResult: BarcodeResult, environmentData: EnvironmentData) {
            if (barcodeResult.barcodes.isNotEmpty()) {
                val firstBarcode = barcodeResult.barcodes[0]

                scanAgainButton?.visibility = View.VISIBLE

                val stringBuilder = StringBuilder()
                stringBuilder.append("條碼: ").append(firstBarcode.data)
                    .append("\n")
                stringBuilder.append("長度: ")
                    .append(firstBarcode.data.length.toString()).append("\n")
                stringBuilder.append("條碼類型: ").append(firstBarcode.symbology)

                scanResultContainer?.visibility = View.VISIBLE
                scanResult?.text = stringBuilder.toString()

                // pause a camera preview and stop barcode analyzing
                previewView?.pausePreview()
                previewView?.stopBarcodeAnalyzing()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_screen_layout)
        title = "單個掃描"

        previewView = findViewById(R.id.preview_view)
        scanResultContainer = findViewById(R.id.scan_result)
        scanResult = findViewById(R.id.text_area)
        scanAgainButton = findViewById(R.id.scan_again_button)

        scanAgainButton?.setOnClickListener {
            scanAgainButton?.visibility = View.INVISIBLE
            scanResultContainer?.visibility = View.INVISIBLE

            //start to stream a camera preview
            previewView?.resumePreview()
            previewView?.startBarcodeAnalyzing()
        }
    }

    override fun onCameraOpened() {
        openCamera()
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

    private fun openCamera() {
        previewView?.let { pv ->
            val scanArea = Size(90, 40)

            //set camera preview configuration
            val config: PreviewConfig = PreviewConfig.Builder()
                .setCameraResolution(CameraResolutions.RESOLUTION_UHD)
                .setBarcodeScanArea(scanArea)
                .build()
            pv.init(config, resultHandlingCallback)

            pv.startPreview()
            pv.startBarcodeAnalyzing()

            // give enough offset not to overlap with actionbar.
            val cameraFacingControl = CameraFacingControl()
            cameraFacingControl.createButton()
            pv.addControl(cameraFacingControl)

            val flashLightControl = FlashLightControl()
            flashLightControl.createButton()
            pv.addControl(flashLightControl)

            // Show viewfinder the same size as scan area.
            pv.setViewFinder(ViewFinderRectangle(scanArea))
        }
    }
}