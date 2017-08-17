package com.example.hyunjin.activities

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by HyunJin on 8/2/2017.
 */

internal class FirebaseEngine : MainActivity() {

    //To read or write data from the database(Firebase), we need and instance of DatabaseReference
    private var mDatabase: DatabaseReference? = null

    fun firebaseGetReference() {
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    fun firebaseWriteUser(username: String, coins: Int, highscore: Int) {
        firebaseGetReference()
        val user = User(username, coins, highscore)
        mDatabase!!.child("users").child(username).setValue(user)
    }
}
