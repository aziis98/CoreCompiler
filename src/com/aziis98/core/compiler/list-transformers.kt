package com.aziis98.core.compiler

/**
 * Created by aziis98 on 21/03/2017.
 */

abstract class ListCondenser<T> {

    data class MatchInterval(val startInclusive: Int, val endInclusive: Int = startInclusive) {
        fun isEmpty() = startInclusive == endInclusive
        val indices
            get() = startInclusive .. endInclusive
    }

    fun condenseRight(list: List<T>): List<T> {
        val l = mutableListOf<T>()
        l += list

        main@ while (true) {
//            println("l: $l")
            for ((i, value) in l.withIndex()) {
                val match = match(value, i, l)

                if (!match.isEmpty()) {

                    val values = match.indices.map { l[it] }

                    repeat(match.endInclusive - match.startInclusive) {
                        l.removeAt(match.startInclusive)
                    }

                    l[match.startInclusive] = merge(values)

                    continue@main
                }
            }
            return  l
        }
    }

    fun condenseLeft(list: List<T>): List<T> {
        val l = mutableListOf<T>()
        l += list.asReversed()

        main@ while (true) {
//            println("l: $l")
            for ((i, value) in l.withIndex()) {
                val match = match(value, i, l)

                if (!match.isEmpty()) {

                    val values = match.indices.map { l[it] }.asReversed()

                    repeat(match.endInclusive - match.startInclusive) {
                        l.removeAt(match.startInclusive)
                    }

                    l[match.startInclusive] = merge(values)

                    continue@main
                }
            }
            return  l
        }
    }


    protected abstract fun match(value: T, index: Int, source: List<T>): MatchInterval

    protected abstract fun merge(values: List<T>): T

}

abstract class ListStartEndCondenser<T> {

    protected abstract fun matchStart(value: T): Boolean
    protected abstract fun matchEnd(value: T): Boolean

    protected abstract fun merge(list: List<T>): T

    fun group(list: List<T>): List<T> {

        val l = mutableListOf<T>()

        var startIndex = -1
        var insideGroup = false

        list.forEachIndexed { i, value ->
            if (!insideGroup) {
                if (matchStart(value)) {
                    insideGroup = true
                    startIndex = i
                }
                else {
                    l += value
                }
            }
            else {
                if (matchEnd(value)) {
                    l += merge(list.subList(startIndex, i + 1))

                    insideGroup = false
                    startIndex = -1
                }
            }
        }

        assert(!insideGroup) { "Unclosed group" }

        return l
    }

}