package com.example.imagesearchapp.ui

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.downloader.*
import com.example.imagesearchapp.R
import com.example.imagesearchapp.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.unsplash_photo_load_state_footer.*
import kotlinx.android.synthetic.main.unsplash_photo_load_state_footer.progress_bar
import com.karumi.dexter.PermissionToken

import com.karumi.dexter.MultiplePermissionsReport

import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext

import java.io.File
import java.lang.Error


class DetailFragment : Fragment() {

    lateinit var fragmentDitalBinding: FragmentDetailBinding

    private val args by navArgs<DetailFragmentArgs>()
    lateinit var urlImage: String
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        PRDownloader.initialize(requireContext());

        // Inflate the layout for this fragment
        fragmentDitalBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_detail, container, false)


        fragmentDitalBinding.apply {
            val photo = args.photo
            urlImage = photo.urls.full
            Glide.with(this@DetailFragment)
                .load(urlImage)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewCreator.isVisible = true
                        textViewDescription.isVisible = photo.description != null
                        return false
                    }

                })
                .error(R.drawable.ic_error).into(imageView)

            textViewDescription.text = photo.description

            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            textViewCreator.apply {
                text = "Photo by ${photo.user.name} on UnSplash"

                setOnClickListener {
                    context.startActivity(intent)
                }
                paint.isUnderlineText = true
            }
        }

        fragmentDitalBinding.btnDownload.setOnClickListener {
            checkDownloadPermission()
        }


        return fragmentDitalBinding.root
    }


    private fun checkDownloadPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        downloadImage()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please allow all permissions",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()
    }

    private fun downloadImage() {

        showProgressDialog()
        val file: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        PRDownloader.download(urlImage, file.path, URLUtil.guessFileName(urlImage, null, null))
            .build()
            .setOnStartOrResumeListener { }
            .setOnPauseListener { }
            .setOnCancelListener(object : OnCancelListener {
                override fun onCancel() {}
            })
            .setOnProgressListener(object : OnProgressListener {
                override fun onProgress(progress: Progress?) {
                    val per: Long = progress!!.currentBytes * 100 / progress.totalBytes
                    progressDialog.setMessage("download : $per %")

                }
            })

            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Download Complete", Toast.LENGTH_LONG).show()
                }

                override fun onError(error: com.downloader.Error?) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()

                }

            })
    }

    fun showProgressDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Downloading ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }
}