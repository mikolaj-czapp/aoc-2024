package org.example.common

typealias Position = Pair<Int, Int>
typealias Vector = Pair<Int, Int>

open class SquareMatrix(
    val positions: List<List<Char>>,
) {
    init {
        val numberOfRows = positions.size
        positions.forEach { row -> assert(row.size == numberOfRows) }
    }

    fun isPositionInsideBounds(position: Position): Boolean =
        position.first >= 0 && position.first < positions.size && position.second >= 0 && position.second < positions.size

    fun isMoveInsideBounds(
        position: Position,
        direction: Vector,
    ): Boolean = isPositionInsideBounds(position.add(direction))
}
