package com.samsung.android.knox.smartscan.samplescanapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.samsung.android.knox.smartscan.SDKVersion
import com.samsung.android.knox.smartscan.license.LicenseActivationCallback
import com.samsung.android.knox.smartscan.license.ScanLicenseClient
import com.samsung.android.knox.smartscan.samplescanapp.multiScan.MultiScanActivity
import com.samsung.android.knox.smartscan.samplescanapp.splitScreen.SplitScreenActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.full_screen).setOnClickListener {

            // 顯示載入中的 Toast
            val loadingToast = Toast.makeText(this, "正在檢查授權...", Toast.LENGTH_LONG)
            loadingToast.show()

            /* 啟用license */
            ScanLicenseClient.getScanLicense()
                .activateLicense(this, Constants.KPE_LICENSE_KEY, object : LicenseActivationCallback {
                    override fun onFail(errorCode: Int) {
                        Log.d(TAG, "failed. ErrorCode : $errorCode")
                        // 關閉載入中的 Toast
                        loadingToast.cancel()

                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "無法啟用", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onSuccess() {
                        Log.d(TAG, "succeeded.")
                        // 關閉載入中的 Toast
                        loadingToast.cancel()

                        runOnUiThread {
                            // 檢查授權成功後啟動新的Activity
                            Toast.makeText(this@MainActivity, "授權成功", Toast.LENGTH_SHORT).show()
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    SplitScreenActivity::class.java
                                )
                            )
                        }
                    }
                })
        }

        val showNewButton = intent.getBooleanExtra("showNewButton", false)
        val newButton = findViewById<Button>(R.id.new_inventory_button)


        if (showNewButton) {
            newButton.visibility = View.VISIBLE
        } else {
            newButton.visibility = View.GONE
        }


        findViewById<Button>(R.id.multi_scan).setOnClickListener {

            // 顯示載入中的 Toast
            val loadingToast = Toast.makeText(this, "正在檢查授權...", Toast.LENGTH_LONG)
            loadingToast.show()

            ScanLicenseClient.getScanLicense()
                .activateLicense(this, Constants.KPE_LICENSE_KEY, object : LicenseActivationCallback {
                    override fun onFail(errorCode: Int) {
                        Log.d(TAG, "failed. ErrorCode : $errorCode")
                        // 關閉載入中的 Toast
                        loadingToast.cancel()

                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "無法啟用", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onSuccess() {
                        Log.d(TAG, "succeeded.")
                        // 關閉載入中的 Toast
                        loadingToast.cancel()

                        runOnUiThread {
                            // 檢查授權成功後啟動新的Activity
                            Toast.makeText(this@MainActivity, "授權成功", Toast.LENGTH_SHORT).show()
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    MultiScanActivity::class.java
                                )
                            )
                        }
                    }
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (super.onOptionsItemSelected(item)) {
            return true
        }

        if (item.itemId == R.id.menu_about) {
            showAboutDialog()
            return true
        }

        return false
    }


    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("關於")
            .setMessage("KCAP SDK Version: ${SDKVersion.getVersionString()}")
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}