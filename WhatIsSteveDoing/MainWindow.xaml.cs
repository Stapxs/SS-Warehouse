using System;
using System.Windows;
using System.Runtime.InteropServices;
using System.Text;
using System.Security.Policy;
using System.IO;
using System.Windows.Documents;
using System.Threading;
using System.Windows.Input;
using System.Windows.Interop;
using System.Diagnostics;

namespace WhatIsSteveDoing
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {

        string yname = "";
        string fsave = "WISD.ini";
        System.Collections.Generic.List<setVer> sets = new System.Collections.Generic.List<setVer>();
        string lastname = "";
        Thread thread;
        bool exit = false;
        bool path = false;

        // 跳过次数
        int passTimes = 0;

        public MainWindow()
        {
            InitializeComponent();
            this.Closing += Window_Closing;
            sets = ReadSet();

            setVer nameset = GetSet("name");
            if (nameset.name != "Err")
            {
                yname = nameset.value;
                name.Text = yname;
            }
            setVer ahid = GetSet("autohid");
            if (ahid.value == "true")
            {
                setautohidd.Visibility = Visibility.Collapsed;
                this.ShowInTaskbar = false;
                this.Hide();
            }

            logbox.Text += GetForgroundAppInfo();

            logbox.Text += "\n\n>>> 初始化完成，使用 Ctrl + Alt + U 显示隐藏的窗口。\n\n";

            // 获取公告
            logbox.Text += "\n>>> 获取公告……";
            thread = new Thread(GetNote);
            thread.Start();

            // 启动循环
            logbox.Text += "\n>>> 启动循环……";
            thread = new Thread(Run);
            thread.Start();
        }

        public void GetNote()
        {
            try
            {
                string url = "https://stapx.chuhelan.com/api/SS-Doing/note.txt";
                string back = CallWebPage(url, null, null);
                if (!String.IsNullOrWhiteSpace(back))
                {
                    this.Dispatcher.BeginInvoke(new Action(() =>
                    {
                            note.Text = back;
                    }), System.Windows.Threading.DispatcherPriority.SystemIdle, null);
                }
            }
            catch(Exception e)
            {
                this.Dispatcher.BeginInvoke(new Action(() =>
                {
                        note.Text = e.Message;
                }), System.Windows.Threading.DispatcherPriority.SystemIdle, null);
            }
        }

        public void Run()
        {
            while (!exit)
            {
                if (!path)
                {
                    this.Dispatcher.BeginInvoke(new Action(() =>
                    {
                        logbox.Text += "\n>> 正在处理更新……";
                    }), System.Windows.Threading.DispatcherPriority.SystemIdle, null);

                    string back = GetForgroundAppInfo();

                    this.Dispatcher.BeginInvoke(new Action(() =>
                    {
                        if(logbox.Text.Length >= 10000)
                        {
                            logbox.Text = "";
                        }
                        logbox.Text += back;
                        logbox.SelectionStart = logbox.Text.Length;
                    }), System.Windows.Threading.DispatcherPriority.SystemIdle, null);

                    Thread.Sleep(30000);
                }
            }
        }

        protected override void OnSourceInitialized(EventArgs e)
        {
            base.OnSourceInitialized(e);

            IntPtr handle = new WindowInteropHelper(this).Handle;
            RegisterHotKey(handle, 919, 0x0001 | 0x0002, 0x55);

            HwndSource source = PresentationSource.FromVisual(this) as HwndSource;
            source.AddHook(WndProc);
        }

        IntPtr WndProc(IntPtr hwnd, int msg, IntPtr wParam, IntPtr lParam, ref bool handle)
        {
            try
            {
                if ((int)wParam == 919)
                {
                    this.Show();
                }
            }
            catch { }
            return IntPtr.Zero;
        }

        public class setVer
        {
            public string name;
            public string value;

            public setVer(string name, string value)
            {
                this.name = name;
                this.value = value;
            }
        }

        public System.Collections.Generic.List<setVer> ReadSet()
        {
            logbox.Text += "\n>> 读取设置……";
            System.Collections.Generic.List<setVer> info = new System.Collections.Generic.List<setVer>();
            StreamReader sr = new StreamReader(fsave, Encoding.Default);
            String line;
            while ((line = sr.ReadLine()) != null)
            {
                if (!string.IsNullOrWhiteSpace(line))
                {
                    string[] infolist = line.Split(':');
                    info.Add(new setVer(infolist[0], infolist[1]));
                    logbox.Text += "\n" + infolist[0] + ":" + infolist[1];
                }
            }
            sr.Close();
            return info;
        }

        public setVer GetSet(string name)
        {
            foreach (setVer info in sets)
            {
                if (info.name == name)
                {
                    return info;
                }
            }
            return new setVer("Err", "NoFound");
        }

        [DllImport("user32.dll", CharSet = CharSet.Auto, ExactSpelling = true)]
        public static extern IntPtr GetForegroundWindow();
        [DllImport("user32.dll", EntryPoint = "GetWindowText")]
        public static extern int GetWindowText(int hwnd, StringBuilder lpString, int cch);
        [DllImport("user32.dll")]
        public static extern int GetWindowTextLength(IntPtr hWnd);
        [DllImport("user32.dll")]
        public static extern bool RegisterHotKey(IntPtr hWnd, int id, uint fsModifiers, uint vk);
        [DllImport("user32.dll")]
        public static extern bool UnregisterHotKey(IntPtr hWnd, int id);


        public class AppInfo
        {
            public IntPtr hander;
            public string windowName;
        }

        public string GetForgroundAppInfo()
        {
            string says = "";

            AppInfo info = new AppInfo();

            // 获取焦点（前台）应用
            IntPtr hWnd = GetForegroundWindow();
            info.hander = hWnd;

            // 获取窗口标题
            int length = GetWindowTextLength(hWnd);
            StringBuilder s = new StringBuilder(length + 1);
            int i = GetWindowText((int)hWnd, s, s.Capacity);
            info.windowName = s.ToString();

            // 测试
            says += "\n>> 当前的前台窗口标题为：" + s.ToString() + "\n句柄：" + (int)hWnd;

            // 发送数据
            if (!string.IsNullOrWhiteSpace(lastname))
            {
                if (lastname != s.ToString())
                {
                    setVer nameset = GetSet("name");
                    if (nameset.name != "Err")
                    {
                        says += "\n>> 数据变更";
                        string time = GetTimeStamp();
                        lastname = s.ToString();
                        string url = "https://stapx.chuhelan.com/api/SS-Doing/?do=" + s.ToString() + "&name=" + nameset.value + "&time=" + time + "&type=PC";
                        says += "\n>> 发送数据……";
                        string back = CallWebPage(url, null, null);
                        says += "\n返回：" + back;
                        passTimes = 0;
                    }
                    else
                    {
                        says += "\n错误：用户名设置不存在，请设置后重启。";
                    }
                }
                else
                {
                    if (passTimes <= 3)
                    {
                        says += "\n>> 数据未变更";
                        passTimes += 1;
                    }
                    else
                    {
                        says += "\n>> 防过期刷新";
                        string time = GetTimeStamp();
                        lastname = s.ToString();
                        string url = "https://stapx.chuhelan.com/api/SS-Doing/?do=" + s.ToString() + "&name=" + yname + "&time=" + time + "&type=PC";
                        says += "\n>> 发送数据……";
                        string back = CallWebPage(url, null, null);
                        says += "\n返回：" + back;
                        passTimes = 0;
                    }
                }
            }
            else
            {
                says += "\n>> 上次数据空白或不存在";
                setVer nameset = GetSet("name");
                if (nameset.name != "Err")
                {
                    string time = GetTimeStamp();
                    lastname = s.ToString();
                    string url = "https://stapx.chuhelan.com/api/SS-Doing/?do=" + s.ToString() + "&name=" + nameset.value + "&time=" + time + "&type=PC";
                    says += "\n>> 发送数据……";
                    string back = CallWebPage(url, null, null);
                    says += "\n返回：" + back;
                }
                else
                {
                    says += "\n错误：用户名不存在。";
                }

            }
            return says;
        }

        /// <summary>
        /// 访问URL地址
        /// </summary>
        /// <param name="url">URL地址</param>
        /// <param name="postDataStr">参数</param>
        /// <param name="Encod">编码方式</param>
        /// <returns></returns>
        private static string CallWebPage(string url, string postDataStr, Encoding Encod)
        {
            string rStr = "";
            System.Net.WebRequest req = null;
            System.Net.WebResponse resp = null;
            System.IO.Stream os = null;
            System.IO.StreamReader sr = null;
            try
            {
                //创建连接
                req = System.Net.WebRequest.Create(url);
                //设置访问方式和发送的请求数据的内容类型
                if (string.IsNullOrEmpty(postDataStr))
                {
                    req.ContentType = "application/x-www-form-urlencoded";
                    req.Method = "GET";
                }
                else
                {
                    req.ContentType = "application/x-www-form-urlencoded";
                    req.Method = "POST";
                    if (Encod == null)
                    {
                        Encod = System.Text.Encoding.Default;
                    }
                    byte[] bytes = Encod.GetBytes(postDataStr);
                    req.ContentLength = bytes.Length;
                    os = req.GetRequestStream();
                    os.Write(bytes, 0, bytes.Length);
                    os.Close();
                }

                //读取返回结果
                resp = req.GetResponse();
                sr = new System.IO.StreamReader(resp.GetResponseStream(), System.Text.Encoding.Default);
                rStr = sr.ReadToEnd();
            }
            catch (Exception ex1)
            {
                //LogUtil.Warn("HttpUtil.CallWebPage 异常：" + ex1.Message);

            }
            finally
            {
                try
                {
                    //关闭资源
                    if (os != null)
                    {
                        os.Dispose();
                        os.Close();
                    }
                    if (sr != null)
                    {
                        sr.Dispose();
                        sr.Close();
                    }

                    if (resp != null)
                    {
                        resp.Close();

                    }


                    if (req != null) req = null;
                }
                catch (Exception ex2)
                {
                    //LogUtil.Exception("HttpUtil.CallWebPage 关闭连接异常：" + ex2.Message);
                }
            }
            return rStr;
        }

        private void save_Click(object sender, RoutedEventArgs e)
        {
            yname = name.Text;
            string saveline = "";
            bool isGet = false;
            if (File.Exists(fsave))
            {
                StreamReader sr = new StreamReader(fsave, Encoding.Default);
                String line;
                while ((line = sr.ReadLine()) != null)
                {
                    string[] info = line.Split(':');
                    if (info[0] == "name")
                    {
                        isGet = true;
                        saveline += "name:" + yname + "\n";
                    }
                    else
                    {
                        saveline += line + "\n";
                    }

                }
                sr.Close();
                if (isGet)
                {
                    logbox.Text += "\n>> 正在保存设置……";
                    logbox.Text += saveline;
                    File.WriteAllText(fsave, saveline);
                }
                else
                {
                    FileStream fs = new FileStream(fsave, FileMode.Append);
                    StreamWriter sw = new StreamWriter(fs);
                    sw.WriteLine("name:" + yname + "\n");
                    sw.Flush();
                    sw.Close();
                    fs.Close();
                }
            }
            else
            {
                FileStream fs = new FileStream(fsave, FileMode.Open, FileAccess.Write);
                StreamWriter sr = new StreamWriter(fs);
                sr.WriteLine("name:" + yname + "\n");
                sr.Flush();
                sr.Close();
                fs.Close();
            }
        }

        private void hidd_Click(object sender, RoutedEventArgs e)
        {
            MessageBox.Show("使用 Ctrl + Alt + U 显示隐藏的窗口");
            this.ShowInTaskbar = false;
            this.Hide();
        }

        private void setautohidd_Click(object sender, RoutedEventArgs e)
        {
            FileStream fs = new FileStream(fsave, FileMode.Append);
            StreamWriter sr = new StreamWriter(fs);
            sr.WriteLine("autohid:" + "true" + "\n");
            sr.Flush();
            sr.Close();
            fs.Close();
            setautohidd.Visibility = Visibility.Collapsed;
        }

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            exit = true;
            thread.Abort();
            string time = GetTimeStamp();
            string url = "https://stapx.chuhelan.com/api/SS-Doing/?do=此用户已退出%20WISD%20同步程序。" + "&name=" + yname + "&time=" + time + "&type=PC";
            logbox.Text += "\n>> 发送数据……";
            string back = CallWebPage(url, null, null);
            logbox.Text += "\n返回：" + back;
            e.Cancel = false;
        }

        public void Command()
        {
            MessageBox.Show("abab");
        }

        private void path_Click(object sender, RoutedEventArgs e)
        {
            if (path)
            {
                path = false;
                pathbtm.Content = "继续同步";
                logbox.Text += "\n>>> 已暂停提交数据。";

                string time = GetTimeStamp();
                string url = "https://stapx.chuhelan.com/api/SS-Doing/?do=此用户已暂停%20WISD%20同步程序。" + "&name=" + yname + "&time=" + time + "&type=PC";
                logbox.Text += "\n>> 发送数据……";
                string back = CallWebPage(url, null, null);
                logbox.Text += "\n返回：" + back;

                lastname = "此用户已暂停 WISD 同步程序。";
            }
            else
            {
                path = true;
                pathbtm.Content = "暂停同步";
                logbox.Text += "\n>>> 已继续提交数据。";

                string back = GetForgroundAppInfo();
                logbox.Text += back;
            }
        }

        public static string GetTimeStamp()
        {
            TimeSpan ts = DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, 0);
            return Convert.ToInt64(ts.TotalSeconds).ToString();

        }
    }
}
