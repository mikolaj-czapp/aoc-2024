package org.example.dec3

import org.example.InputReader

fun main() {
    val inputString = InputReader().readFile("dec3/test.txt")
    println("Evaluated memory string: ${Dec3.task1(inputString)}")
    println("Evaluated conditional memory string: ${Dec3.task2(inputString)}")
}

class Dec3 {
    companion object {
        private val mulExpressionRegex = MulExpression.regex

        fun task1(input: String): Int =
            mulExpressionRegex
                .findAll(input)
                .map { MulExpression(it.value) }
                .map { it.evaluate() }
                .sum()

        fun task2(input: String): Int = conditionalEvaluate(input, MulState.DO, 0)

        private fun conditionalEvaluate(
            input: String,
            state: MulState,
            result: Int,
        ): Int {
            val nextState = nextState(input, state)
            nextState?.let {
                val nextStateRegexRange = nextState.range
                val first = nextStateRegexRange.first
                val last = nextStateRegexRange.last
                val toEval = input.substring(0, first)
                val rest = input.substring(last + 1)
                return when (state) {
                    MulState.DO -> conditionalEvaluate(rest, nextState.state, result + task1(toEval))
                    MulState.DONT -> conditionalEvaluate(rest, nextState.state, result)
                }
            }
            return when (state) {
                MulState.DO -> result + task1(input)
                MulState.DONT -> result
            }
        }

        private fun nextState(
            input: String,
            currentState: MulState,
        ): NextState? {
            val nextCurrentStateMatch = currentState.findNext(input)
            val nextOtherStateMatch = currentState.otherState().findNext(input)
            if (nextCurrentStateMatch == null && nextOtherStateMatch == null) {
                return null
            }
            nextCurrentStateMatch?.let {
                nextOtherStateMatch?.let {
                    if (nextOtherStateMatch.range.first < nextCurrentStateMatch.range.first) {
                        return NextState(currentState.otherState(), nextOtherStateMatch.range)
                    }
                    return NextState(currentState, nextCurrentStateMatch.range)
                }
            }
            nextOtherStateMatch?.let {
                return NextState(currentState.otherState(), nextOtherStateMatch.range)
            }
            throw RuntimeException("You sure you got your logic right?") // Stupid Kotlin won't let me leave nothing here even though all cases are covered
        }

        private data class NextState(
            val state: MulState,
            val range: IntRange,
        )

        private enum class MulState {
            DO {
                override fun regex() = Regex("do\\(\\)")
            },
            DONT {
                override fun regex() = Regex("don't\\(\\)")
            }, ;

            fun findNext(text: String): MatchResult? = regex().find(text)

            abstract fun regex(): Regex

            fun otherState(): MulState =
                when (this) {
                    DO -> DONT
                    DONT -> DO
                }
        }

        @JvmInline
        private value class MulExpression(
            private val expression: String,
        ) {
            companion object {
                val regex: Regex = Regex("mul\\(\\d+,\\d+\\)")
            }

            init {
                require(regex.matches(expression))
            }

            fun evaluate(): Int {
                val digitRegex = Regex("\\d+")
                val digits = digitRegex.findAll(expression)
                val firstDigit = digits.first().groupValues[0].toInt()
                val secondDigit = digits.last().groupValues[0].toInt()
                return firstDigit * secondDigit
            }
        }
    }
}
