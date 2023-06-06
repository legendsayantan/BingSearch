package com.legendsayantan.bingsearch

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val run = findViewById<Button>(R.id.start);
        val stop = findViewById<Button>(R.id.stop);
        val count = findViewById<EditText>(R.id.count);
        val delay = findViewById<EditText>(R.id.delay);
        val timer = Timer()
        var totalcount: Int
        run.setOnClickListener {
            if(count.text.isNullOrEmpty() || delay.text.isNullOrEmpty()) {
                return@setOnClickListener
            }
            totalcount = count.text.toString().toInt()
            timer.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        try {
                            comeToFront()
                            search()
                        }catch (_: Exception) { }
                        runOnUiThread {
                            try {
                                count.setText((--totalcount).toString())
                            }catch (_: Exception) { }
                            if(totalcount <= 0) {
                                this.cancel()
                            }
                        }
                    }
                },0,delay.text.toString().toLong()*1000
            )
        }
        stop.setOnClickListener {
            timer.cancel()
        }
    }
    fun generateRandomString(length: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the characters to choose from
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
    fun search(){
        val url = "https://www.bing.com/search?q="+generateRandomString(10) // The URL you want to open
        val packageName = "com.microsoft.emmx" // Package name of the desired browser app

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage(packageName)
        try {
            println(url)
            startActivity(intent)
        }catch (_: Exception) {
            Toast.makeText(this,"Please install Edge browser",Toast.LENGTH_LONG).show()
        }
    }
    fun comeToFront() {
        val intent = Intent("${applicationContext.packageName}.actions.recycle")
        sendBroadcast(intent)
    }
}