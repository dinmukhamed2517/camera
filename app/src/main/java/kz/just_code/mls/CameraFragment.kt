
package kz.just_code.mls

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import kz.just_code.mls.databinding.FragmentCameraBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64

class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate) {

    val REQUEST_IMAGE_CAPTURE = 112
    override fun onBindView() {
        super.onBindView()

        binding.cameraButton.setOnClickListener {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try{
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE)

            }catch (e: Exception){
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(imageBitmap)
            val byteArray = convertBitmapToByteArray(imageBitmap)
            val base64String = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
            (requireActivity() as MainActivity).sharedViewModel.imageData = base64String

        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

}
