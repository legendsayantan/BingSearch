package com.legendsayantan.bingsearch

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doOnTextChanged
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(
            "bing",
            MODE_PRIVATE
        )
    }
    lateinit var count: EditText
    lateinit var delay: EditText
    lateinit var button: Button
    lateinit var github: TextView
    var searching = false
    private var timer = Timer()
    var searchProgress = 0
    val updateUi = {
        runOnUiThread {
            button.text = if (searching) "Stop" else "Start Search"
            button.backgroundTintList =
                if (searching) getColorStateList(R.color.red) else getColorStateList(R.color.green)
            button.keepScreenOn = searching
            findViewById<TextView>(R.id.output).text =
                if(isInMultiWindowMode) if(searching) "Starting..." else "Not Searching." else "Start Split-Screen to begin"
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        count = findViewById(R.id.count)
        delay = findViewById(R.id.delay)
        count.setText(sharedPreferences.getInt("count", 20).toString())
        count.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrBlank() && text.isDigitsOnly()) sharedPreferences.edit()
                .putInt("count", text.toString().toInt()).apply()
        }
        delay.setText(sharedPreferences.getInt("delay", 5).toString())
        delay.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrBlank() && text.isDigitsOnly()) sharedPreferences.edit()
                .putInt("delay", text.toString().toInt()).apply()
        }

        if (isInMultiWindowMode) {
            updateUi()
            if (sharedPreferences.getBoolean("autoStart", false)) startSearch()
            button.setOnClickListener {
                if (searching) stopSearch()
                else startSearch()
                updateUi()
            }
        } else {
            button.text = "Auto Start"
            button.backgroundTintList = if (sharedPreferences.getBoolean(
                    "autoStart",
                    false
                )
            ) getColorStateList(R.color.green) else getColorStateList(R.color.red)
            button.setOnClickListener {
                if (sharedPreferences.getBoolean("autoStart", false)) {
                    sharedPreferences.edit().putBoolean("autoStart", false).apply()
                    button.backgroundTintList = getColorStateList(R.color.red)
                    Toast.makeText(this, "Auto Start Disabled", Toast.LENGTH_SHORT).show()
                } else {
                    sharedPreferences.edit().putBoolean("autoStart", true).apply()
                    button.backgroundTintList = getColorStateList(R.color.green)
                    Toast.makeText(this, "Auto Start Enabled", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setSearchProgress = {
            runOnUiThread {
                findViewById<TextView>(R.id.output).text = "Completed : $it"
            }
        }
        github = findViewById(R.id.github)
        github.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/legendsayantan/BingSearch")
            )
            startActivity(intent)
        }
    }

    override fun onPause() {
        if(searching && !isInMultiWindowMode) {
            stopSearch()
        }
        super.onPause()
    }

    override fun onResume() {
        if(searching && !isInMultiWindowMode) {
            stopSearch()
        }
        super.onResume()
    }
    fun startSearch() {
        if (count.text.isNullOrEmpty() || delay.text.isNullOrEmpty() || !startEdge()) {
            return
        }
        searching = true
        updateUi()
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (searchProgress >= sharedPreferences.getInt("count", 20)) {
                    stopSearch()
                    setSearchProgress(sharedPreferences.getInt("count", 20))
                    return
                }
                search()
                searchProgress++
                setSearchProgress(searchProgress)
            }
        }, 5000, sharedPreferences.getInt("delay", 5) * 1000L)
    }

    fun stopSearch() {
        timer.cancel()
        searchProgress = 0
        searching = false
        updateUi()
    }

    private fun startEdge(): Boolean {
        val intent = Intent()
        intent.component = ComponentName(getString(R.string.edge), getString(R.string.edgeMain))
        intent.flags = Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK
        return try {
            startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread { findViewById<TextView>(R.id.output).text = "Install Microsoft Edge." }
            false
        }
    }
    fun generateRandomString(length: Int): String {
        val charPool: List<Char> =
            ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the characters to choose from
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    fun search() {
        val url =
            "https://www.bing.com/search?q=" + generateRandomString(10) // The URL you want to open
        val packageName = getString(R.string.edge) // Package name of the desired browser app

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage(packageName)
        intent.flags = Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        var setSearchProgress: (Int) -> Unit = {}
    }
}