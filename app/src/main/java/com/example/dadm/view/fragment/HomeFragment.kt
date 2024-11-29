package com.example.dadm.view.fragment
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.dadm.R
import com.example.dadm.databinding.FragmentHomeBinding
import com.example.dadm.viewmodel.ChallengeViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import android.content.Context
import androidx.navigation.NavOptions
import com.example.dadm.view.MainActivity
import kotlinx.coroutines.runBlocking
import android.util.Log
import com.example.dadm.utils.Result


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var audioBackground: MediaPlayer
    private lateinit var audioSpinBottle: MediaPlayer
    private var isMute: Boolean = true
    private val challengeViewModel: ChallengeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores(view)
        observerViewModel()
        mediaController()
        challengeViewModel.getListChallenge()
        observerListChallenge()
        observerErrores()
    }

    private fun mediaController() {
        audioBackground = MediaPlayer.create(context, R.raw.background_music).apply {
            isLooping = true
            start()
        }
        audioSpinBottle = MediaPlayer.create(context, R.raw.spinning_bottle)
    }

    private fun controladores(view: View) {
        navController = Navigation.findNavController(view)

        binding.toolbarHome.icAddChallenge.setOnClickListener {
            audioBackground.pause()
            findNavController().navigate(R.id.action_homeFragment_to_agregarRetoFragment2)
        }

        binding.buttonAnimation.setOnClickListener {
            challengeViewModel.spinBottle()
        }

        binding.toolbarHome.icMuteOff.setOnClickListener {
            isMute = true
            binding.toolbarHome.icMuteOn.isVisible = isMute
            binding.toolbarHome.icMuteOff.isVisible = !isMute
            audioBackground.pause()
        }

        binding.toolbarHome.icMuteOn.setOnClickListener {
            isMute = false
            binding.toolbarHome.icMuteOff.isVisible = !isMute
            binding.toolbarHome.icMuteOn.isVisible = isMute
            audioBackground.start()
        }

        binding.toolbarHome.icRules.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rulesPlayFragment)
        }

        binding.toolbarHome.icStar.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es")
            }
            view.context.startActivity(intent)
        }

        binding.toolbarHome.icShareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "App pico botella")
                val shareMessage = "App pico botella\n¡Solo los valientes lo juegan !!\nhttps://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }
            startActivity(Intent.createChooser(shareIntent, "Compartir usando"))
        }

        binding.toolbarHome.icLogout.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
//
        // Cerrar sesión de Firebase
        FirebaseAuth.getInstance().signOut()

        // Limpiar SharedPreferences
        requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()

        // Reiniciar la actividad principal
        requireActivity().apply {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun observerViewModel() {
        observerRotationBottle()
        observerEnableButton()
        observerDialogChallenge()
        observerCountdown()
    }

    private fun observerErrores() {
        challengeViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observerDialogChallenge() {
        challengeViewModel.statusShowDialog.observe(viewLifecycleOwner) { shouldShowDialog ->
            if (shouldShowDialog) {
                audioBackground.pause()

                challengeViewModel.listChallenge.observe(viewLifecycleOwner) { resultado ->
                    when (resultado) {
                        is Result.Success -> {
                            if (resultado.data.isNotEmpty()) {
                                val retoAleatorio = resultado.data.random()
                                challengeViewModel.dialogoMostrarReto(
                                    requireContext(),
                                    audioBackground,
                                    isMute,
                                    retoAleatorio.descripcion
                                )
                            } else {
                                Toast.makeText(context, "No hay retos disponibles", Toast.LENGTH_SHORT).show()
                            }
                        }
                        is Result.Failure -> {
                            Toast.makeText(
                                context,
                                "Error al cargar los retos: ${resultado.exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                challengeViewModel.resetStatusShowDialog()

                if (!isMute) {
                    audioBackground.start()
                }
            }
        }
    }

    private fun observerListChallenge() {
        challengeViewModel.listChallenge.observe(viewLifecycleOwner) { resultado ->
            when (resultado) {
                is Result.Success -> {
                    if (resultado.data.isEmpty()) {
                        Toast.makeText(context, "No hay retos disponibles", Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Failure -> {
                    Toast.makeText(
                        context,
                        "Error al cargar los retos: ${resultado.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observerEnableButton() {
        challengeViewModel.enableButton.observe(viewLifecycleOwner) { enableButton ->
            val isCountdownActive = challengeViewModel.countdown.value != null && challengeViewModel.countdown.value!! > 0
            binding.buttonAnimation.isVisible = enableButton && !isCountdownActive && !challengeViewModel.statusRotationBottle.value!!
        }
    }

    private fun observerRotationBottle() {
        challengeViewModel.statusRotationBottle.observe(viewLifecycleOwner) { statusRotation ->
            if (statusRotation) {
                audioBackground.pause()
                audioSpinBottle.start()
                challengeViewModel.rotationBottle.observe(viewLifecycleOwner) { rotation ->
                    binding.ivBottle.startAnimation(rotation)
                }
            }
        }
    }

    private fun observerCountdown() {
        challengeViewModel.countdown.observe(viewLifecycleOwner) { countdownValue ->
            if (countdownValue != null) {
                binding.tvCountdown.isVisible = true
                binding.tvCountdown.text = countdownValue.toString()
                binding.buttonAnimation.isVisible = false
            }

            if (countdownValue == 0) {
                binding.tvCountdown.isVisible = false
                binding.buttonAnimation.isVisible = true
                //challengeViewModel.setStatusShowDialog(true)
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