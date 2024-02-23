package com.samsung.android.knox.smartscan.samplescanapp.putSettings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.samsung.android.knox.smartscan.SDKConstants.ControlAnchor
import com.samsung.android.knox.smartscan.SDKConstants.FocusMode
import com.samsung.android.knox.smartscan.SDKConstants.LogoAnchor
import com.samsung.android.knox.smartscan.barcode.data.BarcodeInfo

object ConfigurationManager : SharedPreferences.OnSharedPreferenceChangeListener {
    const val KEY_SYMBOLOGIES = "symbologies"
    const val KEY_ZOOM_FACTOR = "zoom_factor"
    const val KEY_FOCUS_MODE = "focus_mode"
    const val KEY_BEEP = "beep"
    const val KEY_VIBRATOR = "vibrator"
    const val KEY_VIEWFINDER = "viewfinder"
    const val KEY_RECTANGULAR_WIDTH = "rectangular_width"
    const val KEY_RECTANGULAR_HEIGHT = "rectangular_height"

    const val KEY_CAMERA_FACING_ANCHOR = "camera_facing_anchor"
    const val KEY_CAMERA_FACING_MARGIN_X = "camera_facing_margin_x"
    const val KEY_CAMERA_FACING_MARGIN_Y = "camera_facing_margin_y"
    const val KEY_FLASHLIGHT_ANCHOR = "flashlight_anchor"
    const val KEY_FLASHLIGHT_MARGIN_X = "flashlight_margin_x"
    const val KEY_FLASHLIGHT_MARGIN_Y = "flashlight_margin_y"
    const val KEY_LOGO_ANCHOR = "logo_anchor"
    const val KEY_LOGO_OFFSET_X = "logo_offset_x"
    const val KEY_LOGO_OFFSET_Y = "logo_offset_y"

    private val defaultSymbologies: List<String> = BarcodeInfo.allSupportedBarcodeSymbology

    var symbologies: List<String> = emptyList()
    var zoomRatio = 1.0f
    var focusMode: FocusMode = FocusMode.OFF
    var beep: Boolean = true
    var vibrator: Boolean = true
    var viewfinder = ViewfinderType.RECTANGLE
    var rectangularWidth = 90
    var rectangularHeight = 40

    var cameraFacingAnchor: ControlAnchor = ControlAnchor.TOP_RIGHT
    var cameraFacingMarginX = 0
    var cameraFacingMarginY = 0
    var flashlightAnchor: ControlAnchor = ControlAnchor.TOP_RIGHT
    var flashlightMarginX = 0
    var flashlightMarginY = 0
    var logoAnchor: LogoAnchor = LogoAnchor.BOTTOM_CENTER
    var logoOffsetX = 0
    var logoOffsetY = 0


    enum class ViewfinderType (val value: String){
        RECTANGLE("RECTANGLE"),LASER("LASER");

        companion object {
            fun find(value: String?): ViewfinderType? {
                return values().find { it.value == value }
            }
        }
    }

    fun init(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.registerOnSharedPreferenceChangeListener(this)

        loadValue(preferences, KEY_SYMBOLOGIES)
        loadValue(preferences, KEY_ZOOM_FACTOR)
        loadValue(preferences, KEY_FOCUS_MODE)
        loadValue(preferences, KEY_BEEP)
        loadValue(preferences, KEY_VIBRATOR)
        loadValue(preferences, KEY_VIEWFINDER)
        loadValue(preferences, KEY_RECTANGULAR_WIDTH)
        loadValue(preferences, KEY_RECTANGULAR_HEIGHT)
        loadValue(preferences, KEY_CAMERA_FACING_ANCHOR)
        loadValue(preferences, KEY_CAMERA_FACING_MARGIN_X)
        loadValue(preferences, KEY_CAMERA_FACING_MARGIN_Y)
        loadValue(preferences, KEY_FLASHLIGHT_ANCHOR)
        loadValue(preferences, KEY_FLASHLIGHT_MARGIN_X)
        loadValue(preferences, KEY_FLASHLIGHT_MARGIN_Y)
        loadValue(preferences, KEY_LOGO_ANCHOR)
        loadValue(preferences, KEY_LOGO_OFFSET_X)
        loadValue(preferences, KEY_LOGO_OFFSET_Y)
    }

