package com.kovartthan.myapplication.model

import java.net.InetAddress

data class ScanModel(
     val type: String?,
     val name: String?,
     val port : Int,
     val address : InetAddress?
)