package com.praveen.multicast_dns.ui

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.praveen.multicast_dns.R
import com.praveen.multicast_dns.adapters.DataAdapter
import com.praveen.multicast_dns.listeners.ScannerListener
import com.praveen.multicast_dns.listeners.RegistrationListener
import com.praveen.multicast_dns.listeners.ResolvedListener
import com.praveen.multicast_dns.model.ScannedData
import com.praveen.multicast_dns.utils.Constants
import com.praveen.multicast_dns.utils.Messages
import com.praveen.multicast_dns.utils.NSDService
import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener,
    RegistrationListener.Registration, ScannerListener.ScanDiscovery, ResolvedListener.ResolveListene {
    var scanner: Button? = null
    var btn_publish: Button? = null
    var recyclerView: RecyclerView? = null
    private var mNsdManager: NsdManager? = null
    private var isServicePublished = false
    private var isDisCoveryRunning = false
    var isPublishedClicked = false
    var isScanClicked = false
    private var scanDataAdapter: DataAdapter? = null
    var disCoveryListener: ScannerListener = ScannerListener(this)
    var mRegistrationListener: RegistrationListener = RegistrationListener(this)
    private var scandata: MutableList<ScannedData> = ArrayList<ScannedData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNsdManager = NSDService.initializeNSDManger(this)
        scanner = findViewById(R.id.scan)
        btn_publish = findViewById(R.id.register)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.setHasFixedSize(true)
        scanDataAdapter = DataAdapter(scandata)
        recyclerView!!.adapter = scanDataAdapter
        if (btn_publish != null) {
            btn_publish!!.setOnClickListener(this)
        }
        if (scanner != null) {
            scandata.clear()
            scanDataAdapter!!.notifyDataSetChanged()
            scanner!!.setOnClickListener(this)
        }
    }

    override fun onPause() {
        if (mNsdManager != null) {
            unRegisterService()
            stopDisCoverService()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (mNsdManager != null) {
            if (isPublishedClicked) {
                registerService(Constants.PORT)
            }
            if (isScanClicked) {
                disCoverService()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    fun disCoverService() {
        if (!isDisCoveryRunning) {
            isDisCoveryRunning = true
            scanner!!.text = "Scanning...."
            mNsdManager!!.discoverServices(
                Constants.SERVICE_TYPE,
                NsdManager.PROTOCOL_DNS_SD, disCoveryListener
            )
        }
    }


    fun stopDisCoverService() {
        if (isDisCoveryRunning) {
            isDisCoveryRunning = false
            mNsdManager!!.stopServiceDiscovery(disCoveryListener)
        }
    }


    fun registerService(port: Int) {
        val serviceInfo = NsdServiceInfo()
        serviceInfo.serviceName = Constants.SERVICE_NAME
        serviceInfo.serviceType = Constants.SERVICE_TYPE
        serviceInfo.port = port
        if (!isServicePublished) {
            isServicePublished = true
            mNsdManager!!.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD,
                mRegistrationListener
            )
        }
    }


    fun unRegisterService() {
        if (isServicePublished) {
            isServicePublished = false
            mNsdManager!!.unregisterService(mRegistrationListener)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.register -> {
                isPublishedClicked = true
                isScanClicked = false
                registerService(Constants.PORT)
            }
            R.id.scan -> if (scanner!!.text.toString().equals("SCAN", ignoreCase = true)) {
                isPublishedClicked = false
                isScanClicked = true
                disCoverService()
            }
        }
    }

    override fun onDeviceRegistration(message: String?) {
        Messages.showToast(this, message)
    }

    override fun onSeriveFound(serviceInfo: NsdServiceInfo?) {
        mNsdManager!!.resolveService(serviceInfo, ResolvedListener(this))
    }

    override fun discoveryStatus(message: String?) {
        Messages.showToast(this, message)
    }

    override fun onDeviceFound(data: ScannedData?) {
        runOnUiThread {
            if (data != null) {
                if(!scandata.contains(data)) {
                    scandata!!.add(data)
                    scandata = scandata.distinct().toMutableList()
                    scanDataAdapter!!.notifyDataSetChanged()
                    scanner!!.text = "Scan"
                }
            }
        }
    }


}