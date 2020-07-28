package com.vimers.smartblock.exercises

import android.content.Context
import com.vimers.smartblock.persistence.PersistentObject
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object ExerciseFactory {
    /** List of all types of exercises. */
    private val ALL_TYPES = listOf<KClass<out Exercise>>(
            MathExercise::class
    );

    /** Returns a random exercise instance from all exercise types that are enabled. */
    fun createRandomFromEnabled(context: Context): Exercise {
        val enabledExerciseTypes = getEnabledExercises(context)
        return instantiate(context, enabledExerciseTypes.random())
    }

    @Suppress("UNCHECKED_CAST")
    private fun getExerciseSettingsClass(klass: KClass<out Exercise>) = (
            klass.nestedClasses.find { it.simpleName == "Settings" }
                    ?: throw ClassNotFoundException(
                            "${klass.qualifiedName} does not have a nested " +
                                    "Exercise.Settings subclass named exactly \"Settings\""
                    )
            ) as KClass<Exercise.Settings>

    private fun getExerciseSettings(
            context: Context,
            exerciseClass: KClass<out Exercise>
    ): Exercise.Settings {
        val exerciseSettingsClass = getExerciseSettingsClass(exerciseClass)
        return PersistentObject(context, exerciseSettingsClass)
                .obj
    }

    private fun getEnabledExercises(context: Context) =
            ALL_TYPES.filter { getExerciseSettings(context, it).enabled }

    private fun instantiate(context: Context, klass: KClass<out Exercise>): Exercise {
        return klass.primaryConstructor!!.call(getExerciseSettings(context, klass))
    }
}