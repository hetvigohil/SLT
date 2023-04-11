package com.slt.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.slt.R
import com.slt.base.BaseActivity
import com.slt.extensions.makeException
import com.slt.extra.Constants
import com.slt.model.ScrapLocationModel
import com.slt.ui.adapter.NewScrapSelectionAdapter
import com.slt.viewmodel.HomeViewModel
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_new_scrap.*
import kotlinx.android.synthetic.main.activity_new_scrap.ivBack
import kotlinx.android.synthetic.main.activity_scanner.*
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NewScrapActivity : BaseActivity(R.layout.activity_new_scrap) {

    lateinit var newScrapSelectionAdapter: NewScrapSelectionAdapter
    var pos = 0
    private lateinit var mHomeViewModel: HomeViewModel
    var locationID: String = ""
    var REQUEST_TAKE_PHOTO = 1
    var mCurrentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHomeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        rvSelectCollection.layoutManager = LinearLayoutManager(this)
        newScrapSelectionAdapter = NewScrapSelectionAdapter()
        rvSelectCollection.adapter = newScrapSelectionAdapter

        val item: ScrapLocationModel? = Constants.scrapLocationModel
        locationID = intent.getStringExtra(Constants.LOCATIONID).toString()
        item?.let {
            tvZone.text = it.zone
            tvLocation.text = it.location
            it.availableItem?.let { it1 -> newScrapSelectionAdapter.addData(it1) }
        }

        newScrapSelectionAdapter.onClick = { item, position ->
            pos = position

            var permission: ArrayList<String> = ArrayList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission.add(Manifest.permission.CAMERA)
                permission.add(Manifest.permission.READ_MEDIA_IMAGES)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permission.add(Manifest.permission.CAMERA)
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                permission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            Dexter.withActivity(this@NewScrapActivity)
                .withPermissions(
                    permission
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            dispatchTakePictureIntent()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) { /* ... */
                    }
                }).onSameThread().check()

        }


        ivBack.setOnClickListener {
            onBackPressed()
        }

        rlSubmit.setOnClickListener {

            newScrapSelectionAdapter.mData.forEachIndexed { index, availableItem ->
                availableItem.filePath?.let {
                    val f: File = File(availableItem.filePath.toString())
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
                        startActivity(
                            Intent(
                                applicationContext,
                                DashBoardActivity::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        finish()
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

                        if (newScrapSelectionAdapter.getSelected().size == newScrapSelectionAdapter.getMediaID().size) {
                            var jsonArray = JsonArray()

                            newScrapSelectionAdapter.getSelected().forEach {
                                val `object` = JsonObject()
                                try {
                                    `object`.addProperty("item", it.code)
                                    `object`.addProperty("mediaID", it.mediaId)
                                    jsonArray.add(`object`)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }

                            val `object` = JsonObject()
                            try {
                                `object`.addProperty("locationID", locationID)
                                `object`.add("mediaJSON", jsonArray)

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
        if (requestCode == REQUEST_TAKE_PHOTO) {
            val newimage_path = CompressFILE(mCurrentPhotoPath)
//            val newimage_path = data?.data.toString()
//            val bitmap : Bitmap = data?.extras?.get("data") as Bitmap
//            newScrapSelectionAdapter.mData[pos].imagePath = bitmap
            newScrapSelectionAdapter.mData[pos].filePath = newimage_path
            newScrapSelectionAdapter.notifyDataSetChanged()
        }
    }


    fun CompressFILE(filePath: String?): String? {
        val compressPath: String?

        //compressPath = SiliCompressor.with(getApplicationContext()).compress(filePath, globalClass.attachFolder);
        compressPath = try {
            val compressedImage: File = Compressor(this)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(60)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                    ).absolutePath
                )
                .compressToFile(File(filePath))
            compressedImage.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            filePath
        }
        return compressPath
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this,
                    "com.slt.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }


}