package org.example.dec12

import org.example.InputReader
import org.example.common.Direction
import org.example.common.Position
import org.example.common.SquareMatrix
import org.example.common.add
import org.example.common.at
import org.example.common.isAdjacentTo

fun main() {
    val inputString = InputReader().readFile("dec12/input.txt")
    println("Result: ${Dec12.task1(inputString)}")
}

class Dec12 {
    companion object {
        fun task1(string: String): Int {
            val garden = prepareInput(string)
            val regions = findRegions(garden)
            return regions.sumOf { it.getFencePrice(garden) }
        }

        private fun prepareInput(input: String): SquareMatrix<Char> = SquareMatrix(input.split(Regex(("\n"))).map { it.toList() })

        private fun findRegions(garden: SquareMatrix<Char>): List<Region> {
            val visitedPositions = mutableSetOf<Position>()
            return garden.positions
                .flatMapIndexed { index, row ->
                    row.mapIndexed { idx, c ->
                        Region(identifyRegion(garden, visitedPositions, Position(index, idx), setOf()), c)
                    }
                }.filter { it.plots.isNotEmpty() }
        }

        private fun identifyRegion(
            garden: SquareMatrix<Char>,
            visitedPositions: MutableSet<Position>,
            currentPosition: Position,
            currentRegion: Set<Position>,
        ): Set<Position> {
            if (currentPosition in visitedPositions) {
                return currentRegion
            }
            visitedPositions.add(currentPosition)
            return Direction.entries
                .filter {
                    garden.isPositionInsideBounds(currentPosition.add(it.vector()))
                }.filter {
                    isNeighbourWithinTheSameRegion(garden, currentPosition, currentPosition.add(it.vector()))
                }.flatMap {
                    identifyRegion(
                        garden,
                        visitedPositions,
                        currentPosition.add(it.vector()),
                        currentRegion + currentPosition,
                    )
                }.toSet()
        }

        private fun isNeighbourWithinTheSameRegion(
            map: SquareMatrix<Char>,
            initialPosition: Position,
            destinationPosition: Position,
        ) = destinationPosition.isAdjacentTo(initialPosition) &&
            map.positions.at(destinationPosition) ==
            map.positions.at(
                initialPosition,
            )

        private data class Region(
            val plots: Set<Position>,
            val plant: Char,
        ) {
            val area
                get() = plots.size

            private fun calculatePerimeter(garden: SquareMatrix<Char>): Int =
                plots.sumOf { plot ->
                    Direction.entries
                        .filter {
                            garden.isPositionInsideBounds(plot.add(it.vector()))
                        }.filterNot {
                            isNeighbourWithinTheSameRegion(garden, plot, plot.add(it.vector()))
                        }.count() +
                        Direction.entries
                            .count {
                                !garden.isPositionInsideBounds(plot.add(it.vector()))
                            }
                }

            fun getFencePrice(garden: SquareMatrix<Char>) = area * calculatePerimeter(garden)
        }
    }
}
