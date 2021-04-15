package com.halogen.bit.model

import org.mindrot.jbcrypt.BCrypt

/**
 * A POJO representing a user
 * @property username String
 * @property password String
 * @property bits Int
 * @constructor
 */
data class User (
    val username: String = "",
    var password: String = "",
    var bits: Int = 0,
) {
    var id: String = ""

    //Encrypts the password if necessary
    init {
        password = BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun checkPassword(password: String) = run {
        BCrypt.checkpw(password, this.password)
    }
}