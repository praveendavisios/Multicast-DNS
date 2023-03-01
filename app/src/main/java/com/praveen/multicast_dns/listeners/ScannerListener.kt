package com.praveen.multicast_dns.listeners

import android.net.nsd.NsdManager.DiscoveryListener
import android.net.nsd.NsdServiceInfo

class ScannerListener(discovery: ScanDiscovery?) :
    DiscoveryListener {
    private var discovery: ScanDiscovery? = null

    init {
        this.discovery = discovery
    }

    interface ScanDiscovery {
        fun onSeriveFound(serviceInfo: NsdServiceInfo?)
        fun discoveryStatus(message: String?)
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        discovery!!.discoveryStatus("Start Scanning failed")
    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        discovery!!.discoveryStatus("Stop Scanning failed")
    }

    override fun onDiscoveryStarted(serviceType: String) {
        discovery!!.discoveryStatus("Scanning started")
    }

    override fun onDiscoveryStopped(serviceType: String) {
        discovery!!.discoveryStatus("Scanning stop")
    }

    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
        discovery!!.onSeriveFound(serviceInfo)
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo) {
        discovery!!.discoveryStatus("Service lost")
    }
}