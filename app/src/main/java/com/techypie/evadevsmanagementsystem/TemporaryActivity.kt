package com.techypie.evadevsmanagementsystem

import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TemporaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temporary)

        var anim = ValueAnimator.ofInt(0, 5000)
        anim.duration = 4000

        anim.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {

                findViewById<ProgressBar>(R.id.progressBar).progress =
                    animation.animatedValue.toString().toInt()

                when (animation.animatedValue.toString().toInt()) {
                    in 0..30 -> findViewById<ProgressBar>(R.id.progressBar).progressDrawable =
                        resources.getDrawable(R.drawable.progress_bg_red, null)

                    in 31..70 -> findViewById<ProgressBar>(R.id.progressBar).progressDrawable =
                        resources.getDrawable(R.drawable.progress_bg_yellow, null)

                    else -> findViewById<ProgressBar>(R.id.progressBar).progressDrawable =
                        resources.getDrawable(R.drawable.progress_bg_green, null)
                }
            }
        })
        anim.start()


    }
}