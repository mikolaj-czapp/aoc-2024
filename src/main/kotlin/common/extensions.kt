package org.example.common

fun Pair<Int, Int>.times(multiplier: Int): Pair<Int, Int> = Pair(this.first * multiplier, this.second * multiplier)

fun Pair<Int, Int>.add(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)
fun Pair<Int, Int>.sub(other: Pair<Int, Int>) = Pair(this.first - other.first, this.second - other.second)

fun List<List<Char>>.at(pair: Pair<Int, Int>): Char = this[pair.first][pair.second]

fun <A, B> MutableMap<A, MutableList<B>>.update(
    key: A,
    value: B,
) {
    if (this.containsKey(key)) {
        this[key]!!.add(value)
    } else {
        this[key] = mutableListOf(value)
    }
}

fun <A> Collection<A>.cartesianProduct(): List<Pair<A, A>> =
    this.flatMap { a ->
        this.map { it to a }
    }

fun <A>  List<Pair<A, A>>.removeDiagonal(): List<Pair<A, A>> =
    this.filter { it.first != it.second }
