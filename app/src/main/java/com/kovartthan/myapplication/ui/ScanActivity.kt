package com.kovartthan.myapplication.ui

import android.app.ProgressDialog
import android.net.nsd.NsdManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.lifecycle.ViewModelProviders
import com.kovartthan.myapplication.R
import com.kovartthan.myapplication.databinding.ActivityMainBinding
import com.kovartthan.myapplication.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class ScanActivity : AppCompatActivity(), ScanPostListener {
    private lateinit var model: ScanViewModel
    private var scanListAdapter: ScanListAdapter? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProviders.of(this).get(ScanViewModel::class.java)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            model.scanPostListener = this@ScanActivity
        }

        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Scanning....")
        progressDialog?.setCancelable(false)

        binding.btnScan.setOnClickListener {
            model.onScan(this@ScanActivity)
        }
        binding.btnPublish.setOnClickListener {
            model.onPublish(this@ScanActivity)
        }

        doBindingForList()
    }


    private fun doBindingForList() {
        scanListAdapter = ScanListAdapter(this)
        rvScannedList.adapter = scanListAdapter
        model.scanModelList?.observe(this, Observer {
            scanListAdapter?.updateList(it)
        })
    }

    override fun showProgress(isScanning: Boolean) {
        if (isScanning) {
            progressDialog?.show()
        } else {
            progressDialog?.dismiss()
        }
    }


    override fun onShow(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
