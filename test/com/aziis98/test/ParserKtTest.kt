package com.aziis98.test

import com.aziis98.core.compiler.ListCondenser
import com.aziis98.core.compiler.ListStartEndCondenser
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by aziis98 on 21/03/2017.
 */

class ParserKtTest {

    class StringCondenser : ListStartEndCondenser<String>() {
        override fun matchStart(value: String): Boolean {
            return value == "\""
        }

        override fun matchEnd(value: String) = matchStart(value)

        override fun merge(list: List<String>): String {
            return list.joinToString("")
        }
    }

    class Condenser1(val op: String, val merge: (Int, Int) -> Int) : ListCondenser<String>() {
        override fun match(value: String, index: Int, source: List<String>): MatchInterval {
            if (value == op)
                return MatchInterval(index - 1, index + 1)
            else
                return MatchInterval(index)
        }

        override fun merge(values: List<String>): String {
//            println(values)
            return merge(values[0].toInt(), values[2].toInt()).toString()
        }
    }

    @Test
    fun testCondenser() {

        val condenser1 = Condenser1("+", { a, b -> a + b })
        val l1 = listOf("1", "+", "2", "+", "3")

        assertEquals(listOf("6"), condenser1.condenseRight(l1))


        val condenser2 = Condenser1("^", { a, b -> Math.pow(a.toDouble(), b.toDouble()).toInt() })
        val l2 = listOf("2", "^", "1", "^", "3")

        assertEquals(listOf("2"), condenser2.condenseLeft(l2))
    }

    @Test
    fun testStartEndCondenser() {
        val l1 = listOf("Ciao, io ", "sono", " ", "\"", "Antonio De", " ", "Lucreziis", "\"", " ed ho", " ", "18 ", "\"", "anni", "\"")
        val stringCondenser = StringCondenser()

        println(stringCondenser.group(l1))
    }
}