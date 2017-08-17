package com.example.hyunjin.activities

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView

class ShopActivity : AppCompatActivity() {

    private val USER_INFO = "UserInfo"
    private var coins = 0
    internal lateinit var tvRemainingCoins: TextView
    internal lateinit var btnBuyDoubleCoins: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        setActionBar("Clickathon Shop")

        tvRemainingCoins = findViewById(R.id.tvRemainingCoinsNum) as TextView
        btnBuyDoubleCoins = findViewById(R.id.btnUpgradeDouble) as Button

        val itemPrice = Integer.parseInt(btnBuyDoubleCoins.text.toString())
        btnBuyDoubleCoins.setOnClickListener {
            coins -= itemPrice
            tvRemainingCoins.text = coins.toString()
        }

        val prefReader = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        coins = prefReader.getInt("coins", coins)
        tvRemainingCoins.text = coins.toString()
    }

    fun setActionBar(title: String) {
        val actionBar = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(false)
        actionBar.title = title
        actionBar.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}