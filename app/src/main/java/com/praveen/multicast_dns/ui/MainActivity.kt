package com.praveen.multicast_dns.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.praveen.multicast_dns.R
import com.praveen.multicast_dns.adapters.DataAdapter
import com.praveen.multicast_dns.model.ScannedData
import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var scanner: Button? = null
    var btn_publish: Button? = null
    var recyclerView: RecyclerView? = null
    private var scanDataAdapter: DataAdapter? = null
    private var scandata: MutableList<ScannedData> = ArrayList<ScannedData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClick(v: View?) {
    }


}