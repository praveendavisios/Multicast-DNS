package com.praveen.multicast_dns.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast

object Messages {

    fun showToast(context: Context?, message: String?) {
        if (context != null) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}
