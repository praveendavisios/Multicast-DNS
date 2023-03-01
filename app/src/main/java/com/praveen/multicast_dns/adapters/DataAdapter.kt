package com.praveen.multicast_dns.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.praveen.multicast_dns.R
import com.praveen.multicast_dns.model.ScannedData

class DataAdapter(private val mList: MutableList<ScannedData>?) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scan_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList?.get(position)

        holder.txt_service_ip.text = "IP Address  : " + ItemsViewModel!!.hostAddress
        holder.txt_service_name.text = "Service Name  : " + ItemsViewModel!!.serviceName
        holder.txt_service_type.text = "Service Type  : " + ItemsViewModel!!.serviceType
        holder.txt_service_port.text = "Port  : " + ItemsViewModel!!.port

    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var txt_service_name: TextView
        var txt_service_type: TextView
        var txt_service_ip: TextView
        var txt_service_port: TextView

        init {
            txt_service_port = itemView.findViewById(R.id.port)
            txt_service_ip = itemView.findViewById(R.id.ip)
            txt_service_name = itemView.findViewById(R.id.name)
            txt_service_type = itemView.findViewById(R.id.type)
        }
    }
}
