package com.example.dadm.view.fragment
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.dadm.R
import com.example.dadm.databinding.FragmentHomeBinding
import com.example.dadm.viewmodel.ChallengeViewModel
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var audioBackground: MediaPlayer
    private lateinit var audioSpinBottle: MediaPlayer

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
        audioBackground = MediaPlayer.create(context, R.raw.background_music)
        audioSpinBottle = MediaPlayer.create(context, R.raw.spinning_bottle)
        audioBackground.start()
    }

    private fun controladores(view: View) {
        navController = Navigation.findNavController(view)

        binding.toolbarHome.icAddChallenge.setOnClickListener {
            audioBackground.pause()
            findNavController().navigate(R.id.action_homeFragment_to_agregarRetoFragment2)
            challengeViewModel.estadoMostrarDialogo(false)
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

    }

    private fun observerViewModel() {
        observerRotationBottle()
        observerEnableButton()
//      observerEnableStreamers()
        observerDialogChallenge()
        observerCountdown()
    }

    private fun observerDialogChallenge() {
        // Observar el estado de visibilidad del diálogo
        challengeViewModel.statusShowDialog.observe(viewLifecycleOwner) { shouldShowDialog ->
            if (shouldShowDialog) {
                // Pausar el audio de fondo mientras el diálogo está activo
                audioBackground.pause()

                // Mostrar el diálogo con el mensaje de desafío
                val messageChallenge = "Debes tomar un trago"
                challengeViewModel.dialogoMostrarReto(
                    requireContext(),
                    audioBackground,
                    isMute,
                    messageChallenge
                )

                // Restablece el estado para que no se muestre continuamente
                challengeViewModel.resetStatusShowDialog()

                // Reanuda el audio de fondo después de cerrar el diálogo
                audioBackground.start()
            }
        }
    }
    

    private fun observerEnableButton() {
        challengeViewModel.enableButton.observe(viewLifecycleOwner) { enableButton ->
            // Verificar si la botella está girando o si la cuenta regresiva está activa
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
                // Mostrar la cuenta regresiva en pantalla
                binding.tvCountdown.isVisible = true
                binding.tvCountdown.text = countdownValue.toString()

                // Ocultar el botón mientras la cuenta regresiva está activa
                binding.buttonAnimation.isVisible = false
            }

            if (countdownValue == 0) {
                // Ocultar la cuenta regresiva al finalizar
                binding.tvCountdown.isVisible = false

                // Mostrar el botón nuevamente después de la cuenta regresiva
                binding.buttonAnimation.isVisible = true

                // Aquí activamos el estado para que el cuadro de diálogo de reto se muestre
                challengeViewModel.setStatusShowDialog(true)
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