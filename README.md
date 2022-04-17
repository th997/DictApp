# DictApp
一个可以自定义划词搜索的android工具

## 功能示例
> 当你阅读英文文档时可以自定义一个查词地址, 如下所示:
选中单词后会弹出选项, 选择"DICTAPP"后会跳转到自定义的页面, 如:
http://dict.kekenet.com/en/escalation
 
<img src="doc/img/demo.png"> 

> 可以通过修改配置文件设置不同的搜索链接,配置文件位于sd卡下 "DictApp/config" ,打开该文本编辑即可

配置文件默认内容为: http://dict.kekenet.com/en/%s  "%s"会自动替换为选中的单词

可以自行修改为其它地址, 如: https://www.youdao.com/result?word=%s&lang=en
