package com.ssteam.stevedoing

import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.view.View

class BackRunActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_run)

        showSayDialog()
    }

    fun BacHidRun(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        moveTaskToBack(false)
        startActivity(intent)
    }

    private fun showSayDialog() {
        val normalDialog = AlertDialog.Builder(this)
        normalDialog.setTitle("喵喵喵？")
        normalDialog.setMessage("别看了，这个功能没法用 XD")
        normalDialog.setNegativeButton(
            "知道了"
        ) { dialog, which ->
            finish()
        }
        // 显示
        normalDialog.show()
    }
}