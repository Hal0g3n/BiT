package com.halogen.bit.ui.shop

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.halogen.bit.model.Auction
import java.util.*
import kotlin.collections.ArrayList

class ShopViewModel: ViewModel() {

    private val db = Firebase.firestore
    val storage = FirebaseStorage.getInstance()

    val auctions = ArrayList<Auction>()

    init {
        //Today Date
        val today = Date()

        //Query for active auctions
        db.collection("auctions").whereGreaterThanOrEqualTo("startingDate", today).whereLessThan("closingDate", today).get().addOnSuccessListener {
            for (doc in it) { auctions.add(doc.toObject()) }
        }
    }

}