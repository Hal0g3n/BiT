package com.halogen.bit

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val charSet = ('A'..'Z').toList()

    @Test
    fun stringToBoolean() {
        for (i in 1..100) {
            val code = (1..6)
                .map { i -> kotlin.random.Random.nextInt(0, charSet.size) }
                .map(charSet::get)
                .joinToString("")
            println(code)
            if (code.matches(Regex("^[A-Z]{6}$"))) continue
            assert(false)
        }
        assert(true)
    }

}