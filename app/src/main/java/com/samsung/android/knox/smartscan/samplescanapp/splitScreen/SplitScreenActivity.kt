package com.samsung.android.knox.smartscan.samplescanapp.splitScreen

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.samsung.android.knox.smartscan.SDKConstants
import com.samsung.android.knox.smartscan.preview.BarcodeScanPreviewView
import com.samsung.android.knox.smartscan.preview.PreviewResultHandlingCallback
import com.samsung.android.knox.smartscan.preview.control.CameraFacingControl
import com.samsung.android.knox.smartscan.preview.control.FlashLightControl
import com.samsung.android.knox.smartscan.preview.data.BarcodeResult
import com.samsung.android.knox.smartscan.preview.data.EnvironmentData
import com.samsung.android.knox.smartscan.preview.data.PreviewConfig
import com.samsung.android.knox.smartscan.samplescanapp.CameraPermissionActivity
import com.samsung.android.knox.smartscan.samplescanapp.Constants
import com.samsung.android.knox.smartscan.samplescanapp.MainActivity
import com.samsung.android.knox.smartscan.samplescanapp.R

@Suppress("DEPRECATION")
class SplitScreenActivity : CameraPermissionActivity() {

    private var previewView: BarcodeScanPreviewView? = null
    private var scanResult: TextView? = null
    private var scanAgainButton: Button? = null
    private var product1: TextView? = null
    private var product2: TextView? = null
    private var product3: TextView? = null
    private var product4: TextView? = null
    private var product5: TextView? = null
    private var product6: TextView? = null

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    // 在 FullScreenActivity 中的成員變數中添加一個集合
    private val scannedBarcodesSet = mutableSetOf<String>()
    private val filledProductsSet = mutableSetOf<Int>()
    private val resultHandlingCallback = object : PreviewResultHandlingCallback {
        override fun handleBarcodes(
            barcodeResult: BarcodeResult,
            environmentData: EnvironmentData
        ) {
            if (barcodeResult.barcodes.isNotEmpty()) {
                val firstBarcode = barcodeResult.barcodes[0]
                val barcodeData = firstBarcode.data

                scanAgainButton?.visibility = View.VISIBLE

                // 檢查條碼是否已經處理過
                if (!scannedBarcodesSet.contains(barcodeData)) {
                    scannedBarcodesSet.add(barcodeData) // 將條碼添加到集合中
                    scanResult?.append("${firstBarcode.data}\n")

                    // 根據條碼的第一個數字進行分類
                    when (barcodeData.firstOrNull()) {
                        '1' -> updateProductField(1, barcodeData)
                        '2' -> updateProductField(2, barcodeData)
                        '3' -> updateProductField(3, barcodeData)
                        '4' -> updateProductField(4, barcodeData)
                        '5' -> updateProductField(5, barcodeData)
                        '6' -> updateProductField(6, barcodeData)

                        // 可以繼續添加更多的條件
                        else -> {
                            // 如果是其他條碼，你可以在這裡添加相應的處理邏輯
                            showAlertDialog("警告", "未包含在商品類別")
                        }
                    }

                    // pause a camera preview and stop barcode analyzing
                    //previewView?.pausePreview()
                    previewView?.stopBarcodeAnalyzing()
                    playSystemNotificationSound()
                    vibrateDevice()
                }


            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.split_screen_layout)
        title = "單個條碼盤點系統"
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        previewView = findViewById(R.id.preview_view)
        product1 = findViewById(R.id.product1)
        product2 = findViewById(R.id.product2)
        product3 = findViewById(R.id.product3)
        product4 = findViewById(R.id.product4)
        product5 = findViewById(R.id.product5)
        product6 = findViewById(R.id.product6)
        scanResult?.movementMethod = ScrollingMovementMethod()
        scanAgainButton = findViewById(R.id.scan_again_button)
        scanAgainButton?.visibility = View.VISIBLE
        scanAgainButton?.setOnClickListener {
            scanAgainButton?.visibility = View.VISIBLE
            //scanResult?.text =""

            scanAgainButton?.setOnClickListener {
                //start to stream a camera preview
                previewView?.resumePreview()
                previewView?.startBarcodeAnalyzing()
            }
        }
    }

    private fun updateProductField(productNumber: Int, barcodeData: String) {
        // 根據商品編號更新相應的欄位
        when (productNumber) {
            1 -> {
                updateProductField1(barcodeData)
                filledProductsSet.add(1)
                // 顯示"已加入系統"的訊息
                showAddedToSystemMessage(productNumber)
            }

            2 -> {
                updateProductField2(barcodeData)
                filledProductsSet.add(2)
                // 顯示"已加入系統"的訊息
                showAddedToSystemMessage(productNumber)
            }

            3 -> {
                updateProductField3(barcodeData)
                filledProductsSet.add(3)
                // 顯示"已加入系統"的訊息
                showAddedToSystemMessage(productNumber)
            }

            4 -> {
                updateProductField4(barcodeData)
                filledProductsSet.add(4)
                // 顯示"已加入系統"的訊息
                showAddedToSystemMessage(productNumber)
            }

            5 -> {
                updateProductField5(barcodeData)
                filledProductsSet.add(5)
                // 顯示"已加入系統"的訊息
                showAddedToSystemMessage(productNumber)
            }

            6 -> {
                updateProductField6(barcodeData)
                filledProductsSet.add(6)
                // 顯示"已加入系統"的訊息
                showAddedToSystemMessage(productNumber)
            }
        }
    }

    private var isDialogShowing = false
    private fun showAddedToSystemMessage(productNumber: Int) {
        isDialogShowing = true
        AlertDialog.Builder(this@SplitScreenActivity)
            .setTitle("")
            .setMessage("商品$productNumber 已加入系統")
            .setPositiveButton("確定") { dialog, _ ->
                dialog.dismiss()
                isDialogShowing = false
                checkNextAction()
            }
            .show()
    }

    private fun checkNextAction() {
        if (!isDialogShowing && filledProductsSet.size == 6 && scannedBarcodesSet.size == 6) {
            showInventoryCompleteDialog() // 如果沒有對話框正在顯示，且六個商品都已掃描，則顯示盤點完成對話框
        }
    }


    private fun showInventoryCompleteDialog() {
        AlertDialog.Builder(this@SplitScreenActivity)
            .setTitle("")
            .setMessage("商品已全數盤點完成")
            .setPositiveButton("確定") { dialog, _ ->
                dialog.dismiss()
                //resetInventory() // 重新開始盤點
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("showNewButton", true)
                startActivity(intent)
                finish()  // 結束當前的 Activity，避免在返回時回到之前的狀態
            }
            .show()

    }


    private fun resetInventory() {
        // 開始新的盤點
        startNewInventory()
    }

    private fun startNewInventory() {
        // 重置掃描記錄和已填入的商品集合
        scannedBarcodesSet.clear()
        filledProductsSet.clear()

        // 開始新的盤點
        val intent = Intent(this, SplitScreenActivity::class.java)
        startActivity(intent)
        finish()  // 結束當前的 Activity，避免在返回時回到之前的狀態
    }


    private fun updateProductField1(barcodeData: String) {
        // 更新商品1的欄位
        product1?.text = barcodeData
        product1?.setTextColor(Color.RED)
    }

    private fun updateProductField2(barcodeData: String) {
        // 更新商品2的欄位
        product2?.text = barcodeData
        product2?.setTextColor(Color.RED)
    }

    private fun updateProductField3(barcodeData: String) {
        // 更新商品3的欄位
        product3?.text = barcodeData
        product3?.setTextColor(Color.RED)
    }

    private fun updateProductField4(barcodeData: String) {
        // 更新商品4的欄位
        product4?.text = barcodeData
        product4?.setTextColor(Color.RED)
    }

    private fun updateProductField5(barcodeData: String) {
        // 更新商品5的欄位
        product5?.text = barcodeData
        product5?.setTextColor(Color.RED)
    }

    private fun updateProductField6(barcodeData: String) {
        // 更新商品6的欄位
        product6?.text = barcodeData
        product6?.setTextColor(Color.RED)
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this@SplitScreenActivity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("確定") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private var isNotificationSoundPlaying = false

    private fun playSystemNotificationSound() {
        Log.d(Constants.TAG, "Playing system notification sound")
        if (!isNotificationSoundPlaying) {
            mediaPlayer =
                MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer?.release()
                mediaPlayer = null
                isNotificationSoundPlaying = false
            }
            mediaPlayer?.start()
            isNotificationSoundPlaying = true
        }
    }

    private fun vibrateDevice() {
        Log.d(Constants.TAG, "Vibrating device")
        vibrator?.let {
            if (it.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    it.vibrate(
                        VibrationEffect.createOneShot(
                            500,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    it.vibrate(100)
                }
            }
        }
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

    override fun onCameraOpened() {
        openCamera()
    }

    private fun openCamera() {
        previewView?.let { pv ->
            //set camera preview configuration
            //set full BarcodeScanArea(100%)
            val config: PreviewConfig = PreviewConfig.Builder()
                .setBarcodeScanArea(Size(100, 100))
                .setCameraResolution(SDKConstants.CameraResolutions.RESOLUTION_UHD)
                .build()

            pv.init(config, resultHandlingCallback)

            val cameraFacingControl = CameraFacingControl()
            cameraFacingControl.createButton()
            pv.addControl(cameraFacingControl)

            val flashLightControl = FlashLightControl()
            flashLightControl.createButton()
            pv.addControl(flashLightControl)

            pv.startPreview()
            scanAgainButton?.setOnClickListener {
                pv.startBarcodeAnalyzing()
            }
        }
    }
}