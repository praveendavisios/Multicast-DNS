package com.praveen.multicast_dns.ui

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.praveen.multicast_dns.R
import com.praveen.multicast_dns.adapters.DataAdapter
import com.praveen.multicast_dns.databinding.ActivityMainBinding
import com.praveen.multicast_dns.listeners.ScannerListener
import com.praveen.multicast_dns.listeners.RegistrationListener
import com.praveen.multicast_dns.listeners.ResolvedListener
import com.praveen.multicast_dns.model.ScannedData
import com.praveen.multicast_dns.utils.Constants
import com.praveen.multicast_dns.utils.Messages
import com.praveen.multicast_dns.utils.NSDService
import com.praveen.multicast_dns.viemodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(),
    RegistrationListener.Registration, ScannerListener.ScanDiscovery, ResolvedListener.ResolveListene {
    private var mNsdManager: NsdManager? = null
    private var isServicePublished = false
    private var isDisCoveryRunning = false
    var isPublishedClicked = false
    var isScanClicked = false
    private var scanDataAdapter: DataAdapter? = null
    var disCoveryListener: ScannerListener = ScannerListener(this)
    var mRegistrationListener: RegistrationListener = RegistrationListener(this)
    private var scandata: MutableList<ScannedData> = ArrayList<ScannedData>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel

        mNsdManager = NSDService.initializeNSDManger(this)
        recycler_view!!.layoutManager = LinearLayoutManager(this)
        recycler_view!!.setHasFixedSize(true)
        scanDataAdapter = DataAdapter(scandata)
        recycler_view!!.adapter = scanDataAdapter


        viewModel.registerBtn.observe(this, Observer {
            if (!it) return@Observer
            isPublishedClicked = true
            isScanClicked = false
            registerService(Constants.PORT)

        })

        viewModel.scanBtn.observe(this, Observer {
            if (!it) return@Observer
            isPublishedClicked = false
            isScanClicked = true
            disCoverService()

        })

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
            scan!!.text = "Scanning...."
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
                    scan!!.text = "SCAN"
                }
            }
        }
    }


}