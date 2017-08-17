package com.example.hyunjin.activities

/**
 * Created by HyunJin on 8/2/2017.
 */

internal class User {
    lateinit var username: String
    var coins: Int = 0
    var highscore: Int = 0

    private constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(username: String, coins: Int, highscore: Int) {
        this.username = username
        this.coins = coins
        this.highscore = highscore
    }
}
