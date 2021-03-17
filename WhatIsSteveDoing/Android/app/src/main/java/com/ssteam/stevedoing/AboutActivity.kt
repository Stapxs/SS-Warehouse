package com.ssteam.stevedoing

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.FileWriter
import java.io.IOException
import java.net.*


class AboutActivity : AppCompatActivity() {

    lateinit var message: String
    private lateinit var mainActivity: AboutActivity
    private var note = "没有公告……"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        mainActivity = this
        message = intent.getStringExtra(EXTRA_MESSAGE).toString()
        if(message != "ERR") {
            val button: Button = this.findViewById(R.id.likeButton)
            button.foregroundTintList = resources.getColorStateList(R.color.colorLove)
        } else {
            showSayDialog()
        }

        Thread(getNote).start()
    }

    fun openSSzhp(view: View) {
        val uri: Uri = Uri.parse("https://stapx.chuhelan.com/Doing.html")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun showLog(view: View) {
        shownoteDialog()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getALove(view: View) {
        if(message == "ERR") {
            val button: Button = this.findViewById(R.id.likeButton)
            button.foregroundTintList = resources.getColorStateList(R.color.colorLove)
            Toast.makeText(this, "谢谢喜欢！", Toast.LENGTH_SHORT).show()
            Thread(addLove).start()
            val fileDir = filesDir
            val setFile = "${fileDir.path}/WISD.ini"
            try {
                val fw = FileWriter(setFile, true)
                fw.write("love:true")
                fw.close()
            } catch (e: IOException) {}
        }
    }

    private var addLove = Runnable {
        try {
            val back = URL("https://stapx.chuhelan.com/api/SS-Doing/Love/?type=AD").readText()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private var getNote = Runnable {
        println(">>> 获取公告")
        try {
            val back = URL("https://stapx.chuhelan.com/api/SS-Doing/Note.txt").readText()
            if (!back.isBlank()) {
                note = back
                println("获取完成")
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    // 对话框
    private fun shownoteDialog() {
        val normalDialog = AlertDialog.Builder(this@AboutActivity)
        normalDialog.setMessage("\n$note")
        normalDialog.setNegativeButton(
            "知道了"
        ) { dialog, which ->
            //...To-do
        }
        // 显示
        normalDialog.show()
    }

    private fun showSayDialog() {
        val normalDialog = AlertDialog.Builder(this@AboutActivity)
        normalDialog.setTitle("喵喵喵？")
        normalDialog.setMessage("这是 SS 第一次写安卓程序，难免会有各种稀奇古怪的乱七八糟的不明所以的巴拉巴拉的小问题，都可以戳“关于作者”去反馈啦 XD")
        normalDialog.setNegativeButton(
            "知道了"
        ) { dialog, which ->
            //...To-do
        }
        // 显示
        normalDialog.show()
    }
}