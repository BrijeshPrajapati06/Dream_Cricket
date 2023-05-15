package com.diyantech.dreamcricket.activity

import android.animation.Animator
import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView
import com.diyantech.dreamcricket.MainActivity
import com.diyantech.dreamcricket.R
import com.diyantech.dreamcricket.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity(R.layout.activity_splash_screen) {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showPlayVideoLottieAnimation()
    }
    fun showPlayVideoLottieAnimation() {
        binding.animationView.apply {
            show()
            speed = 0.8F
            setAnimation("117144-cricket-fever-animation.json")
            playAnimation()
            addAnimatorListener(@RequiresApi(Build.VERSION_CODES.HONEYCOMB)
            object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    Log.e("Animation:", "start")
                }
                override fun onAnimationEnd(animation: Animator) {
                    Log.e("Animation:", "end")
                    binding.animationView.visibility = View.GONE
                    val intent = Intent(this@SplashScreen,MainActivity::class.java)
                    startActivity(intent)
//                    binding.validLottieAnimationView.visibility = View.GONE
                }
                override fun onAnimationCancel(animation: Animator) {
                    Log.e("Animation:", "cancel")
                }
                override fun onAnimationRepeat(animation: Animator) {
                    Log.e("Animation:", "repeat")

                }
            })
        }
    }


}

private fun LottieAnimationView.show() {
    this.visibility = View.VISIBLE
}
