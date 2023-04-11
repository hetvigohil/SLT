package com.slt.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slt.R
import com.slt.model.HistoryModel


class RecentCollectionAdapter : RecyclerView.Adapter<RecentCollectionAdapter.RecentCollectionViewHolder>() {

    var mData : ArrayList<HistoryModel> = ArrayList()
//    var onClick: ((categoryModel : CategoryModel,position : Int) -> Unit)? = null

    class RecentCollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCollectionViewHolder {
        return RecentCollectionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recent_collection, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecentCollectionViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val item = mData[position]

    }

    override fun getItemCount(): Int {
        return 10
    }

    fun addData(mData : ArrayList<HistoryModel>){
        this.mData = mData
        notifyDataSetChanged()
    }
}