    fun release(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun loadValue(preferences: SharedPreferences, key: String) {
        when (key) {
            KEY_SYMBOLOGIES -> {
                symbologies = preferences.getStringSet(KEY_SYMBOLOGIES, null)?.toList() ?: defaultSymbologies
            }

            KEY_ZOOM_FACTOR -> {
                zoomRatio = preferences.getString(KEY_ZOOM_FACTOR, null)?.toFloat() ?: 1.0f
            }

            KEY_FOCUS_MODE -> {
                focusMode = FocusMode.find(preferences.getString(KEY_FOCUS_MODE, null)) ?: FocusMode.OFF
            }

            KEY_BEEP -> {
                beep = preferences.getBoolean(KEY_BEEP, beep)
            }

            KEY_VIBRATOR -> {
                vibrator = preferences.getBoolean(KEY_VIBRATOR, vibrator)
            }

            KEY_VIEWFINDER ->{
                viewfinder = ViewfinderType.find(preferences.getString(KEY_VIEWFINDER, null)) ?: ViewfinderType.RECTANGLE
            }

            KEY_RECTANGULAR_WIDTH -> {
                rectangularWidth = preferences.getString(KEY_RECTANGULAR_WIDTH, null)?.toInt() ?: 90
            }

            KEY_RECTANGULAR_HEIGHT -> {
                rectangularHeight = preferences.getString(KEY_RECTANGULAR_HEIGHT, null)?.toInt() ?: 40
            }

            KEY_CAMERA_FACING_ANCHOR -> {
                cameraFacingAnchor = ControlAnchor.find(preferences.getString(KEY_CAMERA_FACING_ANCHOR, null)) ?: ControlAnchor.TOP_RIGHT
            }

            KEY_CAMERA_FACING_MARGIN_X -> {
                cameraFacingMarginX = preferences.getString(KEY_CAMERA_FACING_MARGIN_X,null)?.toInt() ?: 0
            }

            KEY_CAMERA_FACING_MARGIN_Y -> {
                cameraFacingMarginY = preferences.getString(KEY_CAMERA_FACING_MARGIN_Y,null)?.toInt() ?: cameraFacingMarginY
            }

            KEY_FLASHLIGHT_ANCHOR -> {
                flashlightAnchor = ControlAnchor.find(preferences.getString(KEY_FLASHLIGHT_ANCHOR, null)) ?: ControlAnchor.TOP_RIGHT
            }

            KEY_FLASHLIGHT_MARGIN_X -> {
                flashlightMarginX = preferences.getString(KEY_FLASHLIGHT_MARGIN_X,null)?.toInt() ?: 0
            }

            KEY_FLASHLIGHT_MARGIN_Y -> {
                flashlightMarginY = preferences.getString(KEY_FLASHLIGHT_MARGIN_Y,null)?.toInt() ?: flashlightMarginY
            }

            KEY_LOGO_ANCHOR -> {
                logoAnchor = LogoAnchor.find(preferences.getString(KEY_LOGO_ANCHOR, null)) ?: LogoAnchor.BOTTOM_CENTER
            }

            KEY_LOGO_OFFSET_X -> {
                logoOffsetX = preferences.getString(KEY_LOGO_OFFSET_X,null)?.toInt() ?: 0
            }

            KEY_LOGO_OFFSET_Y -> {
                logoOffsetY = preferences.getString(KEY_LOGO_OFFSET_Y,null)?.toInt() ?: 0
            }
        }
    }

    override fun onSharedPreferenceChanged(preferences: SharedPreferences, key: String) {
       loadValue(preferences, key)
    }

    /**
     * This is the zoom factor entries.
     */
    val zoomFactorEntries: List<String> = listOf(
        "1.0",
        "1.1",
        "1.2",
        "1.3",
        "1.4",
        "1.5",
        "1.6",
        "1.7",
        "1.8",
        "1.9",
        "2.0",
        "2.1",
        "2.2",
        "2.3",
        "2.4",
        "2.5",
        "2.6",
        "2.7",
        "2.8",
        "2.9",
        "3.0",
        "3.1",
        "3.2",
        "3.3",
        "3.4",
        "3.5",
        "3.6",
        "3.7",
        "3.8",
        "3.9",
        "4.0"
    )

}