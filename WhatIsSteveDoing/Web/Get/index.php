<?php

header("Content-type:text/html;charset=utf-8");

// SS Doing API : 晓狩 Doing 前台 API Get
// 林槐出品，必属稽品！

// In - name: 用户名，group: 群组标签，qq: QQ 识别标签，bot: Bot 标识（用于统计数据）。
// Out - name/qq:type:group:time:do 
    // exp - Stapxs/1234567890:Android:DHW:1603813266:index.php - Visual Studio Code

// 全局变量
$filestat = '../doing.ini';        // 文件名
// 判断输入
if($_GET['bot'] == null || ($_GET['qq'] == null && $_GET['name'] == null))
{
    echo "Err > 参数不完整，请确认调入内容。";
    exit;
}
if($_GET['group'] == null)
{
    echo "Err > 获取方式已经不受支持，请更新。";
    exit;
}
if(strpos($_GET['name'], ':') != false || strpos($_GET['group'], ':') != false || strpos($_GET['qq'], ':') != false)
{
    echo "Err > 输入不合法，请确认调入内容。";
    exit;
}
// 检索文件
$outStr = '';
$cbody = file($filestat);
for($i=0; $i<count($cbody); $i++)
{
    $infostr = explode(":", $cbody[$i]);
    $names = explode("/", $infostr[0]);
    if(($_GET['name'] == $names[0] || $_GET['qq'] == $names[1]) && $_GET['group'] == $infostr[2])
    {
        $outStr = $outStr . $cbody[$i];
    }
}
if($outStr == '')
{
    echo "Err > 什么都没有！";
}
else
{
    echo $outStr;
}

?>