package com.fahmi.authui

object AuthUi {
    var baseURL = "https://yourapi.com/"
    var enableLog = false

    fun initialize(baseURL: String, enableLog: Boolean) {
        this.baseURL = baseURL
        this.enableLog = enableLog
    }
}