package com.praveen.multicast_dns.listeners

import android.annotation.SuppressLint
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.praveen.multicast_dns.model.ScannedData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class ResolvedListener(myResolveListener: ResolveListene?) :
    NsdManager.ResolveListener {
    private var myResolveListener: ResolveListene? = null
    private var observable: Observable<ScannedData>? = null

    interface ResolveListene {
        fun onDeviceFound(data: ScannedData?)
    }

    init {
        this.myResolveListener = myResolveListener
    }

    override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {}

    @SuppressLint("CheckResult")
    override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
        val data = ScannedData()
        data.hostAddress = serviceInfo.host
        data.port = serviceInfo.port
        data.serviceName = serviceInfo.serviceName
        data.serviceType = serviceInfo.serviceType
        observable = Observable.just<ScannedData>(data)
        observable!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<ScannedData> { result: ScannedData? ->
                myResolveListener!!.onDeviceFound(
                    result
                )
            })
    }
}