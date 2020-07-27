package com.vimers.smartblock

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.vimers.smartblock.exercises.Exercise
import com.vimers.smartblock.exercises.ExerciseFactory

class Blocker(private val context: Context) {
    private lateinit var exercise: Exercise
    private val dialog: Dialog
    private val currentAppMonitor = CurrentAppMonitor(context)
    private val blockedAppsSet = BlockedAppsSet(context)

    init {
        generateNewExercise()
        dialog = BlockDialogFactory.createDialog(context, exercise)
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Проверить") { dialogInterface, _ ->
            checkAnswer(dialogInterface)
        }
    }

    private fun generateNewExercise() {
        exercise = ExerciseFactory.createRandomFromEnabled(context)
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
        generateNewExercise()
        if (canBlock())
            dialog.show()
    }

    private fun canBlock() = !currentAppMonitor.isOnHomeScreen &&
            blockedAppsSet.all.contains(currentAppMonitor.packageName)
}