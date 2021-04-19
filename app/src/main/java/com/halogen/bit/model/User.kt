package com.halogen.bit.model

import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDate
import java.time.ZoneId
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
    var username: String = "",
    var password: String = "",
    var history: ArrayList<Int> = arrayListOf(0),
    var lastUpdated: Date = Date(),
    var bits: Int = 0
) {
    var id: String = ""

    //Encrypts the password if necessary
    init {
        password = BCrypt.hashpw(password, BCrypt.gensalt())
    }

    /**
     * Updates History and returns true if data is constant else false
     * @return Boolean
     */
    fun updateHistory(): Boolean {
        val diff = ChronoUnit.DAYS.between(lastUpdated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).toInt()
        lastUpdated = Date()
        for (i in 1..diff) history.add(0)

        return diff != 0
    }

    fun checkPassword(password: String) = run {
        BCrypt.checkpw(password, this.password)
    }
}