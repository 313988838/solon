## Solon 启动处理顺序

* 1.实例化 Solon.global() 并加载配置
* 2.加载扩展文件夹
* 3.扫描插件并排序
* 4.运行builder函数
* 5.运行插件
* 6.推送 PluginLoadEndEvent 事件
* 7.导入java bean(@Import)
* 8.扫描并加载java bean
* 9.推送 BeanLoadEndEvent 事件
* a.加载渲染印映关系
* b.执行bean加完成事件
* c.推送 AppLoadEndEvent 事件
* d.结束