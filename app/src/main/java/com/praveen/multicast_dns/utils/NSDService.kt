package com.praveen.multicast_dns.utils

import android.content.Context
import android.net.nsd.NsdManager

object NSDService {

    fun initializeNSDManger(mContext: Context): NsdManager {
        return mContext.getSystemService(Context.NSD_SERVICE) as NsdManager
    }

}
