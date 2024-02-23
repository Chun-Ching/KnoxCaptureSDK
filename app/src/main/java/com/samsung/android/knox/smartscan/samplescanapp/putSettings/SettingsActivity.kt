package com.samsung.android.knox.smartscan.samplescanapp.putSettings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.samsung.android.knox.smartscan.samplescanapp.R

class SettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_frame_layout)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.menu_settings)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SettingsFragment.newInstance())
                .commitNow()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            onBackPressed()
        }
        return true
    }
}