# 怎么去建立M5310去和onenet建立连接，从而去调用onenet的API

  首先我们需要onenet的账号，然后选择NB-IOT

![](https://s1.ax1x.com/2020/09/10/wYFkwV.png)

然后创建设备，设备的IMEI是在M5310的芯片上面，IMSI是去打开我们的串口助手，然后获取到IMSI

![](https://s1.ax1x.com/2020/09/10/wYE0mR.png)

![](https://s1.ax1x.com/2020/09/10/wYZQI0.png)

获取到imei和imsi就可以创建好这个设备，并且重新打开我们的开发板

![](https://s1.ax1x.com/2020/09/10/wYZrRO.jpg)

当有蓝灯出现的时候也就说明我们的开发板和云平台对接了，这时我们再打开我们的onenet平台就可以看到在线了

![](https://s1.ax1x.com/2020/09/10/wYZbLj.png)

## 我们该怎么调用我们的API呢

[参考这个文档](https://open.iot.10086.cn/doc/nb-iot/book/application-develop/list/20batch-query-dev-latest-data.html)

```python
GET http://api.heclouds.com/devices/datapoints?devIds=12323,12324 HTTP/1.1
    #devIds是指设备的id
api-key: WhI*************v1c=#这个是根据Master-APIkey来看的
Content-Type: application/json
```

用我们的测试软件airpost去测试

![](https://s1.ax1x.com/2020/09/10/wYnBiq.png)

最后返回的数据这3个设备，分别是我们的温度，湿度，光照数据。