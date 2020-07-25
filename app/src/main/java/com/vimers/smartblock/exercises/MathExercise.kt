package com.vimers.smartblock.exercises

import kotlin.random.Random

class MathExercise(settings: Settings) : Exercise(settings) {
    private val num1 = Random.nextInt(settings.num1Limit)
    private val num2 = Random.nextInt(settings.num2Limit)

    enum class MathAction(private val symbol: String, val perform: (Int, Int) -> Int) {
        ADDITION("+", { a, b -> a + b }),
        SUBTRACTION("-", { a, b -> a - b }),
        MULTIPLICATION("*", { a, b -> a * b }),
        DIVISION("÷", { a, b -> a / b });

        override fun toString() = symbol
    }

    private val action = MathAction.values().random()

    override val question
        get() = "$num1 $action $num2 = ?"

    private val correctAnswer = action.perform(num1, num2)

    override fun checkAnswer(answer: String) = answer == correctAnswer.toString()

    class Settings : Exercise.Settings() {
        var num1Limit = 10
        var num2Limit = 10
        var actions = mutableListOf<MathAction>(MathAction.ADDITION)
    }
}