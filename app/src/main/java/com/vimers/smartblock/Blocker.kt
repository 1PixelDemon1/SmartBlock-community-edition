package com.vimers.smartblock

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.vimers.smartblock.exercises.ExerciseFactory

class Blocker(private val context: Context) {
    private var exercise = ExerciseFactory.createRandomFromEnabled(context)
    private val dialog = BlockDialogFactory.createDialog(context, exercise)
    private val currentAppMonitor = CurrentAppMonitor(context)
    private val blockedAppsSet = BlockedAppsSet(context)

    init {
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Проверить") { dialogInterface, _ ->
            checkAnswer(dialogInterface)
        }
    }

    private fun checkAnswer(dialogInterface: DialogInterface) {
        val givenAnswer = dialog.findViewById<EditText>(R.id.answerInput).text.toString()
        if (exercise.checkAnswer(givenAnswer)) {
            dialogInterface.dismiss()
        } else {
            dialog.findViewById<TextView>(R.id.wrongAnswer).visibility = View.VISIBLE
        }
    }

    fun block() {
        if (canBlock())
            dialog.show()
        exercise = ExerciseFactory.createRandomFromEnabled(context)
    }

    private fun canBlock() = !currentAppMonitor.isOnHomeScreen() &&
            blockedAppsSet.all.contains(currentAppMonitor.getCurrentRunningApp())
}