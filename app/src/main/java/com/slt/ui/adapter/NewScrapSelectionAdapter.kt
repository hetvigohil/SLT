package com.slt.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.slt.R
import com.slt.model.AvailableItem
import kotlinx.android.synthetic.main.item_scrap_collection.view.*


class NewScrapSelectionAdapter : RecyclerView.Adapter<NewScrapSelectionAdapter.NewScrapViewHolder>() {

    var mData : ArrayList<AvailableItem> = ArrayList()
    var onClick: ((categoryModel : AvailableItem,position : Int) -> Unit)? = null
    var onDelete: ((categoryModel : AvailableItem,position : Int) -> Unit)? = null

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

        val options = RequestOptions()
        options.centerCrop()


        item.filePath?.let {

            holder.itemView.ivDelete.visibility = View.VISIBLE
            holder.itemView.ivDone.visibility = View.VISIBLE
            holder.itemView.ivSuccessfully.visibility = View.VISIBLE
            holder.itemView.ivDefault.visibility = View.GONE
            holder.itemView.image.visibility = View.VISIBLE
            holder.itemView.ivArrow.visibility = View.GONE

//            holder.itemView.llWithoutImage.visibility = View.GONE
//            holder.itemView.llImageSuccessfully.visibility = View.VISIBLE
//            holder.itemView.tvSelectedItemName.text = item.name

//            holder.itemView.image.setImageBitmap(it)
            Glide.with(holder.itemView.context).load(item.filePath).apply(options).into(holder.itemView.image)

        }?:kotlin.run {
           /* holder.itemView.llWithoutImage.visibility = View.VISIBLE
            holder.itemView.llImageSuccessfully.visibility = View.INVISIBLE
            holder.itemView.tvItemName.text = item.name
*/
            holder.itemView.ivDelete.visibility = View.GONE
            holder.itemView.ivDone.visibility = View.GONE
            holder.itemView.ivSuccessfully.visibility = View.GONE
            holder.itemView.ivDefault.visibility = View.VISIBLE
            holder.itemView.image.visibility = View.GONE
            holder.itemView.ivArrow.visibility = View.VISIBLE
            Glide.with(holder.itemView.context).load(item.itemImage).apply(options).into(holder.itemView.image)

        }
        holder.itemView.tvSelectedItemName.text = item.name
        holder.itemView.setOnClickListener {
            onClick?.invoke(item,position)
        }

        holder.itemView.ivDelete.setOnClickListener {
            onDelete?.invoke(item,position)
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