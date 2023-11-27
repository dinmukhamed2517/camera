package kz.just_code.mls

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kz.just_code.mls.databinding.FragmentAudioBinding

class AudioFragment : Fragment(R.layout.fragment_audio) {

    private lateinit var binding: FragmentAudioBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAudioBinding.bind(view)

        val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { fileUri ->
                    // Enable buttons since a file is selected
                    enableButtons()

                    // Play the video file
                    playVideo(fileUri)
                }
            }
        }

        binding.pickFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            pickFileLauncher.launch(intent)
        }

        binding.playButton.setOnClickListener {
            binding.videoView.start()
        }

        binding.pauseButton.setOnClickListener {
            binding.videoView.pause()
        }

        binding.stopButton.setOnClickListener {
            binding.videoView.stopPlayback()
            // Disable buttons after stopping playback
            disableButtons()
        }
    }

    private fun playVideo(videoUri: Uri) {
        // Set the URI of the video to be played
        binding.videoView.setVideoURI(videoUri)
        // Start playing the video
        binding.videoView.start()
    }

    private fun enableButtons() {
        binding.playButton.isEnabled = true
        binding.pauseButton.isEnabled = true
        binding.stopButton.isEnabled = true
    }

    private fun disableButtons() {
        binding.playButton.isEnabled = false
        binding.pauseButton.isEnabled = false
        binding.stopButton.isEnabled = false
    }
}