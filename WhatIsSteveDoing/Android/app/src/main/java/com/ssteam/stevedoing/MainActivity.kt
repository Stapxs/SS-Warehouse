package com.ssteam.stevedoing

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.AlertDialog
import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.*
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.net.URL


class MainActivity : AppCompatActivity() {

    class SetVer(name: String, value: String) {
        var name = name
        var value = value
    }

    private lateinit var mainContext: Context
    private lateinit var mainActivity: MainActivity
    public var sets: MutableList<SetVer> = mutableListOf()
    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainContext = this
        mainActivity = this

        // Log 框
        val textView: TextView = this.findViewById(R.id.logs)
        textView.text = "${textView.text}\n\n>>> 正在初始化……"

        // 申请权限
        textView.text = "${textView.text}\n>> 请求 PACKAGE_USAGE_STATS 权限："
        if (check(this)) {
            Toast.makeText(this, "权限正常", Toast.LENGTH_SHORT).show()
            textView.text = "${textView.text}成功"
        } else {
            textView.text = "${textView.text}失败\n正在引导打开设置界面……"
            Toast.makeText(this, "请允许获取应用使用情况", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            this.startActivity(intent)
        }

        val packagename = getTopAppPackageName(this)
        if(!packagename.isNullOrBlank()) {
            textView.text = "${textView.text}\n>> 获取前台应用：成功 - $packagename"
        } else {
            textView.text = "${textView.text}\n>> 获取前台应用：失败"
        }

        // 读取设置
        val fileDir = filesDir
        textView.text = "${textView.text}\n>> 读取设置：$fileDir"
        val setFile = File("${fileDir.path}/WISD.ini")
        if(!setFile.exists()) {
            setFile.createNewFile()
            textView.text = "${textView.text}\n> 设置文件不存在，已新建。"
        } else {
            textView.text = "${textView.text}\n> 设置项……"
            val optFileList: List<String> = setFile.readLines()
            for(set in optFileList) {
                val name = set.substring(0, set.indexOf(":"))
                val value = set.substring(set.indexOf(":") + 1)
                sets.add(SetVer(name, value))
                textView.text = "${textView.text}\n\t$name --> $value"
            }
        }

        // 初始化控件
        if(getSet("name") != "ERR") {
            val editText: EditText = this.findViewById(R.id.inName)
            editText.setText(getSet("name"))
        }
        if(getSet("group") != "ERR") {
            val editText: EditText = this.findViewById(R.id.inGroup)
            editText.setText(getSet("group"))
        }
        if(getSet("qq") != "ERR") {
            val editText: EditText = this.findViewById(R.id.inQQ)
            editText.setText(getSet("qq"))
        }
        if(getSet("hid") != "ERR") {
            val text: TextView = this.findViewById(R.id.hidList)
            val list = getSet("hid").split(",")
            for (str in list) {
                text.text = "${text.text}$str\n"
            }
        }

        // 开始循环
        textView.text = "${textView.text}\n\n>>> 正在启动主循环……"
        Thread(test).start()
    }

