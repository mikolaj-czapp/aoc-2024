package org.example.dec6

import org.example.InputReader

fun main() {
    val inputString = InputReader().readFile("dec6/input.txt")
//    println("Result: ${Dec6.task1(inputString)}")
    println("Result: ${Dec6.task2(inputString)}")
}

class Dec6 {
    companion object {
        fun task1(input: String): Int {
            val map = MapMatrix(input.split(Regex("\n")).map { it.toList() })
            return map.getUniqueGuardPathPositions().size
        }

        fun task2(input: String): Int {
            val map = MapMatrix(input.split(Regex("\n")).map { it.toList() })

            return map.positions
                .flatMapIndexed { rowIndex, row ->
                    row.mapIndexed { colIndex, char ->
                        map.doesObstructionCauseLoop(Pair(rowIndex, colIndex))
                    }
                }.count { it }
        }
    }

    private class MapMatrix(
        val positions: List<List<Char>>,
    ) {
        private val OBSTRUCTION_CHAR = '#'

        private enum class GuardDirection {
            UP {
                override fun rotate() = RIGHT

                override fun vector() = Pair(-1, 0)

                override fun char(): Char = '^'
            },
            RIGHT {
                override fun rotate() = DOWN

                override fun vector() = Pair(0, 1)

                override fun char(): Char = '>'
            },
            DOWN {
                override fun rotate() = LEFT

                override fun vector() = Pair(1, 0)

                override fun char(): Char = 'v'
            },
            LEFT {
                override fun rotate() = UP

                override fun vector() = Pair(0, -1)

                override fun char(): Char = '<'
            }, ;

            abstract fun rotate(): GuardDirection

            abstract fun vector(): Pair<Int, Int>

            abstract fun char(): Char
        }

        init {
            val numberOfRows = positions.size
            positions.forEach { row -> assert(row.size == numberOfRows) }
        }

        fun getUniqueGuardPathPositions(): Set<Pair<Int, Int>> {
            var guard = Guard(findInitialGuardPosition(), GuardDirection.UP)
            val guardUniquePositions = mutableSetOf<Pair<Int, Int>>()
            while (isStepSafe(guard.position, guard.guardDirection.vector())) {
                guardUniquePositions.add(guard.position)
                guard =
                    if (isStepUnobstructed(guard)) {
                        guard.move()
                    } else {
                        guard.rotate().move()
                    }
            }
            guardUniquePositions.add(guard.position)
            return guardUniquePositions
        }

        fun doesObstructionCauseLoop(obstruction: Pair<Int, Int>): Boolean {
            var guard = Guard(findInitialGuardPosition(), GuardDirection.UP)
            val guardUniquePositions = mutableSetOf<Guard>()
            while (isStepSafe(guard.position, guard.guardDirection.vector())) {
                guardUniquePositions.add(guard)
                guard =
                    if (isStepUnobstructed(guard, obstruction)) {
                        guard.move()
                    } else {
                        guard.rotate()
                    }
                if (guardUniquePositions.contains(guard)) {
                    return true
                }
            }
            return false
        }

        private fun findInitialGuardPosition(): Pair<Int, Int> {
            positions.forEachIndexed { i, chars ->
                chars.forEachIndexed { j, char ->
                    if (char == GuardDirection.UP.char()) {
                        return Pair(i, j)
                    }
                }
            }
            throw NoSuchElementException("Cannot find initial guard position")
        }

        private fun isStepSafe(
            position: Pair<Int, Int>,
            direction: Pair<Int, Int>,
        ): Boolean =
            position.first + direction.first >= 0 &&
                position.second + direction.second >= 0 &&
                position.first + direction.first < positions.size &&
                position.second + direction.second < positions.size

        private fun isStepUnobstructed(
            guard: Guard,
            additionalObstruction: Pair<Int, Int>? = null,
        ): Boolean {
            val moved = guard.move().position
            if (additionalObstruction != null) {
                return additionalObstruction != moved && positions[moved.first][moved.second] != OBSTRUCTION_CHAR
            }
            return positions[moved.first][moved.second] != OBSTRUCTION_CHAR
        }

        private data class Guard(
            val position: Pair<Int, Int>,
            val guardDirection: GuardDirection,
        ) {
            fun move() =
                Guard(
                    Pair(
                        position.first + guardDirection.vector().first,
                        position.second + guardDirection.vector().second,
                    ),
                    guardDirection,
                )

            fun rotate() = Guard(position, guardDirection.rotate())
        }
    }
}
