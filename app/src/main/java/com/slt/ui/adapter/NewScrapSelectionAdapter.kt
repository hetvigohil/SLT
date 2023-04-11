package com.slt.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slt.R
import com.slt.model.AvailableItem
import kotlinx.android.synthetic.main.item_scrap_collection.view.*


class NewScrapSelectionAdapter : RecyclerView.Adapter<NewScrapSelectionAdapter.NewScrapViewHolder>() {

    var mData : ArrayList<AvailableItem> = ArrayList()
    var onClick: ((categoryModel : AvailableItem,position : Int) -> Unit)? = null

    class NewScrapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewScrapViewHolder {
        return NewScrapViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scrap_collection, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewScrapViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val item = mData[position]

        Log.e("onBindViewHolder",""+item)

        item.filePath?.let {
            holder.itemView.llWithoutImage.visibility = View.INVISIBLE
            holder.itemView.llImageSuccessfully.visibility = View.VISIBLE
            holder.itemView.tvSelectedItemName.text = item.name

//            holder.itemView.image.setImageBitmap(it)
            Glide.with(holder.itemView.context).load(item.filePath).into(holder.itemView.image)

        }?:kotlin.run {
            holder.itemView.llWithoutImage.visibility = View.VISIBLE
            holder.itemView.llImageSuccessfully.visibility = View.INVISIBLE
            holder.itemView.tvItemName.text = item.name
        }

        holder.itemView.setOnClickListener {
            onClick?.invoke(item,position)
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun addData(mData : ArrayList<AvailableItem>){
        this.mData = mData
        notifyDataSetChanged()
    }

    fun getSelected(): ArrayList<AvailableItem>{
        var mdata : ArrayList<AvailableItem> = ArrayList()
        mData?.forEach {
            it.filePath?.let {file->
                mdata.add(it)
            }
        }
        return mdata
    }

    fun getMediaID(): ArrayList<AvailableItem> {
        var mdata: ArrayList<AvailableItem> = ArrayList()
        mData?.forEach {
            if (it.mediaId != "") {
                mdata.add(it)
            }
        }
        return mdata
    }
}