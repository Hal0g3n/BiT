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
    val password: String = "",
    var bits: Int = 0
) {

    //Encrypts the password if necessary
    init {
        BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun checkPassword(password: String) = run {
        BCrypt.checkpw(password, this.password)
    }
}