package com.slt.ui

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.slt.R
import com.slt.base.BaseActivity
import com.slt.extensions.makeException
import com.slt.extra.Constants
import com.slt.model.ScrapLocationModel
import com.slt.ui.adapter.NewScrapSelectionAdapter
import com.slt.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_new_scrap.*
import kotlinx.android.synthetic.main.activity_new_scrap.ivBack
import kotlinx.android.synthetic.main.activity_scanner.*
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.File


class NewScrapActivity : BaseActivity(R.layout.activity_new_scrap) {

    lateinit var newScrapSelectionAdapter : NewScrapSelectionAdapter
    var pos = 0
    private lateinit var mHomeViewModel: HomeViewModel
    var locationID : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHomeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        rvSelectCollection.layoutManager = LinearLayoutManager(this)
        newScrapSelectionAdapter = NewScrapSelectionAdapter()
        rvSelectCollection.adapter = newScrapSelectionAdapter

        val item: ScrapLocationModel? = intent.getParcelableExtra(Constants.SCRAPITEM)
        locationID = intent.getStringExtra(Constants.LOCATIONID).toString()
        item?.let {
            tvZone.text = it.zone
            tvLocation.text = it.location
            newScrapSelectionAdapter.addData(it.availableItem)
        }

        newScrapSelectionAdapter.onClick = { item, position ->
            pos = position
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 1000)
        }


        ivBack.setOnClickListener {
            onBackPressed()
        }

        rlSubmit.setOnClickListener {

            newScrapSelectionAdapter.mData.forEachIndexed { index, availableItem ->
                if (availableItem.filePath != ""){
                    val f: File = File(availableItem.filePath)
                    pos = index
                    getBase64(availableItem.filePath)?.let { it1 ->
                        mHomeViewModel.uploadMedia(it1, f.name, f.extension).apply {
                            showLoading()
                        }
                    }
                }
            }

        }

        mHomeViewModel.mSubmitData.observe(this) {
            hideLoading()
            when (it) {
                is com.slt.base.Result.Success -> {
                    makeToast(it.message)
                    it.data.let {
                        val intent = Intent(this,DashBoardActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                }
                is com.slt.base.Result.Error -> {
                    makeException(it.code, it.message, mainView)
//                    makeToast(it.message)
                }
                is com.slt.base.Result.ErrorCode -> {
                    makeToast(it.message)
                }
                is com.slt.base.Result.SuccessWithMessage -> {
                    makeToast(it.message)
                }
                else -> {}
            }
        }

        mHomeViewModel.mUploadMediaData.observe(this) {
            hideLoading()
            when (it) {
                is com.slt.base.Result.Success -> {
                    makeToast(it.message)
                    it.data.let {
                        newScrapSelectionAdapter.mData[pos].mediaId = it.mediaID
                        newScrapSelectionAdapter.notifyDataSetChanged()

                        if (newScrapSelectionAdapter.getSelected().size == newScrapSelectionAdapter.getMediaID().size){
                            var jsonArray = JsonArray()

                            newScrapSelectionAdapter.getSelected().forEach {
                                val `object` = JsonObject()
                                try {
                                    `object`.addProperty("item",it.code)
                                    `object`.addProperty("mediaID",it.mediaId)
                                    jsonArray.add(`object`)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }

                            val `object` = JsonObject()
                            try {
                                `object`.addProperty("locationID",locationID)
                                `object`.add("mediaJSON",jsonArray)

                                mHomeViewModel.submit(`object`).apply {
                                    showLoading()
                                }

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }

                    }
                }
                is com.slt.base.Result.Error -> {
                    makeException(it.code, it.message, mainView)
//                    makeToast(it.message)
                }
                is com.slt.base.Result.ErrorCode -> {
                    makeToast(it.message)
                }
                is com.slt.base.Result.SuccessWithMessage -> {
                    makeToast(it.message)
                }
                else -> {}
            }
        }

    }

    fun getBase64(filePath: String?): String? {
        var bmp: Bitmap? = null
        var bos: ByteArrayOutputStream? = null
        var bt: ByteArray? = null
        var encodeString: String? = null
        try {
            bmp = BitmapFactory.decodeFile(filePath)
            bos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bt = bos.toByteArray()
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return encodeString
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1000) {
            val newimage_path = data?.data.toString()
            val bitmap : Bitmap = data?.extras?.get("data") as Bitmap
            newScrapSelectionAdapter.mData[pos].imagePath = bitmap
            newScrapSelectionAdapter.mData[pos].filePath = newimage_path
            newScrapSelectionAdapter.notifyDataSetChanged()
        }
    }



}