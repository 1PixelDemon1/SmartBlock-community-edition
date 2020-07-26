package com.vimers.smartblock.exercises

/** Abstract class for exercises. */
abstract class Exercise(private val settings: Settings) {
    /** Exercise's question, for example "2 + 2 = ?". */
    abstract val question: String

    /** Checks if the answer is correct. */
    abstract fun checkAnswer(answer: String): Boolean

    /**
     * Class which holds additional settings for this type of exercises.
     * [Exercise] subclasses are supposed to have their own nested [Settings] class which inherits
     * this one to store their settings in.
     */
    abstract class Settings {
        /** Is this exercise type enabled at all? */
        var enabled = true
    }
}