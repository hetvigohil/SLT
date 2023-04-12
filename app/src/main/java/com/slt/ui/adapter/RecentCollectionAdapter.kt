package com.slt.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.slt.R
import com.slt.model.CollectedItem
import com.slt.model.HistoryModel
import kotlinx.android.synthetic.main.item_recent_collection.view.*


class RecentCollectionAdapter : RecyclerView.Adapter<RecentCollectionAdapter.RecentCollectionViewHolder>() {

    var mData : ArrayList<HistoryModel> = ArrayList()
    var onClick: ((categoryModel : CollectedItem,position : Int) -> Unit)? = null

    class RecentCollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCollectionViewHolder {
        return RecentCollectionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recent_collection, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecentCollectionViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val item = mData[position]
        holder.itemView.tvZone.text = item.zone
        holder.itemView.tvLocation.text = item.location
        holder.itemView.tvDate.text = item.date
        holder.itemView.tvTime.text = item.time
        holder.itemView.llPanel.removeAllViews()

        item.collectedItem.forEach {

            val layout2: View =
                LayoutInflater.from(holder.itemView.context).inflate(com.slt.R.layout.layout_collect_item, null, false)

            val txt = layout2.findViewById<TextView>(R.id.tvTitle)
            txt.text = it.name
            holder.itemView.llPanel.addView(layout2)

            txt.setOnClickListener {view ->
                onClick?.invoke(it,position)
            }

        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun addData(mData : ArrayList<HistoryModel>){
        this.mData.clear()
        this.mData = mData
        notifyDataSetChanged()
    }
}