package org.example.dec1.task1

import org.example.InputReader
import kotlin.math.abs

fun main() {
    val inputString = InputReader().readFile("dec1/task1/input1.txt")
    val listOfRows = inputString.split(Regex("\\n+")).filterNot { it.isEmpty() }
    val pairOfListsSorted =
        listOfRows
            .map {
                it.split(Regex("\\s+"))
            }.map {
                it[0].toInt() to it[1].toInt()
            }.unzip()
            .let {
                it.first.sorted() to it.second.sorted()
            }
    println(distance(pairOfListsSorted))
    println(similarityScore(pairOfListsSorted))
}

fun similarityScore(pairOfLists: Pair<List<Int>, List<Int>>): Int =
    pairOfLists.first.sumOf { id ->
        id * pairOfLists.second.count { it == id }
    }

fun distance(pairOfLists: Pair<List<Int>, List<Int>>): Int = (pairOfLists.first zip pairOfLists.second).sumOf { abs(it.first - it.second) }
