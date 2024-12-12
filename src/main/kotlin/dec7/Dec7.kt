package org.example.dec7

import org.example.InputReader

fun main() {
    val inputString = InputReader().readFile("dec7/test.txt")
    println("Result: ${Dec7.task1(inputString)}")
    println("Result: ${Dec7.task2(inputString)}")
}

typealias Operation = (Long, Long) -> Long

class Dec7 {
    companion object {
        fun task1(input: String): Long = getCalibrationResult(prepareInput(input), listOf(::add, ::multiply))

        fun task2(input: String): Long = getCalibrationResult(prepareInput(input), listOf(::add, ::multiply, ::concatenate))

        private fun getCalibrationResult(
            resultAndOperands: List<Pair<Long, List<Long>>>,
            operations: List<Operation>,
        ): Long {
            val correctResults =
                resultAndOperands.filter {
                    val operationsCombinations = generateCombinations(operations, it.second.size - 1)
                    operationsCombinations.any { combination ->
                        return@any it.first ==
                            it.second.reduceIndexed { index, number, acc ->
                                combination[index - 1](number, acc)
                            }
                    }
                }
            return correctResults.sumOf { it.first }
        }

        private fun prepareInput(input: String): List<Pair<Long, List<Long>>> {
            return input.split(Regex("\\n+")).map {
                val resultAndOperands = it.split(":")
                return@map Pair(
                    resultAndOperands[0].toLong(),
                    resultAndOperands[1].split(Regex("\\s")).filterNot { it.isBlank() }.map { it.toLong() },
                )
            }
        }

        private fun add(
            a: Long,
            b: Long,
        ): Long = a + b

        private fun multiply(
            a: Long,
            b: Long,
        ): Long = a * b

        private fun concatenate(
            a: Long,
            b: Long,
        ): Long = (a.toString() + b.toString()).toLong()

        private fun generateCombinations(
            operations: List<Operation>,
            n: Int,
        ): List<List<Operation>> {
            fun generateRecursive(
                current: List<Operation>,
                remaining: Int,
            ): List<List<Operation>> {
                if (remaining == 0) {
                    return listOf(current)
                }

                val result = mutableListOf<List<Operation>>()
                operations.forEach {
                    result += generateRecursive(current + it, remaining - 1)
                }
                return result
            }

            return generateRecursive(emptyList(), n)
        }
    }
}
