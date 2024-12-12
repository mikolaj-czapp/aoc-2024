package org.example.dec11

import org.example.InputReader
import org.example.common.add

fun main() {
    val inputString = InputReader().readFile("dec11/input.txt")
    println("Result: ${Dec11.task1(inputString)}")
    println("Result: ${Dec11.task2(inputString)}")
}

class Dec11 {
    companion object {
        fun task1(inputString: String): Long = countStonesAfterBlinking(parseInput(inputString), 25)

        fun task2(inputString: String): Long = countStonesAfterBlinking(parseInput(inputString), 75)

        private fun countStonesAfterBlinking(
            initialStones: List<Long>,
            blinkCount: Int,
        ): Long {
            val stonesMap =
                initialStones
                    .groupingBy { it }
                    .eachCount()
                    .toMap()
                    .mapValues { it.value.toLong() }
            val result =
                (1..blinkCount).fold(stonesMap) { acc, idx ->
                    blink(acc)
                }
            return result.map { it.value }.sum()
        }

        private fun blink(map: Map<Long, Long>): Map<Long, Long> {
            val newMap = mutableMapOf<Long, Long>()
            map.forEach { (key, count) ->
                val keyString = key.toString()
                if (key == 0.toLong()) {
                    newMap.add(1L, count)
                } else if (keyString.length % 2 == 0) {
                    val left = keyString.substring(0, keyString.length / 2).toLong()
                    val right = keyString.substring(keyString.length / 2, keyString.length).toLong()
                    newMap.add(left, count)
                    newMap.add(right, count)
                } else {
                    newMap.add(key * 2024, count)
                }
            }
            return newMap
        }

        private fun parseInput(inputString: String): List<Long> = inputString.split(" ").map { it.toLong() }
    }
}
