# 怎么调用onenet平台的API从而读取我们的设备数据和下发命令，做到控制开关

1、首先是怎么读取数据

读取的API是:http://api.heclouds.com/devices/datapoints?devIds=629063002

其中devids这个指的是你的设备ID

![](https://s1.ax1x.com/2020/09/18/wfq6Rs.png)

然后在我们的headers里面添加一些必要参数，就可以调用了

![](https://s1.ax1x.com/2020/09/18/wfq2Mq.png)

其中api-key的是你产品里面的master-apikey

![](https://s1.ax1x.com/2020/09/18/wfqoi4.png)

把我们的参数写好就可以调用了，选择get方式，点击发送

![](https://s1.ax1x.com/2020/09/18/wfLSFe.png)

这个value就是我们需要获取到的参数值了，例如第一个是我们的光照值，然后第二个是我们灯的状态，false代表就是关灯的意思

## 接下来我们再说说怎么去调用API来控制我们的小灯开关

首先我们的API接口是（http://api.heclouds.com/nbiot?imei=865820030399849&obj_id=3311&obj_inst_id=0&mode=1）

imei就是你产品里面的imei，也可以去平台上查找

![](https://s1.ax1x.com/2020/09/18/wfLMSs.png)

然后obj_id指的是我们的设备里面类型的id,控制不同的传感器对应不同的id

![](https://s1.ax1x.com/2020/09/18/wfL1O0.png)

obj_inst_id指的是我们传感器类型里面的id，例如我的Light-Control里面有4个，然后0就是指的是我的第一个个数，1是第二个个数，这样类推下去

![](https://s1.ax1x.com/2020/09/18/wfLHAS.png)

最后mode是指我发送的数据类型，这里mode只有1或者2,1指的是我发送的是16位进制的内容，2指的是我发送的是时间戳,这个mode与我们最后发送的参数有关，我们要发送控制命令的话，那么Mode选择1即可，然后val：0指的是我们发送低电平过去给开发板，从而做到可以控制开发板的小灯或者延伸的话，进而控制舵机或者电机来实现更多的功能

![](https://s1.ax1x.com/2020/09/18/wfOk9J.png)

要注意一点，我们发送这个post命令，它的请求头和get的请求头是一样的也是这样写

![](https://s1.ax1x.com/2020/09/18/wfq2Mq.png)

但是我们还有添加query body这个部分，不然是发送失败的

![](https://s1.ax1x.com/2020/09/18/wfXczd.png)

填写对应的参数即可

最后再在我们的body里面写我们要控制的设备，已经发送的命令，例如我现在是控制一个小灯的开

![](https://s1.ax1x.com/2020/09/18/wfXLyn.png)

![](https://s1.ax1x.com/2020/09/18/wfjPSJ.jpg)

现在蓝灯是一盏灯的状态，其他蓝灯都是关的，我这边点击发送

![](https://s1.ax1x.com/2020/09/18/wfjmFO.png)

返回一个成功的参数，这时我们再看我们的开发板

![](https://s1.ax1x.com/2020/09/18/wfjl6A.jpg)

这时我们的第一个小灯也蓝了，这时我们再去我们的平台上面查看

![](https://s1.ax1x.com/2020/09/18/wfj4XR.png)

它也由原来的false值变成了true，说明我们调用平台的api成功了

这时我们再用这个api写代码发送post，get命令都可以，可以做任何我们可以想象到的事情

[参考网站一](https://open.iot.10086.cn/doc/book/device-develop/multpro/lwm2m/%E5%9F%BA%E4%BA%8EM5310%E6%A8%A1%E7%BB%84%E5%AE%9E%E7%8E%B0NB%E8%AE%BE%E5%A4%87%E6%8E%A5%E5%85%A5%E5%AE%9E%E4%BE%8B%EF%BC%88LWM2M%EF%BC%89.html)

[参考网站二](https://open.iot.10086.cn/doc/nb-iot/book/application-develop/api-list.html)