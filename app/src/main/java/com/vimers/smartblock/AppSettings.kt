package com.vimers.smartblock

class AppSettings {
    var mail: String? = null
    var password: String? = null

    companion object {
        const val PERSISTENT_OBJECT_NAME = "AppSettings"
    }
}