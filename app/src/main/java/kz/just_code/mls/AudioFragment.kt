package kz.just_code.mls

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kz.just_code.mls.databinding.FragmentAudioBinding
import java.io.IOException

class AudioFragment : Fragment(R.layout.fragment_audio) {

    private lateinit var binding: FragmentAudioBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAudioBinding.bind(view)

        val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { fileUri ->
                    // Enable buttons since a file is selected
                    enableButtons()

                    // Play the audio file
                    playAudio(fileUri)
                }
            }
        }

        binding.pickFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
            pickFileLauncher.launch(intent)
        }

        binding.playButton.setOnClickListener {
            mediaPlayer?.start()
        }

        binding.pauseButton.setOnClickListener {
            mediaPlayer?.pause()
        }

        binding.stopButton.setOnClickListener {
            mediaPlayer?.stop()
            // Reset the MediaPlayer to its uninitialized state
            mediaPlayer?.reset()
            // Disable buttons after stopping playback
            disableButtons()
        }
    }

    private fun playAudio(audioUri: Uri) {
        releaseMediaPlayer()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(requireContext(), audioUri)
                prepare()
                start()
            }
        } catch (e: IOException) {
            // Handle the exception
            e.printStackTrace()
        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
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

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
    }
}
