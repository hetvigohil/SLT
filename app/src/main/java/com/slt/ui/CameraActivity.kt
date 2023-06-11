package com.slt.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.slt.R
import com.slt.base.BaseActivity
import com.slt.extra.Constants
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : BaseActivity(R.layout.activity_camera) ,SurfaceHolder.Callback, Camera.PictureCallback{

    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val permission: ArrayList<String> = ArrayList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission.add(Manifest.permission.CAMERA)
            permission.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permission.add(Manifest.permission.CAMERA)
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        Dexter.withActivity(this@CameraActivity)
            .withPermissions(
                permission,
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        setupSurfaceHolder()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) { *//* ... *//*
                }
            }).onSameThread().check()*/

        setupSurfaceHolder()

        ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setupSurfaceHolder() {
        startBtn.visibility = View.VISIBLE
        surfaceView.visibility = View.VISIBLE

        surfaceHolder = surfaceView.holder
        surfaceView.holder.addCallback(this)
        setBtnClick()
    }

    private fun setBtnClick() {
        startBtn.setOnClickListener { captureImage() }
    }

    private fun captureImage() {
        if (camera != null) {
            camera!!.takePicture(null, null, this)
        }
    }

    private fun startCamera() {
        camera = Camera.open()
        camera!!.setDisplayOrientation(90)
        try {
            camera!!.setPreviewDisplay(surfaceHolder)
            camera!!.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun resetCamera() {
        if (surfaceHolder!!.surface == null) {
            // Return if preview surface does not exist
            return
        }

        // Stop if preview surface is already running.
        camera!!.stopPreview()
        try {
            // Set preview display
            camera!!.setPreviewDisplay(surfaceHolder)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Start the camera preview...
        camera!!.startPreview()
    }

    private fun releaseCamera() {
        camera!!.stopPreview()
        camera!!.release()
        camera = null
    }


    override fun surfaceCreated(p0: SurfaceHolder) {
        startCamera()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        resetCamera()

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        releaseCamera()

    }

    override fun onPictureTaken(bytes: ByteArray, p1: Camera?) {
        saveImage(bytes)
        resetCamera()

    }

    private fun saveImage(bytes: ByteArray) {
        val outStream: FileOutputStream
        try {
            /*val fileName =""+System.currentTimeMillis() + ".jpeg"
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                fileName
            )*/

            @SuppressLint("SimpleDateFormat") val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
            )

            outStream = FileOutputStream(file)
            outStream.write(bytes)
            outStream.close()
            val newimage_path = CompressFILE(file.absolutePath)
            val intent = Intent()
            intent.putExtra(Constants.IMAGE_PATH,newimage_path)
            setResult(RESULT_OK,intent)
            finish()

//            Toast.makeText(this, "Picture Saved: $fileName", Toast.LENGTH_LONG).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
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


}