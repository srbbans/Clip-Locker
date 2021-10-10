package bans.cliplocker

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import bans.cliplocker.databinding.ActivityAuthBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executor


class AuthActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        blinker()

        val sharedP = getSharedPreferences("settings", MODE_PRIVATE)
        if (sharedP.getBoolean("auth", false)) {
            setupAndInitiateLogin()
        } else {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private fun setupAndInitiateLogin() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Locker")
            .setSubtitle("Private access !")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }


    private fun blinker() {
        val eyeBallLeft: View = binding.emoji.eyeBallLeft
        val eyeBallRight: View = binding.emoji.eyeBallRight

        lifecycleScope.launch(Dispatchers.Main) {
            val stop = false
            while (true) {
                if (stop) break
                delay(3000)
                // Blink eye (close)
                ObjectAnimator.ofFloat(eyeBallLeft, "rotationX", 89f).apply {
                    duration = 500
                    start()
                }
                ObjectAnimator.ofFloat(eyeBallRight, "rotationX", 89f).apply {
                    duration = 500
                    start()
                }
                delay(500)

                // Blink eye (open)
                ObjectAnimator.ofFloat(eyeBallLeft, "rotationX", 0f).apply {
                    duration = 500
                    start()
                }
                ObjectAnimator.ofFloat(eyeBallRight, "rotationX", 0f).apply {
                    duration = 500
                    start()
                }
            }
        }
    }


}