<?php

header("Content-type:text/html;charset=utf-8");

// SS Doing API : 晓狩 Doing 前台 API Set
// 林槐出品，必属稽品！

// In - do: 窗口标题，name: 用户名，type: 平台，time: 时间戳，group: 群组标签，qq: QQ 识别标签。
// Save - name/qq:type:group:time:do 
    // exp - Stapxs/1234567890:Android:DHW:1603813266:index.php - Visual Studio Code
// Out - type > str
    // exp - Err > 这是个报错！

// 全局变量
$outAdd = '';                   // 添加的字符串
$filestat = 'doing.ini';        // 文件名
// 判断输入
if($_GET['do'] == null || $_GET['name'] == null || $_GET['type'] == null || $_GET['time'] == null)
{
    echo "Err > 参数不完整，请确认调入内容。";
    exit;
}
if($_GET['group'] == null)
{
    echo "Err > 客户端版本已经不受支持，请更新客户端。";
    exit;
}
if(strpos($_GET['name'], ':') != false || strpos($_GET['type'], ':') != false || strpos($_GET['group'], ':') != false || strpos($_GET['time'], ':') != false)
{
    echo "Err > 输入不合法，请确认调入内容。";
    exit;
}
// 整理字符串
if($_GET['qq'] == null || strpos($_GET['qq'], ':') != false)
{
    // name:type:group:time:do
    $outAdd = $_GET['name'] . ':' . $_GET['type'] . ':' . $_GET['group'] . ':' . $_GET['time'] . ':' . $_GET['do'] . "\n";
}
else
{
    // name(qq):type:group:time:do
    $outAdd = $_GET['name'] . '/' . $_GET['qq'] . ':' . $_GET['type'] . ':' . $_GET['group'] . ':' . $_GET['time'] . ':' . $_GET['do'] . "\n";
}
// 检索文件替换
$cbody = file($filestat);
$isget = false;
$outstr = "";
for($i=0; $i<count($cbody); $i++)
{
    $infostr = explode(":", $cbody[$i]);
    $names = explode("/", $infostr[0]);
    if(($_GET['name'] == $names[0] || $_GET['qq'] == $names[1]) && $_GET['type'] == $infostr[1] && $_GET['group'] == $infostr[2])
    {
        $isget = true;
        $outstr = $outstr . $outAdd;
    }
    else
    {
        $outstr = $outstr . $cbody[$i];
    }
}
if($isget)
{
    // 写入修改
    $file = fopen($filestat, "w");
    fwrite($file, $outstr);
    fclose($file);
    echo "OK > REWRITE";
}
else
{
    // 写入增加
    $file = fopen($filestat, "a+");
    fwrite($file, $outAdd);
    fclose($file);
    echo "OK > ADD";
}

?>