package org.example.common

open class SquareMatrix(
    val positions: List<List<Char>>,
) {
    init {
        val numberOfRows = positions.size
        positions.forEach { row -> assert(row.size == numberOfRows) }
    }

    fun isMoveInsideBounds(
        position: Pair<Int, Int>,
        direction: Pair<Int, Int>,
    ): Boolean {
        val result = position.add(direction)
        return result.first >= 0 && result.first < positions.size && result.second >= 0 && result.second < positions.size
    }
}
