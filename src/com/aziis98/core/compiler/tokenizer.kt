package com.aziis98.core.compiler

/**
 * Created by aziis98 on 21/03/2017.
 */

data class SplitRule(val left: (Char) -> Boolean, val right: (Char) -> Boolean)

fun MutableList<SplitRule>.rule(left: (Char) -> Boolean, right: (Char) -> Boolean) {
    this += SplitRule(left, right)
}

fun MutableList<SplitRule>.sameRule(same: (Char) -> Boolean) {
    this += SplitRule(same, same)
}

fun MutableList<SplitRule>.simmetricRule(left: (Char) -> Boolean, right: (Char) -> Boolean) {
    this += SplitRule(left, right)
    this += SplitRule(right, left)
}

class Tokenizer(val rules: List<SplitRule>) {
    companion object {
        fun create(action: MutableList<SplitRule>.() -> Unit): Tokenizer {
            val l = mutableListOf<SplitRule>()
            l.action()
            return Tokenizer(l)
        }
    }

    fun tokenize(source: String): List<String> {
        val result = mutableListOf(source[0].toString())

        fun shouldSplit(a: Char, b: Char): Boolean {
            return rules.any { (lft, rgt) -> lft(a) && rgt(b) }
        }

        for (i in 1 .. source.lastIndex) {
            val lastStr = result.last()

            if (shouldSplit(lastStr.last(), source[i])) {
                result[result.lastIndex] = result.last() + source[i]
            }
            else {
                result.add(source[i].toString())
            }
        }

        return result
    }
}
