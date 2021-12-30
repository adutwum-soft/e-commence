package com.example.e_commence.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.e_commence.BuildConfig
import com.example.e_commence.R
import com.example.e_commence.databinding.ShareProductBottmsheetBinding
import com.example.e_commence.loadImage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException





class ShareBottomSheet : BottomSheetDialogFragment() {
    private lateinit var _binding: ShareProductBottmsheetBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ShareProductBottmsheetBinding.inflate(inflater, container, false)

        init()

        dialog?.setOnShowListener {dialogInterface->
            val sheetDialog = dialogInterface as? BottomSheetDialog
            val bottomSheet = sheetDialog?.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            )
//            sheetDialog?.setCancelable(false)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        with(binding){
            prodImg.loadImage(img)
            prodTitle.text = title
            prodDesc.text = desc
            priceEdit.setText(price.toString())

            editImg.setOnClickListener {
                priceEdit.isEnabled = true
            }

            cancelBtn.setOnClickListener {
                dialog?.dismiss()
            }

            contBtn.setOnClickListener {
                shareContent()
            }
        }

        return binding.root
    }

    private fun share(bitmap: Bitmap){
        val uri = getLocalBitmapUri(bitmap)
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "check out $title, price: GHS${binding.priceEdit.text} description: $desc")
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            putExtra(Intent.EXTRA_STREAM, uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
            type = "image/*"
        }

        val chooser = Intent.createChooser(intent, "Share File")
        val resInfoList: List<ResolveInfo> =
            context?.packageManager!!.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            context?.grantUriPermission(
                resolveInfo.activityInfo.packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        startActivity(chooser)
    }

    private fun shareContent() {
        val runnable = Runnable {
            val bitmap = Glide.with(this).asBitmap().load( img).submit().get()
            share(bitmap)
        }
        Thread(runnable).start()
    }



    private fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = context?.let { FileProvider.getUriForFile(it, BuildConfig.APPLICATION_ID +".provider", file) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    private fun init() {
        with(binding){
            priceEdit.isEnabled = false
        }
    }

    override fun getTheme(): Int {
        return R.style.bottomSheetBackground
    }

    companion object {
        var price = 0.0
        var title = ""
        var desc = ""
        var img = ""
    }
}