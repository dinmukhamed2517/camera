package kz.just_code.mls

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kz.just_code.mls.databinding.FragmentGalleryBinding


class GalleryFragment : BaseFragment<FragmentGalleryBinding>(FragmentGalleryBinding::inflate) {



    private val args: GalleryFragmentArgs by navArgs()

    override fun onBindView() {
        super.onBindView()
        val base64String: String? = (requireActivity() as MainActivity).sharedViewModel.imageData
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val imageList = mutableListOf<Bitmap>()
        imageList.add(bitmap)
        val galleryAdapter = GalleryAdapter()
        galleryAdapter.submitList(imageList)
        binding.recyclerView.adapter = galleryAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    }
}



