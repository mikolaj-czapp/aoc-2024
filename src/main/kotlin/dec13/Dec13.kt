package org.example.dec13

import org.example.InputReader

fun main() {
    val inputString = InputReader().readFile("dec13/input.txt")
//    println("Result: ${Dec13.task1(inputString)}")
    println("Result: ${Dec13.task2(inputString)}")
}

class Dec13 {
    companion object {
        fun task1(string: String): Int {
            val equationSystems = parseInput(string)
            return equationSystems
                .filter { isFloatInt(it.a) && isFloatInt(it.b) }
                .filter {
                    it.a.toInt() in 0..100 && it.b.toInt() in 0..100
                }.sumOf {
                    it.a.toInt() * 3 + it.b.toInt()
                }
        }

        fun task2(string: String): Long {
            val equationSystems = parseInput(string, offset = 10000000000000)
            return equationSystems
                .filter { isFloatInt(it.a) && isFloatInt(it.b) }
                .filter {
                    it.a.toLong() >= 0L && it.b.toLong() >= 0L
                }.sumOf {
                    it.a.toLong() * 3 + it.b.toLong()
                }
        }

        private fun parseInput(
            string: String,
            offset: Long = 0,
        ): Set<EquationSystem> {
            val setsRaw = string.split(Regex("\n\n"))
            return setsRaw.map { parseRawSet(it, offset) }.toSet()
        }

        private fun parseRawSet(
            string: String,
            offset: Long = 0,
        ): EquationSystem {
            val data = string.split(Regex("\n"))
            val buttonA = parseButton(data[0])
            val buttonB = parseButton(data[1])
            val prize = parsePrize(data[2])
            return EquationSystem(
                buttonA.first,
                buttonA.second,
                buttonB.first,
                buttonB.second,
                prize.first + offset,
                prize.second + offset,
            )
        }

        private fun parseButton(string: String): Pair<Long, Long> {
            val matches = Regex("[X,Y]\\+(\\d+)").findAll(string)
            return matches.first().groupValues[1].toLong() to matches.last().groupValues[1].toLong()
        }

        private fun parsePrize(string: String): Pair<Long, Long> {
            val matches = Regex("[X,Y]=(\\d+)").findAll(string)
            return matches.first().groupValues[1].toLong() to matches.last().groupValues[1].toLong()
        }

        private fun isFloatInt(value: Float): Boolean = value == value.toLong().toFloat()

        private data class EquationSystem(
            val ax: Long,
            val ay: Long,
            val bx: Long,
            val by: Long,
            val prizeX: Long,
            val prizeY: Long,
        ) {
            val determinant
                get() = ax * by - ay * bx

            val determinantX
                get() = prizeX * by - prizeY * bx

            val determinantY
                get() = ax * prizeY - ay * prizeX

            val a
                get() = determinantX.toFloat() / determinant.toFloat()

            val b
                get() = determinantY.toFloat() / determinant.toFloat()
        }
    }
}
