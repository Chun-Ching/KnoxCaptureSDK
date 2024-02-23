package com.samsung.android.knox.smartscan.samplescanapp.putSettings

import android.os.Bundle
import android.text.InputType
import androidx.preference.*
import com.samsung.android.knox.smartscan.SDKConstants.ControlAnchor
import com.samsung.android.knox.smartscan.SDKConstants.FocusMode
import com.samsung.android.knox.smartscan.SDKConstants.LogoAnchor
import com.samsung.android.knox.smartscan.barcode.data.BarcodeInfo
import com.samsung.android.knox.smartscan.samplescanapp.R

class SettingsFragment: PreferenceFragmentCompat() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)

        // symbologies
        val symbologies = findPreference<MultiSelectListPreference>(ConfigurationManager.KEY_SYMBOLOGIES)
        symbologies?.values = ConfigurationManager.symbologies.toSet()
        symbologies?.entries = BarcodeInfo.allSupportedBarcodeSymbology.toTypedArray()
        symbologies?.entryValues = BarcodeInfo.allSupportedBarcodeSymbology.toTypedArray()
        symbologies?.summaryProvider = Preference.SummaryProvider<MultiSelectListPreference> {
            it.values.joinToString(", ")
        }

        // zoom factor
        findPreference<ListPreference>(ConfigurationManager.KEY_ZOOM_FACTOR)?.let {
            it.entries = ConfigurationManager.zoomFactorEntries.toTypedArray()
            it.entryValues = it.entries
            it.value = ConfigurationManager.zoomRatio.toString()
        }

        // focus mode
        findPreference<DropDownPreference>(ConfigurationManager.KEY_FOCUS_MODE)?.let {
            it.entries = arrayOf("Auto", "Focus on center", "Focus on touch coordinate")
            it.entryValues = arrayOf(FocusMode.OFF.value, FocusMode.CENTER.value, FocusMode.TOUCH_COORDINATE.value)
        }

        // beep after scan
        findPreference<SwitchPreference>(ConfigurationManager.KEY_BEEP)?.isChecked = ConfigurationManager.beep

        // vibrate after scan
        findPreference<SwitchPreference>(ConfigurationManager.KEY_VIBRATOR)?.isChecked = ConfigurationManager.vibrator

        // viewfinder
        findPreference<ListPreference>(ConfigurationManager.KEY_VIEWFINDER)?.let {
            it.entries = arrayOf("Rectangular", "Laser")
            it.entryValues = arrayOf(ConfigurationManager.ViewfinderType.RECTANGLE.value,
                ConfigurationManager.ViewfinderType.LASER.value)
            it.value = ConfigurationManager.viewfinder.value
        }
        findPreference<EditTextPreference>(ConfigurationManager.KEY_RECTANGULAR_WIDTH)?.setOnBindEditTextListener {
            // show number keypad
            it.inputType = InputType.TYPE_CLASS_NUMBER
        }

        findPreference<EditTextPreference>(ConfigurationManager.KEY_RECTANGULAR_HEIGHT)?.setOnBindEditTextListener {
            // show number keypad
            it.inputType = InputType.TYPE_CLASS_NUMBER
        }

        // camera facing
        findPreference<DropDownPreference>(ConfigurationManager.KEY_CAMERA_FACING_ANCHOR)?.let {
            it.entries = arrayOf("Top Left", "Top Right")
            it.entryValues = arrayOf(ControlAnchor.TOP_LEFT.value, ControlAnchor.TOP_RIGHT.value)
            it.value = ConfigurationManager.cameraFacingAnchor.value
        }
        findPreference<EditTextPreference>(ConfigurationManager.KEY_CAMERA_FACING_MARGIN_X)?.let { pref ->
            pref.setOnBindEditTextListener {
                it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            pref.text = "${ConfigurationManager.cameraFacingMarginX}"
        }
        findPreference<EditTextPreference>(ConfigurationManager.KEY_CAMERA_FACING_MARGIN_Y)?.let { pref ->
            pref.setOnBindEditTextListener {
                it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            pref.text = "${ConfigurationManager.cameraFacingMarginY}"
        }

        // flashlight
        findPreference<DropDownPreference>(ConfigurationManager.KEY_FLASHLIGHT_ANCHOR)?.let {
            it.entries = arrayOf("Top Left", "Top Right")
            it.entryValues = arrayOf(ControlAnchor.TOP_LEFT.value, ControlAnchor.TOP_RIGHT.value)
            it.value = ConfigurationManager.flashlightAnchor.value
        }
        findPreference<EditTextPreference>(ConfigurationManager.KEY_FLASHLIGHT_MARGIN_X)?.let { pref ->
            pref.setOnBindEditTextListener {
                it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            pref.text = "${ConfigurationManager.flashlightMarginX}"
        }
        findPreference<EditTextPreference>(ConfigurationManager.KEY_FLASHLIGHT_MARGIN_Y)?.let { pref ->
            pref.setOnBindEditTextListener {
                it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            pref.text = "${ConfigurationManager.flashlightMarginY}"
        }

        // logo
        findPreference<DropDownPreference>(ConfigurationManager.KEY_LOGO_ANCHOR)?.let {
            it.entries = arrayOf("Bottom Center", "Bottom Left",  "Bottom Right","Top Center", "Top Left",  "Top Right")
            it.entryValues = arrayOf(LogoAnchor.BOTTOM_CENTER.value,
                LogoAnchor.BOTTOM_LEFT.value, LogoAnchor.BOTTOM_RIGHT.value,
                LogoAnchor.TOP_CENTER.value,
                LogoAnchor.TOP_LEFT.value, LogoAnchor.TOP_RIGHT.value)
            it.value = ConfigurationManager.logoAnchor.value
        }

        findPreference<EditTextPreference>(ConfigurationManager.KEY_LOGO_OFFSET_X)?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        }
        findPreference<EditTextPreference>(ConfigurationManager.KEY_LOGO_OFFSET_Y)?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        }
    }
}