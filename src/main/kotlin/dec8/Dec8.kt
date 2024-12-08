package org.example.dec8

import org.example.InputReader

fun main() {
    val inputString = InputReader().readFile("dec7/test.txt")
    println("Result: ${Dec8.task1(inputString)}")
}

class Dec8 {
    companion object {
        fun task1(input: String): Int {
            return 0
        }

        private fun prepareInput(input: String): MapMatrix {
            return MapMatrix(input.split(Regex(("\n"))).map { it.toList() })
        }
    }

    private class MapMatrix(
        val positions: List<List<Char>>,
    )
}