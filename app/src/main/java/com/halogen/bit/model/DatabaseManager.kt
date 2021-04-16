package com.halogen.bit.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

/**
 * This is the Database Manager
 * As expected it manages the local and online database
 * @author Halogen
 */
class DatabaseManager(
    app: Application
) : AndroidViewModel(app) {

    //Online database instance for getting information
    private val db = Firebase.firestore

    //For use by ViewModel only
    private val mUser = MutableLiveData<User?>(null)

    /**
     * Is null if there is no user logged in
     * Else it will contain the user
     */
    val user = mUser as LiveData<User?>

    val plans = ArrayList<Plan>()

    /**
     * Function to get user from the online database
     * @param username String
     * @throws IllegalArgumentException if user does not exist
     */
    suspend fun getUser(username: String): User? = run {
        var user: User? = null

        db.collection("users").whereEqualTo("username", username).get().addOnSuccessListener { query ->
            if (query.size() != 1) return@addOnSuccessListener
            user = query.documents[0].toObject<User>()
            user?.id = query.documents[0].id
        }.await()

        return user
    }

    /**
     * Function to login the user
     * @param username String
     * @param password String
     * @param callback Given success of login
     * @return Boolean
     */
    fun login(username: String, password: String, callback: (Boolean) -> Unit) = GlobalScope.launch {

        try {
            //Get the user
            val user = getUser(username)!!

            //If password wrong
            if (!user.checkPassword(password)) callback.invoke(false)
            else { //Password is correct, login user

                //Load the user's plans
                db.collection("users/${user.id}/plans").get()
                        .addOnSuccessListener {
                            for (doc in it) {
                                plans.add(doc.toObject())
                            }
                        }.await() //Await so that the plans will all be loaded

                //Post new User as current User
                mUser.postValue(user)

                //Successfully logged in
                callback.invoke(true)
            }
        }
        //On User non-existent
        catch (e: Exception) { callback.invoke(false) }

    }

    /**
     * Function to login the user
     * @param username String
     * @param password String
     * @param callback Given success of login
     * @return Boolean
     */
    fun register(username: String, password: String, callback: (Boolean) -> Unit) = GlobalScope.launch {
        try {
            //Create New User
            val nUser = User(
                username,
                password,
                0
            )

            //Creating document associated with user
            nUser.id = db.collection("users").document().id
            db.collection("users").document(nUser.id).set(nUser).await()

            //Post new User as current User
            mUser.postValue(nUser)

            //Successfully run
            callback.invoke(true)
        }

        //In case of errors
        catch (e: Exception) { callback.invoke(false) }

    }
}

/* Deprecated code {
    /**
     * Retrieves all information associated with the import code
     * The import code is then deleted from existence as everything is stored here
     * For conflicts due to same name, the imported one is unused
     * @param code String
     * @param callback A callback for when the import is done
     */
    fun import(code: String, callback: (Boolean) -> Unit) {

        //Reference to the code
        val ref = db.collection("codes").document("code")

        //Reference to all the plans
        ref.collection("plans").get().addOnSuccessListener {
            for (plan in it) plans.add(plan.toObject())
            callback.invoke(true)
        }.addOnFailureListener { callback.invoke(false) }

    }

    /**
     * Helper function for deleting everything in a document.
     * This includes sub-collections
     * @param path String
     */
    private fun deleteAtPath(path: String) {
        val deleteFn = Firebase.function
        deleteFn.call(hashMapOf("path" to path))
            .addOnSuccessListener {}
            .addOnFailureListener {}
    }

    //Capital Letters only
    private val charSet = ('A'..'Z').toList()

    /**
     * This function is called to export the data online
     * It creates 5 random codes and tries to export
     * Only exports plans and not history
     * Returns null if unsuccessful
     * @return String - The import code for importing the exported data
     */
    suspend fun export() = run {

        //Attempts to create code 5 times
        for (i in 1..5) {
            //Generate random 6 letter code
            val code = (1..6)
                .map { kotlin.random.Random.nextInt(0, charSet.size) }
                .map(charSet::get)
                .joinToString("")

            //Validate the code
            if (!validateCode(code)) continue

            val ref = db.collection("codes").document(code)

            //Runs a transaction to "chope" a code
            val result = db.runTransaction {
                //If code already exists, exit with failure
                if (it.get(ref).exists()) return@runTransaction false

                //Code available, create doc to "chope" the code for usage
                it.set(ref, "exists" to true)
                //exit with success
                true
            }.await()

            //Check if code successfully "chope"
            if (!result) continue

            //Writing all plans to the internet
            plans.forEach {
                //Going to the collection, create document, add plans
                db.collection("codes/$code/plans").document().set(it)
            }

            //Returns the code generated
            return@run code
        }

        //Did not manage to create code
        null
    }

    private fun validateCode(code: String) = code.matches(Regex("^[A-Z]{6}$"))

} */