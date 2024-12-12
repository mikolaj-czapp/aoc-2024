package org.example.common

fun Pair<Int, Int>.times(multiplier: Int): Pair<Int, Int> = Pair(this.first * multiplier, this.second * multiplier)

fun Pair<Int, Int>.add(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)

fun List<List<Char>>.at(pair: Pair<Int, Int>): Char = this[pair.first][pair.second]
