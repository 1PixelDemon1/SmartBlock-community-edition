package com.vimers.smartblock

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vimers.smartblock.persistence.PersistentObject
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {
    private lateinit var appSettings: PersistentObject<AppSettings>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appSettings = PersistentObject(this, AppSettings::class.java)
        finishIfRegistrationTakenPlace()

        setContentView(R.layout.activity_registration)

        setTextWatchers()
    }

    private fun finishIfRegistrationTakenPlace() {
        if (appSettings.obj.registrationFinished)
            finish()
    }

    private fun setTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setFabEnabled(isInputDataCorrect)
            }

            private val isInputDataCorrect: Boolean
                get() = (emailEdit.length() != 0
                        && passwordEdit.length() != 0)

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }
        emailEdit.addTextChangedListener(textWatcher)
        passwordEdit.addTextChangedListener(textWatcher)
    }

    private fun setFabEnabled(enabled: Boolean) {
        fab.setColorFilter(
                ContextCompat.getColor(
                        this,
                        if (enabled) R.color.colorAccentDisabled else R.color.colorAccent
                )
        )
        fab.isClickable = enabled
    }

    fun doRegistration(view: View) {
        appSettings.edit {
            mail = emailEdit.text.toString()
            password = passwordEdit.text.toString()
        }.save()
        startActivity(Intent(this, MainActivity::class.java))
    }
}