package com.example.dadm.view.fragment
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.dadm.R
import com.example.dadm.databinding.FragmentHomeInventoryBinding
import com.example.dadm.viewmodel.ChallengeViewModel
import kotlinx.coroutines.runBlocking

class HomeInventoryFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var audioBackground: MediaPlayer
    private lateinit var audioSpinBottle: MediaPlayer
    private lateinit var audioShowChallenge: MediaPlayer
    private lateinit var audioButton: MediaPlayer
    private lateinit var audioSuspense: MediaPlayer

    private var isMute: Boolean = true

    private val challengeViewModel: ChallengeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores(view)
        observerViewModel()
        mediaController()
    }

    private fun mediaController() {
        audioBackground = MediaPlayer.create(context, R.raw.music_background)
        audioSpinBottle = MediaPlayer.create(context, R.raw.audio_bottle)
        audioShowChallenge = MediaPlayer.create(context, R.raw.audio_challenge)
        audioButton = MediaPlayer.create(context, R.raw.audio_button)
        audioSuspense = MediaPlayer.create(context, R.raw.audio_suspense)
        audioBackground.start()
    }

    private fun controladores(view: View) {
        navController = Navigation.findNavController(view)

        binding.icContainerMenu.icRules.setOnClickListener {
            audioBackground.pause()
            findNavController().navigate(R.id.action_homeFragment_to_rulesPlayFragment)
            challengeViewModel.statusShowDialog(false)
        }

        binding.btnSpin.setOnClickListener {
            challengeViewModel.spinBottle()
        }
        binding.icContainerMenu.icMuteOff.setOnClickListener {
            isMute = true
            binding.icContainerMenu.icMuteOn.isVisible = isMute
            binding.icContainerMenu.icMuteOff.isVisible = !isMute
            audioBackground.pause()

        }
        binding.icContainerMenu.icMuteOn.setOnClickListener {
            isMute = false
            binding.icContainerMenu.icMuteOff.isVisible = !isMute
            binding.icContainerMenu.icMuteOn.isVisible = isMute
            audioBackground.start()
        }
        binding.icContainerMenu.icAddChallenge.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addChallengeFragment)
        }
    }

    private fun observerViewModel() {
        observerRotationBottle()
        observerEnableButton()
        observerEnableStreamers()
        observerDialogChallenge()
    }

    private fun observerDialogChallenge() {
        challengeViewModel.estadoMostrarDialogo.observe(viewLifecycleOwner) {
            if (it) {
                runBlocking {
                    audioSuspense.start()
                    challengeViewModel.wait(3)
                }
                audioSuspense.pause()
                val messageChallenge = "Debes tomar un trago"
                challengeViewModel.dialogoMostrarReto(
                    requireContext(),
                    audioBackground, isMute,
                    messageChallenge
                )
                audioShowChallenge.start()
                audioSpinBottle.pause()
                audioButton.pause()
            }
        }
    }

    private fun observerEnableStreamers() {
        challengeViewModel.enableStreamers.observe(viewLifecycleOwner) { enableStreamer ->
            binding.lottieCerpentina.isVisible = enableStreamer
            binding.lottieCerpentina.playAnimation()
        }
    }

    private fun observerEnableButton() {
        challengeViewModel.enableButton.observe(viewLifecycleOwner) { enableButton ->
            binding.btnSpin.isVisible = enableButton
        }
    }

    private fun observerRotationBottle() {
        challengeViewModel.statusRotationBottle.observe(viewLifecycleOwner) { statusRotation ->
            if (statusRotation) {
                audioButton.start()
                audioBackground.pause()
                audioSpinBottle.start()
                challengeViewModel.rotationBottle.observe(viewLifecycleOwner) { rotation ->
                    binding.ivBottle.startAnimation(rotation)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioBackground.stop()
    }

    override fun onPause() {
        super.onPause()
        audioBackground.pause()
    }

    override fun onResume() {
        super.onResume()
        if (!isMute) {
            audioBackground.start()
        }
    }

}