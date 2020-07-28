package com.vimers.smartblock.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.vimers.smartblock.AppSettings
import com.vimers.smartblock.MainActivity
import com.vimers.smartblock.R
import com.vimers.smartblock.RegistrationActivity
import com.vimers.smartblock.persistence.PersistentObject
import kotlinx.android.synthetic.main.activity_password_input.*

class PasswordInputActivity : AppCompatActivity() {
    private val appSettings = PersistentObject<AppSettings>(this, AppSettings::class)
    private lateinit var lockDrawable: AnimatedVectorDrawableCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerRegistrationIfNeeded()
        setContentView(R.layout.activity_password_input)

        setupLockAnimation()
    }

    private fun offerRegistrationIfNeeded() {
        if (!appSettings.obj.registrationFinished)
            startActivity(Intent(this, RegistrationActivity::class.java))
    }

    private fun setupLockAnimation() {
        lockDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.lock_open_anim)!!
        lockAnimation.setImageDrawable(lockDrawable)
        lockDrawable.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                goToMainActivity()
            }
        })
    }

    fun checkPassword(view: View) {
        val suppliedPassword = passwordEdit.text.toString()
        if (appSettings.obj.password == suppliedPassword)
            onPasswordCorrect()
        else
            onPasswordWrong()
    }

    private fun onPasswordCorrect() {
        lockDrawable.start()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun onPasswordWrong() {
        passwordEdit.text.clear()
        passwordLayout.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.wrong_password_shake)
        )
    }
}