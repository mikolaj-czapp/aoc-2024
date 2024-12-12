package org.example.common

fun Pair<Int, Int>.times(multiplier: Int): Pair<Int, Int> = Pair(this.first * multiplier, this.second * multiplier)

fun Pair<Int, Int>.add(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)

fun Pair<Int, Int>.sub(other: Pair<Int, Int>) = Pair(this.first - other.first, this.second - other.second)

fun Position.isAdjacentTo(other: Position): Boolean =
    Direction.entries.any {
        this.add(it.vector()) == other
    }

fun <T> List<List<T>>.at(pair: Pair<Int, Int>): T = this[pair.first][pair.second]

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

fun MutableMap<Long, Long>.add(
    key: Long,
    value: Long,
) {
    if (this.containsKey(key)) {
        this[key] = this[key]!!.plus(value)
    } else {
        this[key] = value
    }
}

fun <A> Collection<A>.cartesianProduct(): List<Pair<A, A>> =
    this.flatMap { a ->
        this.map { it to a }
    }

fun <T, U> cartesianProduct(
    collection1: Collection<T>,
    collection2: Collection<U>,
): List<Pair<T, U>> =
    collection1.flatMap { item1 ->
        collection2.map { item2 ->
            Pair(item1, item2)
        }
    }

fun <A> List<Pair<A, A>>.removeDiagonal(): List<Pair<A, A>> = this.filter { it.first != it.second }
