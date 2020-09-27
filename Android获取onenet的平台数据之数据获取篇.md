# Android获取onenet的平台数据之数据获取篇

接着上面的步骤，这时候我们已经成功获取onenet平台上面的API了，那么剩下的步骤就是怎么通过写代码从而调用API实现数据的实时查看和发送命令

```java
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shiqi.lianjieonenet.util.Json;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    static String time;
    static Double wendu;
    static Double shidu;


    public final String url1 = "http://api.heclouds.com/devices/629063002/datapoints?datastream_id=3303_0_5700&limit=5";
    public final String url2 = "http://api.heclouds.com/devices/629063002/datapoints?datastream_id=3304_0_5700&limit=5";
    private TextView txtwendu,txtshidu,txtwendu2,txtshidu2,tishi,shijian;
    private EditText etbaojin;
    private Button btn,baojin;
    private int baojinshu = 35;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtwendu = findViewById(R.id.txtwendu);
        txtshidu = findViewById(R.id.txtshidu);
        txtwendu2 = findViewById(R.id.txtwendu2);
        txtshidu2 = findViewById(R.id.txtshidu2);
        tishi = findViewById(R.id.tishi);
        shijian = findViewById(R.id.txtshijian);
        btn = findViewById(R.id.button);
        baojin = findViewById(R.id.button2);
        etbaojin = findViewById(R.id.etbaojing);

        TextView show_tv =  (TextView) tishi.findViewById(R.id.tishi);
        show_tv.setSelected(true);
        show_tv.setText("     点击“开启应用”即可查看室内温湿度");
        show_tv.setTextColor(Color.WHITE);

        //开启应用按钮点击事件
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        //修改报警值
        baojin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bao=etbaojin.getText().toString().trim();
                baojinshu = Integer.parseInt(bao);
                //其中baojinshu为被转的数字，bao为将被转换的字符串。
                getData();
            }
        });
    }

    //获取网络端数据，请求方式为Http
    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url1)
                            .addHeader("api-key", "LoEEarS4lSeRRLeuwtlK19arUlY=")
                            .addHeader("Content-Type","application/json")
                            .build();
                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    Log.w("test", responseData);

                    //json提取数据
                    JsonRootBean app = Json.gson.fromJson(responseData, JsonRootBean.class);
                    List<Datastreams> streams = app.getData().getDatastreams();
                    List<Datapoints> points = streams.get(0).getDatapoints();
                    int count = app.getData().getCount();//获取数据的数量
//                    wendu = new Integer(1);
                    wendu = points.get(1).getValue();
                    Log.w("www","wendu="+wendu);//log输出
                    final String wendu1 = wendu.toString();//数据类型转换
                    //textview显示需要回到主线程
                    txtwendu.post(new Runnable() {
                        @Override
                        public void run() {
                            txtwendu.setText("当前温度：");
                        }
                    });
                    txtwendu2.post(new Runnable() {
                        @Override
                        public void run() {
                            txtwendu2.setText(String.format("%.1f°C",wendu));
                        }
                    });
                    tishi.post(new Runnable() {
                        @Override
                        public void run() {
                            if(wendu>=baojinshu){
                                tishi.setText("注意！当前室内温度过高！");
                                tishi.setTextColor(Color.RED);
                                Toast.makeText(MainActivity.this, "当前温度过高！您可以打开空调降温，防止中暑。", Toast.LENGTH_SHORT).show();
                            }
                            else if(wendu<baojinshu){
                                TextView show_tv =  (TextView) tishi.findViewById(R.id.tishi);
                                show_tv.setSelected(true);
                                show_tv.setText("当前室内温度为"+String.format("%.2f°C",wendu));
                                show_tv.setTextColor(Color.WHITE);
                            }
                        }
                    });
                    time = new String();
                    time = points.get(1).getAt().toString();
//                    final String time1 =time.substring(0,19);
                    shijian.post(new Runnable() {
                        @Override
                        public void run() {
                            shijian.setText(" 时间: "+time);
//                            shijian.setText(" 时间: "+time1);
                        }
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            .url(url2)
                            .addHeader("api-key", "LoEEarS4lSeRRLeuwtlK19arUlY=")
                            .addHeader("Content-Type","application/json")
                            .build();
                    Response response1 = client1.newCall(request1).execute();
                    String responseData1 = response1.body().string();
                    JsonRootBean app1 = Json.gson.fromJson(responseData1, JsonRootBean.class);
                    List<Datastreams> streams1 = app1.getData().getDatastreams();
                    List<Datapoints> points1 = streams1.get(0).getDatapoints();
                    int count = app1.getData().getCount();//获取数据的数量
                    shidu = points1.get(1).getValue();
                    Log.w("www","shidu="+shidu);
                    final String shidu1 = shidu.toString();
                    txtshidu.post(new Runnable() {
                        @Override
                        public void run() {
                            txtshidu.setText("当前湿度：");
                        }
                    });
                    txtshidu2.post(new Runnable() {
                        @Override
                        public void run() {
                            txtshidu2.setText(String.format("%.1f%%",shidu));
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

```

## 同时我们还得在build.gradle中增加：

```Android
compile 'com.squareup.okhttp3:okhttp:3.4.1'
compile 'com.google.code.gson:gson:2.7'

compile 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.0'
compile 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
implementation 'com.google.android.material:material:1.1.0'
```

## 在AndroidManifest.xml中添加：

```Android
<uses-permission android:name="android.permission.INTERNET" />
```

因为我们获取的数据是json格式所以我们还得进行数据转换，这样我们才可以顺利定位到我们需要的文件

我们需要借用这个网站来进行数据转换[json 在线转换工具](http://www.bejson.com/)

用网站自动生成4个类JsonRootBean，Data，Datapoints ，Datastreams

然后因为我们的时间数据类型错误，那么如何才能解决这个错误呢

![](https://s1.ax1x.com/2020/09/23/wXHDyV.png)

我们需要多创建一个util文件，并且在这个文件里面添加json这个类

```java
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {
    public static Gson gson;

    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }
}

```

做到数据类型正确转换，这时再运行我们的Android来看看我们获取到的数据，这里有一点要注意的，我们需要用真机来进行调试，如果用模拟器会出现闪退的现象，这个应该是和网络有关

### 这时候再打开我们的调试好的APP，点击登录查看

![](https://s1.ax1x.com/2020/09/23/wXbpm8.jpg)

时间是最新的，温度是26.6°，湿度是67.1%，那么我们再登录onenet的平台看看，实时上传的数据是多少

![](https://s1.ax1x.com/2020/09/23/wXbVlq.png)

![](https://s1.ax1x.com/2020/09/23/wXbnmT.png)

很显然，数据是完全对的上的，那么到这一步就是说明可以正确获取到数据了，接下来就是如何发送一个post命令来控制我们的舵机或电机，从而实现智能晾衣架的功能了