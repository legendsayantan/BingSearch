package com.legendsayantan.bingsearch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val run = findViewById<Button>(R.id.start);
        val stop = findViewById<Button>(R.id.stop);
        val count = findViewById<EditText>(R.id.count);
        val delay = findViewById<EditText>(R.id.delay);
        run.setOnClickListener {
            if(count.text.isNullOrEmpty() || delay.text.isNullOrEmpty()) {
                return@setOnClickListener
            }
            SearchService.count = count.text.toString().toInt()
            SearchService.delay = delay.text.toString().toInt()
            c = count.text.toString().toInt()
            d = delay.text.toString().toInt()
            print(SearchService.count)
            startService(Intent(applicationContext,SearchService::class.java))

        }
        stop.setOnClickListener {
            stopService(Intent(applicationContext,SearchService::class.java))
        }
        setOutput = {
            runOnUiThread {
                findViewById<TextView>(R.id.output).text = "Completed : $it"
            }
        }
        intent.extras?.let {
            val completed = it.getInt("completed")
            setOutput(completed)
        }
    }
    companion object{
        var c = 1
        var d = 1
        var setOutput : (Int) -> Unit = {}
    }
}