package org.example.dec5

import org.example.InputReader

fun main() {
    val inputString = InputReader().readFile("dec5/test.txt")
    println("Sum of correct updates: ${Dec5.task1(inputString)}")
    println("Sum of corrected updates: ${Dec5.task2(inputString)}")
}

class Dec5 {
    companion object {
        fun task1(input: String): Int {
            val rulesAndUpdates = prepareInput(input)
            return rulesAndUpdates.updates
                .filter {
                    doesUpdateSatisfyRules(it, rulesAndUpdates.rules)
                }.sumOf { it[it.size / 2] }
        }

        fun task2(input: String): Int {
            val rulesAndUpdates = prepareInput(input)
            return rulesAndUpdates.updates
                .filterNot {
                    doesUpdateSatisfyRules(it, rulesAndUpdates.rules)
                }.map { fixUpdate(it, rulesAndUpdates.rules) }
                .sumOf { it[it.size / 2] }
        }

        private fun doesUpdateSatisfyRules(
            update: List<Int>,
            rules: List<Pair<Int, Int>>,
        ): Boolean {
            return rules.all {
                val firstIndex = update.indexOf(it.first)
                val secondIndex = update.indexOf(it.second)
                return@all (firstIndex < secondIndex) or (firstIndex == -1) or (secondIndex == -1)
            }
        }

        private fun fixUpdate(
            update: List<Int>,
            rules: List<Pair<Int, Int>>,
        ): List<Int> {
            val remainingNumbers = update.toMutableSet()
            val illegalPairs = rules.map { pair -> pair.second to pair.first }
            val result = mutableListOf<Int>()

            while (remainingNumbers.size > 1) {
                var next = -1
                for (x in remainingNumbers) {
                    var ok = true
                    for (y in remainingNumbers) {
                        if (x == y) continue

                        if (illegalPairs.contains(Pair(x, y))) {
                            ok = false
                            break
                        }
                    }

                    if (ok) {
                        next = x
                        break
                    }
                }

                result.add(next)
                remainingNumbers.remove(next)
            }

            return result
        }

        private fun prepareInput(input: String): RulesAndUpdates {
            val rulesAndUpdates =
                input
                    .split("\n")
                    .filter { it.isNotBlank() }
                    .groupBy {
                        it.contains(Regex("\\|"))
                    }

            val rules: List<Pair<Int, Int>> =
                rulesAndUpdates[true]!!.map { rule ->
                    rule.split("|")[0].toInt() to rule.split("|")[1].toInt()
                }
            val updates =
                rulesAndUpdates[false]!!.map { update ->
                    update.split(",").map { it.toInt() }
                }

            return RulesAndUpdates(rules, updates)
        }

        private data class RulesAndUpdates(
            val rules: List<Pair<Int, Int>>,
            val updates: List<List<Int>>,
        )
    }
}