    private var test = Runnable {

        val msg = Message()
        msg.what = 1
        msg.obj = ">> 启动完成"
        handler.sendMessage(msg)

        var getNull: Boolean
        var lastname: String
        var packageName = "android"
        var passTimes = 0
        while (!exit) {

            // 获取前台应用名
            lastname = packageName
            packageName = getTopAppPackageName(mainContext).toString()
            getNull = packageName.isBlank()
            if(getNull) {
                packageName = lastname
            }

            // 发送 LOG
            val msgsend = Message()
            msgsend.what = 1
            if(getNull)
            {
                msgsend.obj = ">>> 获取前台应用失效，使用上次数据：$packageName"
            } else {
                msgsend.obj = ">>> 获取前台应用：$packageName"
            }
            handler.sendMessage(msgsend)

            // 判断基础设置
            if(getSet("name") == "ERR" ||
                getSet("group") == "ERR") {
                val msg4 = Message()
                msg4.what = 1
                msg4.obj = ">>> 错误：用户名或分类组不存在，请设置并保存！"
                handler.sendMessage(msg4)
                // 延迟
                try {
                    Thread.sleep(30000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                break
            }

            // 提交数据
            var send = false
            var name = packageName
            var url = ""
            val msg2 = Message()
            msg2.what = 1
            msg2.obj = ">>> 发送数据……"
            handler.sendMessage(msg2)
            for(str in getSet("hid").split(",")) {
                if(str == name) {
                    name = "*****"
                    val msg4 = Message()
                    msg4.what = 1
                    msg4.obj = ">>>> 触发屏蔽词……"
                    handler.sendMessage(msg4)
                    break
                }
            }
            if(packageName == lastname) {
                if(passTimes < 3) {
                    // 数据未更改
                    send = false
                    passTimes ++
                    val msg1 = Message()
                    msg1.what = 1
                    msg1.obj = ">> 数据未更改 -> $passTimes"
                    handler.sendMessage(msg1)
                } else {
                    // 防过期刷新
                    send = true
                    val time = System.currentTimeMillis()
                    val nowTime = time / 1000
                    url = if(getSet("qq") == "ERR") {
                        "https://stapx.chuhelan.com/api/SS-Doing/?do=$name&name=${getSet(
                            "name"
                        )}&time=$nowTime&group=${getSet("group")}&type=AD"
                    } else {
                        "https://stapx.chuhelan.com/api/SS-Doing/?do=$name&name=${getSet(
                            "name"
                        )}&time=$nowTime&group=${getSet("group")}&type=AD&qq=${getSet("qq")}"
                    }
                }
            } else {
                // 正常提交
                send = true
                val time = System.currentTimeMillis()
                val nowTime = time / 1000
                url = if(getSet("qq") == "ERR") {
                    "https://stapx.chuhelan.com/api/SS-Doing/?do=$name&name=${getSet(
                        "name"
                    )}&time=$nowTime&group=${getSet("group")}&type=AD"
                } else {
                    "https://stapx.chuhelan.com/api/SS-Doing/?do=$name&name=${getSet(
                        "name"
                    )}&time=$nowTime&group=${getSet("group")}&type=AD&qq=${getSet("qq")}"
                }
            }

            if(send) {
                if (!url.isBlank()) {
                    val back = URL(url).readText()
                    val msg3 = Message()
                    msg3.what = 1
                    msg3.obj = ">> 返回：$back"
                    handler.sendMessage(msg3)
                } else {
                    val msg3 = Message()
                    msg3.what = 1
                    msg3.obj = ">> 错误：空。"
                    handler.sendMessage(msg3)
                }
            }

            // 延迟
            try {
                Thread.sleep(30000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    // 设置相关
    fun getSet(name: String): String {
        for(set in sets) {
            if(set.name == name) {
                return set.value
            }
        }
        return "ERR"
    }

    fun setSet(name: String, value: String) {
        var get = false
        for(set in sets) {
            if(set.name == name) {
                get = true
                set.value = value
                break
            }
        }
        if(!get) {
            sets.add(SetVer(name, value))
        }
        saveSet()
    }

    fun saveSet() {
        var saveStr = ""
        for(set in sets) {
            saveStr += "${set.name}:${set.value}\n"
        }
        val fileDir = filesDir
        val setFile = File("${fileDir.path}/WISD.ini")
        setFile.writeText(saveStr)
    }

    // UI 消息处理
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    if(msg.obj != null) {
                        val textView: TextView = mainActivity.findViewById(R.id.logs)
                        textView.text = "${textView.text}\n${msg.obj.toString()}"
                        val LogView: ScrollView = mainActivity.findViewById(R.id.View)
                        LogView.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                }
            }
        }
    }

    // 检查权限
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun check(context: Context): Boolean {
        var appOps: AppOpsManager? = null
        appOps = context
            .getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = 0
        mode = appOps.checkOpNoThrow(
            "android:get_usage_stats",
            Process.myUid(), context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    // 获取前台应用
    fun getTopAppPackageName(context: Context): String? {
        var packageName = ""
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        try {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                val processes =
                    activityManager.runningAppProcesses
                if (processes.size == 0) {
                    return packageName
                }
                for (process in processes) {
                    if (process.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return process.processName
                    }
                }
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val end = System.currentTimeMillis()
                val usageStatsManager =
                    context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                        ?: return packageName
                val events =
                    usageStatsManager.queryEvents(end - 60 * 1000, end) ?: return packageName
                val usageEvent = UsageEvents.Event()
                var lastMoveToFGEvent: UsageEvents.Event? = null
                while (events.hasNextEvent()) {
                    events.getNextEvent(usageEvent)
                    if (usageEvent.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        lastMoveToFGEvent = usageEvent
                    }
                }
                if (lastMoveToFGEvent != null) {
                    packageName = lastMoveToFGEvent.packageName
                }
            }
        } catch (ignored: Exception) {
        }
        return packageName
    }

    // 对话框
    private fun showInputDialog(): Unit {
        /*@setView 装入一个EditView
     */
        val editText = EditText(this@MainActivity)

        val inputDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        inputDialog.setTitle("添加屏蔽词").setView(editText)
        inputDialog.setPositiveButton("确定",
            DialogInterface.OnClickListener { _, _ ->
                if(editText.text.toString().isBlank() || editText.text.toString().indexOf(",") >= 0) {
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                } else {
                    val nowHidList = getSet("hid")
                    if (nowHidList == "ERR") {
                        setSet("hid", editText.text.toString())
                    } else {
                        setSet("hid", "$nowHidList,${editText.text}")
                    }
                }
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show()

                val text: TextView = this.findViewById(R.id.hidList)
                val list = getSet("hid").split(",")
                text.text = ""
                for (str in list) {
                    text.text = "${text.text}$str\n"
                }

            })
        inputDialog.setNegativeButton("关闭",
            DialogInterface.OnClickListener { _, _ -> })
        // 显示
        inputDialog.show()
    }

    private fun showInputDialogDelHid(): Unit {
        /*@setView 装入一个EditView
     */
        val editText = EditText(this@MainActivity)

        val inputDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        inputDialog.setTitle("删除屏蔽词").setView(editText)
        inputDialog.setPositiveButton("确定",
            DialogInterface.OnClickListener { _, _ ->
                if(editText.text.toString().isBlank() || editText.text.toString().indexOf(",") >= 0) {
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                } else {
                    val nowHidList = getSet("hid")
                    if (nowHidList == "ERR") {
                        Toast.makeText(this, "没有屏蔽词", Toast.LENGTH_SHORT).show()
                    } else {
                        var get = false
                        var strout = ""
                        val list = getSet("hid").split(",")
                        for (str in list) {
                            if(str == editText.text.toString()) {
                                get = true
                            } else {
                                strout = "$strout$str,"
                            }
                        }
                        if(get) {
                            setSet("hid", strout.substring(0, strout.length - 1))
                        } else {
                            Toast.makeText(this, "没有这个屏蔽词", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show()

                val text: TextView = this.findViewById(R.id.hidList)
                val list = getSet("hid").split(",")
                text.text = ""
                for (str in list) {
                    text.text = "${text.text}$str\n"
                }

            })
        inputDialog.setNegativeButton("关闭",
            DialogInterface.OnClickListener { _, _ -> })
        // 显示
        inputDialog.show()
    }


    private fun showexitToBackgroundDialog() {
        val normalDialog = AlertDialog.Builder(this@MainActivity)
        normalDialog.setMessage("\n此操作将退出应用并在后台重启，可以做到隐藏后台应用列表的功能，请保证系统不会清除程序后台。")
        normalDialog.setNegativeButton(
            "关闭"
        ) { dialog, which ->
            val intent = Intent(this, BackRunActivity::class.java).apply {
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            }
            startActivity(intent)
        }
        // 显示
        normalDialog.show()
    }

    // 按钮事件
    fun showhLog(view: View) {
        val scroll: ScrollView = this.findViewById(R.id.View)
        val button: Button = this.findViewById(R.id.logshButton)
        if(scroll.visibility == View.VISIBLE) {
            button.text = this.getString(R.string.showlog_bottom_txt)
            scroll.visibility = View.GONE
        } else {
            button.text = this.getString(R.string.hidlog_bottom_txt)
            scroll.visibility = View.VISIBLE
        }
    }

    fun openAbout(view: View){
        val intent = Intent(this, AboutActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, getSet("love"))
        }
        startActivity(intent)
        if(getSet("love") == "ERR") {
            val buttom: Button = this.findViewById(R.id.aboutButton)
            buttom.visibility = View.GONE
        }
    }

    fun addHid(view: View) {
        showInputDialog()
    }

    fun exitToBackground(view: View) {
        showexitToBackgroundDialog()
    }

    fun delHid(view: View) {
        showInputDialogDelHid()
    }

    fun saveSet(view: View) {
        val textView: TextView = this.findViewById(R.id.logs)
        val nameView: EditText = this.findViewById(R.id.inName)
        val groupView: EditText = this.findViewById(R.id.inGroup)
        val qqView: EditText = this.findViewById(R.id.inQQ)
        textView.text = "${textView.text}\n\n>>> 保存设置……"

        if(!nameView.text.toString().isBlank()) {
            setSet("name", nameView.text.toString())
        }
        if(!groupView.text.toString().isBlank()) {
            setSet("group", groupView.text.toString())
        }
        if(!qqView.text.toString().isBlank()) {
            setSet("qq", qqView.text.toString())
        }
        if(nameView.text.toString().isBlank() &&
            groupView.text.toString().isBlank() &&
            qqView.text.toString().isBlank()) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
        }
    }
}