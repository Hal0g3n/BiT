package com.halogen.bit.model

import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

/**
 * A POJO representing a user
 * @property username String
 * @property password String
 * @property bits Int
 * @property history List of Seconds per day where user focused
 * @property lastUpdated Date when history was last updated
 * @constructor
 */
data class User (
    val username: String = "",
    var password: String = "",
    var history: ArrayList<Int> = arrayListOf(0),
    var lastUpdated: LocalDate = LocalDate.now(),
) {
    var bits: Int = 0
    var id: String = ""

    //Encrypts the password if necessary
    init {
        password = BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun updateHistory() {
        val diff = ChronoUnit.DAYS.between(LocalDate.parse("2021-04-16"), LocalDate.now()).toInt()
        lastUpdated = LocalDate.now()
        for (i in 1..diff) history.add(0)
    }

    fun checkPassword(password: String) = run {
        BCrypt.checkpw(password, this.password)
    }
}