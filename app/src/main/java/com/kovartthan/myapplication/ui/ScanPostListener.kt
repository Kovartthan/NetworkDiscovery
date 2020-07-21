package com.kovartthan.myapplication.ui

interface ScanPostListener{
    fun showProgress(isScanning : Boolean)
    fun onShow(message: String)
}