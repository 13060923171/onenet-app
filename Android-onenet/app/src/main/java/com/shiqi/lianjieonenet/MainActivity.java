package com.shiqi.lianjieonenet;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.shiqi.lianjieonenet.network.HttpHandler;
import com.shiqi.lianjieonenet.util.Json;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {
    static String time;
    static Double wendu;
    static Double shidu;
    static Double guangzhao;
    static String va = "";
//首先先获取onenet的api来做到开发板的控制和获取参数，然后通过计算机网络http网络原理的get和post的方法来调试这些api来实现我们的功能
//里面涉及到json的定位方法，杀死线程的方法
//wendu shidu guangzhao duoji
    public final String url1 = "http://api.heclouds.com/devices/645974289/datapoints?datastream_id=3303_0_5700&limit=5";
    public final String url2 = "http://api.heclouds.com/devices/645974289/datapoints?datastream_id=3304_0_5700&limit=5";
    public final String url3 = "http://api.heclouds.com/devices/645974289/datapoints?datastream_id=3301_0_5700&limit=5";
    public final String url4 = "http://api.heclouds.com/devices/645974289/datapoints?datastream_id=3311_2_5850";
    private TextView txtwendu,txtshidu,txtwendu2,txtshidu2,tishi,shijian,txtguangzhao,txtguangzhao2;
    private EditText etguangzhao,etbaojin;
    private Button btn,shouhui,zhankai,kaiqiyingyong,guangbiyingyong,exit;

    @Override
    //点击事件，绑定按钮和文本
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtwendu = findViewById(R.id.txtwendu);
        txtshidu = findViewById(R.id.txtshidu);
        txtguangzhao = findViewById(R.id.textguangzhao1);
        txtguangzhao2 = findViewById(R.id.txtguangzhao2);
        txtwendu2 = findViewById(R.id.txtwendu2);
        txtshidu2 = findViewById(R.id.txtshidu2);
        tishi = findViewById(R.id.tishi);
        shijian = findViewById(R.id.txtshijian);
        btn = findViewById(R.id.button);
        exit = findViewById(R.id.button2);
        shouhui = findViewById(R.id.button3);
        zhankai = findViewById(R.id.button4);
        kaiqiyingyong = findViewById(R.id.button5);
        guangbiyingyong = findViewById(R.id.button6);
        etguangzhao = findViewById(R.id.etbaojing1);
        TextView show_tv =  (TextView) tishi.findViewById(R.id.tishi);
        show_tv.setSelected(true);
        show_tv.setText("     点击“开启应用”即可查看当前的模式");
        show_tv.setTextColor(Color.WHITE);

        //开启应用按钮点击事件
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        //退出应用
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定义一个参数，把这些参数传给post命令所需要的data
                    int con = 1;
                    switch (con){
                        case 1:
                            SwitchControl("865820030399849","3311",2,1,5850,0);
                            con += 1;
                        case 2:
                            closeApp();
                    }
                }
        });
        //控制命令的data,舵机控制，0、1就是TRUE或false
        shouhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightControl("865820030399849","3311",1,1,5850,0);
            }
        });
        zhankai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightControl("865820030399849","3311",1,1,5850,1);
            }
        });
        //自动或者手动开关的控制参数
        guangbiyingyong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchControl("865820030399849","3311",2,1,5850,0);
            }
        });
        kaiqiyingyong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchControl("865820030399849","3311",2,1,5850,1);
            }
        });
    }
    //下发命令
    public void lightControl(final String imei, final String objId, final int objInstId, final int mode, final int resId, final int val) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String toastStr = null;
                //这个通过http协议用post命令下发到开发板，开发板收到命令之后开始执行开启应用，这是一个总的开关
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
    //开关控制
    public void SwitchControl(final String imei, final String objId, final int objInstId, final int mode, final int resId, final int val) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String toastStr = null;
                try {
                    //api地址
                    String url = String.format("http://api.heclouds.com/nbiot?imei=%s&obj_id=%s&obj_inst_id=%d&mode=%d", imei, objId, objInstId, mode);
                    //
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

    //杀死线程,
    private void closeApp() {
        Log.e("bug", "Main close");
        //退出程序
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        }, 500);
    }
    //获取网络端数据，请求方式为Http
    public String getAsString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }
    public void getData(){
        new Thread(new Runnable() {
            public String va;

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                //温度
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
                    //湿度
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
                    if (shidu >= 80){
                        lightControl("865820030399849","3311",0,1,5850,0);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //光照
                try {
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            .url(url3)
                            .addHeader("api-key", "LoEEarS4lSeRRLeuwtlK19arUlY=")
                            .addHeader("Content-Type","application/json")
                            .build();
                    Response response1 = client1.newCall(request1).execute();
                    String responseData1 = response1.body().string();
                    JsonRootBean app1 = Json.gson.fromJson(responseData1, JsonRootBean.class);
                    List<Datastreams> streams1 = app1.getData().getDatastreams();
                    List<Datapoints> points1 = streams1.get(0).getDatapoints();
                    int count = app1.getData().getCount();//获取数据的数量
                    guangzhao = points1.get(1).getValue();
                    Log.w("www","guangzhao="+guangzhao);
                    final String guangzhao1 = guangzhao.toString();
                    txtguangzhao.post(new Runnable() {
                        @Override
                        public void run() {
                            txtguangzhao2.setText("光照强度：");
                        }
                    });
                    txtguangzhao.post(new Runnable() {
                        @Override
                        public void run() {
                            etguangzhao.setText(String.format("%.1fL",guangzhao));
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //机器模式
                try {
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            .url(url4)
                            .addHeader("api-key", "LoEEarS4lSeRRLeuwtlK19arUlY=")
                            .addHeader("Content-Type","application/json")
                            .build();

                    Response response1 = client1.newCall(request1).execute();
                    String responseData1 = response1.body().string();
                    //用Json定位的方式来定位到这是true或false
                    JSONObject object = new JSONObject(responseData1);
                    String data = object.getString("data");
                    JSONObject datastreams = new JSONObject(data);
                    String da = datastreams.getString("datastreams");
                    JSONArray jsonArray = new JSONArray(da);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject != null) {
                            String datapoints = jsonObject.optString("datapoints");
                            JSONArray jsonArray1 = new JSONArray(datapoints);
                            for (int j = 0; i < jsonArray1.length(); j++){
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                if (jsonObject1 != null){
                                    String val = jsonObject1.optString("value");
                                    va = val;
                                    if(va == "true"){
                                        TextView show_tv =  (TextView) tishi.findViewById(R.id.tishi);
                                        show_tv.setSelected(true);

                                        show_tv.setText("    当前模式为手动");
                                        show_tv.setTextColor(Color.WHITE);
                                    }
                                    else if(va == "false"){
                                        TextView show_tv =  (TextView) tishi.findViewById(R.id.tishi);
                                        show_tv.setSelected(true);

                                        show_tv.setText("    当前模式为自动");
                                        show_tv.setTextColor(Color.WHITE);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
