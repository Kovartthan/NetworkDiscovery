package com.kovartthan.myapplication.ui


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kovartthan.myapplication.R
import com.kovartthan.myapplication.model.ScanModel
import kotlinx.android.synthetic.main.layout_item.view.*


class ScanListAdapter(var context : Context) : RecyclerView.Adapter<ScanListAdapter.ScanViewHolder>() {
    private var scanModelList : ArrayList<ScanModel> ?= ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false)
        return ScanViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return  scanModelList?.size!!
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        holder.bind(scanModelList!![position])
    }

    class ScanViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ScanModel) {
            item?.let {
                itemView.txtName.text = item.name
                itemView.txtPort.text = item.port.toString()
                itemView.txtIp.text = item.address.toString()
                itemView.txtType.text = item.type
            }
        }
    }

    fun updateList(scanModel: ScanModel){
        this.scanModelList?.add(scanModel)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.scanModelList?.clear();
    }
}