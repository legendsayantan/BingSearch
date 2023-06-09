package com.legendsayantan.bingsearch

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random


class SearchService : Service() {
    private val timer by lazy { Timer() }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        count = MainActivity.c
        delay = MainActivity.d
        val total = count
        if(startEdge())
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    if(count <= 0) {
                        this.cancel()
                        stopSelf()
                        comeOnTop(total - count)
                        return
                    }
                    count--
                    comeOnTop(total - count)
                    MainActivity.setOutput(total - count)
                    search()
                }
            },5000L,delay*1000L
        )
    }
    fun generateRandomString(length: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the characters to choose from
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
    fun comeOnTop(completed:Int){
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("completed",completed)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun startEdge(): Boolean{
        val intent = Intent()
        intent.component = ComponentName(getString(R.string.edge), getString(R.string.edgeMain))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return try {
            startActivity(intent)
            true
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(applicationContext,"Install Microsoft Edge",Toast.LENGTH_LONG).show()
            false
        }
    }
    fun search(){
        val url = "https://www.bing.com/search?q="+generateRandomString(10) // The URL you want to open
        val packageName = getString(R.string.edge) // Package name of the desired browser app

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage(packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        try {
            startActivity(intent)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    companion object{
        var count = 1
        var delay = 1
    }
}