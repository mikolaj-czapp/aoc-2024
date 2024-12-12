package org.example.common

enum class Direction {
    UP {
        override fun vector() = Pair(-1, 0)
    },
    RIGHT {
        override fun vector() = Pair(0, 1)
    },
    DOWN {
        override fun vector() = Pair(1, 0)
    },
    LEFT {
        override fun vector() = Pair(0, -1)
    }, ;

    abstract fun vector(): Pair<Int, Int>
}
