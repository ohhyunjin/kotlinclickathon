package com.example.hyunjin.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.database.FirebaseDatabase

import utils.ButtonManager

open class MainActivity : AppCompatActivity() {

    private val USER_INFO = "UserInfo"
    private var nickname: String? = null
    private var score = 0
    private var coins = 0
    private var time = 9
    private var highscore = 0
    private var tapIsClicked = false
    private var buttonManager: ButtonManager? = null
    private var firebaseEngine: FirebaseEngine? = null
    private var view: View? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemDeleteData -> {
                deleteUserData()
                view!!.invalidate()
            }
            R.id.setNickname -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Set Nickname")

                //Setup input
                val input = EditText(this)

                //Specify the type of input expected
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)

                //Setup buttons
                builder.setPositiveButton("OK") { dialogInterface, i ->
                    nickname = input.text.toString()
                    val editor = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit()
                    editor.putString("nickname", nickname)
                    editor.apply()
                }

                builder.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.cancel() }

                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.generalmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view = findViewById(R.id.mainLayout)

        val btnTap = findViewById(R.id.btnClick) as Button
        val tvScore = findViewById(R.id.tvScoreNum) as TextView
        val tvTime = findViewById(R.id.tvTimeNum) as TextView
        val tvCoins = findViewById(R.id.tvCoinsNum) as TextView
        val tvHighscore = findViewById(R.id.tvHighScoreNum) as TextView
        val btnReset = findViewById(R.id.btnReset) as Button
        val btnShop = findViewById(R.id.btnShop) as Button

        buttonManager = ButtonManager()
        firebaseEngine = FirebaseEngine()

        //Enable Firebase disk persistence in order to store data during offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        val prefReader = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        coins = prefReader.getInt("coins", coins)
        tvCoins.text = coins.toString()

        val prefReaderHighScore = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        highscore = prefReaderHighScore.getInt("highscore", highscore)
        tvHighscore.text = highscore.toString()

        val nicknameReader = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        nickname = nicknameReader.getString("nickname", nickname)

        tvTime.text = "10"

        checkUpgrades()

        val countDown = object : CountDownTimer(10000, 1000) {
            override fun onTick(l: Long) {
                tvTime.text = time.toString()
                time--
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Game over", Toast.LENGTH_SHORT).show()
                buttonManager!!.toggleButton(btnTap, false)
                buttonManager!!.toggleButton(btnReset, true)
                coins += score
                tvCoins.text = coins.toString()
                if (score > highscore) {
                    highscore = score
                    tvHighscore.text = highscore.toString()
                }

                val editor = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit()
                editor.putInt("coins", coins)
                editor.apply()

                var nick: String = nickname.toString()
                //Store nickname, coins and highscore to Firebase
                firebaseEngine!!.firebaseWriteUser(
                        nick,
                        coins,
                        highscore
                )


                val editorHighscore = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit()
                editorHighscore.putInt("highscore", highscore)
                editorHighscore.apply()
            }
        }


        btnTap.setOnClickListener(View.OnClickListener {
            btnTap.text = "Keep tapping!"
            score++
            val stringScore = score.toString()
            tvScore.text = stringScore
            if (tapIsClicked) {
                return@OnClickListener
            }
            countDown.start()
            tapIsClicked = true
            buttonManager!!.toggleButton(btnReset, false)
        })

        btnReset.setOnClickListener {
            score = 0
            time = 9
            tvScore.text = "0"
            tvTime.text = "10"
            btnTap.text = "Tap me to start"
            buttonManager!!.toggleButton(btnTap, true)
            buttonManager!!.toggleButton(btnReset, false)
            tapIsClicked = false
        }

        btnShop.setOnClickListener {
            val intent = Intent(this@MainActivity, ShopActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUpgrades() {

    }

    private fun deleteUserData() {
        val editorHighscore = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit()
        editorHighscore.clear()
        editorHighscore.commit()
        Toast.makeText(this@MainActivity, "User data deleted", Toast.LENGTH_SHORT).show()
    }
}