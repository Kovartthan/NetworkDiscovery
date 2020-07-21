package com.kovartthan.myapplication.ui

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.PROTOCOL_DNS_SD
import android.net.nsd.NsdServiceInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kovartthan.myapplication.model.ScanModel
import com.kovartthan.myapplication.utils.Constants
import java.lang.Exception


class ScanViewModel() : ViewModel() {

     var nsdManager: NsdManager? = null
    var scanModelList: MutableLiveData<ScanModel>? = null
    var scanPostListener: ScanPostListener? = null
    var isDiscoverableTurnedOn: Boolean = false
    var isPublishRunned: Boolean = false

    init {
        scanModelList = MutableLiveData()
    }



    fun onScan(view: Context) {
            try {
                isDiscoverableTurnedOn = true
                if (nsdManager == null)
                    nsdManager = view.getSystemService(Context.NSD_SERVICE) as NsdManager?
                nsdManager?.discoverServices(
                    Constants.type,
                    PROTOCOL_DNS_SD, object : NsdManager.DiscoveryListener {

                        override fun onDiscoveryStarted(regType: String) {
                            scanPostListener?.onShow("Discovery Started!")
                        }

                        override fun onServiceFound(service: NsdServiceInfo) {
                            nsdManager?.resolveService(
                                service,
                                object : NsdManager.ResolveListener {
                                    override fun onResolveFailed(
                                        serviceInfo: NsdServiceInfo?,
                                        errorCode: Int
                                    ) {
//                    scanPostListener?.onShow("Resolve Failed !")
                                        isDiscoverableTurnedOn = false
                                    }

                                    override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
                                        val data = ScanModel(
                                            serviceInfo?.serviceType,
                                            serviceInfo!!.serviceName,
                                            serviceInfo.port,
                                            serviceInfo.host
                                        )
                                        isDiscoverableTurnedOn = false
                                        scanModelList?.postValue(data)
                                    }

                                })
                        }

                        override fun onServiceLost(service: NsdServiceInfo) {
//            scanPostListener?.onShow("Discovery Start Failed!")
                            isDiscoverableTurnedOn = false
                        }

                        override fun onDiscoveryStopped(serviceType: String) {
//            scanPostListener?.onShow("Discovery Stop Failed!")
                            isDiscoverableTurnedOn = false
                        }

                        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {

                            nsdManager?.stopServiceDiscovery(this)
                            isDiscoverableTurnedOn = false
                        }

                        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {

                            nsdManager?.stopServiceDiscovery(this)
                            isDiscoverableTurnedOn = false
                        }
                    }
                )
            }catch (e : Exception){
                e.printStackTrace()
            }
    }

    fun onPublish(view: Context) {
        try {
            val serviceInfo = NsdServiceInfo()
            serviceInfo.serviceName = Constants.name
            serviceInfo.serviceType = Constants.type
            serviceInfo.port = Constants.port
            if (nsdManager == null)
                nsdManager = view.getSystemService(Context.NSD_SERVICE) as NsdManager?
            nsdManager?.registerService(
                serviceInfo, PROTOCOL_DNS_SD, object : NsdManager.RegistrationListener {
                    override fun onUnregistrationFailed(
                        serviceInfo: NsdServiceInfo?,
                        errorCode: Int
                    ) {
                        scanPostListener?.onShow("UnRegistration Failed !")
                        isPublishRunned = false
                    }

                    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
                        scanPostListener?.onShow("Service Unregistered !")
                        isPublishRunned = false
                    }

                    override fun onRegistrationFailed(
                        serviceInfo: NsdServiceInfo?,
                        errorCode: Int
                    ) {
                        scanPostListener?.onShow("Registration Failed !")
                        isPublishRunned = false
                    }

                    override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
                        scanPostListener?.onShow("Registration Success !")
                        isPublishRunned = true
                    }

                }
            )

        }catch (e : Exception){

        }

    }

}