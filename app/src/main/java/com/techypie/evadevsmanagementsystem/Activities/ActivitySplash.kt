package com.techypie.evadevsmanagementsystem.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityMainBinding
import com.techypie.evadevsmanagementsystem.databinding.ActivitySplashBinding

class ActivitySplash : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@ActivitySplash,ActivityLogin::class.java))
            finish()
        },2600)


        var animation = AnimationUtils.loadAnimation(this@ActivitySplash,R.anim.splash_screen)

        binding.containerSplash.startAnimation(animation)
    }
}