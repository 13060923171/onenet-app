# Android获取onenet的平台数据之发送命令篇

这篇与上面的大体思路一样，就不再重复多说了，这里注意的一点是怎么在Android端添加一个post控件，做到命令的时候发送

```Android
//下发命令
    public void lightControl(final String imei, final String objId, final int objInstId, final int mode, final int resId, final int val) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String toastStr = null;
                try {
                    String url = String.format("http://api.heclouds.com/nbiot?imei=%s&obj_id=%s&obj_inst_id=%d&mode=%d", imei, objId, objInstId, mode);
                    String body = String.format("{\"data\":[{\"res_id\": %d,\"val\": %d}]}", resId, val);
                    Request request = HttpHandler.INSTANCE.post( url, RequestBody.create(HttpHandler.JSON_MEDIA_TYPE, body));
                    Response response = HttpHandler.INSTANCE.getResponse(request);
                    String jsonStr = response.body().string();
                    final JsonRootBean res = Json.gson.fromJson(jsonStr, JsonRootBean.class);
                    Log.w("test", jsonStr);
                    switch (res.getErrno()) {
                        case 0:
                            // TODO 成功
                            break;
                        case 2001:
                            toastStr = "设备不在线。错误代码：2001";
                            break;
                        default:
                            toastStr = String.format("%s. 错误代码：%d", res.getError(), res.getErrno());
                            break;
                    }
                } catch (IOException e) {
                    // TODO 提示连接异常信息
                    toastStr = "连接失败，请检测手机或设备的网络状态";
                    e.printStackTrace();
                } finally {
                    if(toastStr != null) {
                        final String finalToastStr = toastStr;
                        etbaojin.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( MainActivity.this, finalToastStr, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }).start();
    }
```

这里是用json来定位，后面再用post来请求

![](https://s1.ax1x.com/2020/09/27/0F3Uzj.jpg)

首先我们这边的板一开始是只要一盏灯的

![](https://s1.ax1x.com/2020/09/27/0F30Lq.jpg)

操控界面是这样的，因为舵机那一方面还没调好，所以我们就先用小灯来代替，当你按了伸展按钮的时候

![](https://s1.ax1x.com/2020/09/27/0F3RSJ.jpg)

小灯就自动亮了，当你再按收回按钮的时候

![](https://s1.ax1x.com/2020/09/27/0F35ex.jpg)

它就自动灭了，这样我们APP的大致功能全是实现了