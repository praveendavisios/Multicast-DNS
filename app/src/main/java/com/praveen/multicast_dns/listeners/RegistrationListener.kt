package com.praveen.multicast_dns.listeners

import android.net.nsd.NsdManager.RegistrationListener
import android.net.nsd.NsdServiceInfo

class RegistrationListener(registration: Registration?) :
    RegistrationListener {
    private var registration: Registration? = null

    interface Registration {
        fun onDeviceRegistration(message: String?)
    }

    init {
        this.registration = registration
    }

    override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        registration!!.onDeviceRegistration("Registration failed")
    }

    override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        registration!!.onDeviceRegistration("Unregistration failed")
    }

    override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
        registration!!.onDeviceRegistration("Registered Success")
    }

    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
        registration!!.onDeviceRegistration("Unregistered Success")
    }
}