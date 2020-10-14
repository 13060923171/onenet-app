package com.shiqi.lianjieonenet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shiqi.lianjieonenet.network.HttpHandler;
import com.shiqi.lianjieonenet.util.Json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    static String time;
    static Double wendu;
    static Double shidu;
    static Double guangzhao;


    public final String url1 = "http://api.heclouds.com/devices/629063002/datapoints?datastream_id=3303_0_5700&limit=5";
    public final String url2 = "http://api.heclouds.com/devices/629063002/datapoints?datastream_id=3304_0_5700&limit=5";
    public final String url3 = "http://api.heclouds.com/devices/629063002/datapoints?datastream_id=3301_0_5700&limit=5";
    private TextView txtwendu,txtshidu,txtwendu2,txtshidu2,tishi,shijian,txtguangzhao,txtguangzhao2;
    private EditText etbaojin,etguangzhao;
    private Button btn,baojin,shouhui,zhankai;
    private int baojinshu = 35;

    @Override
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
        baojin = findViewById(R.id.button2);
        shouhui = findViewById(R.id.button3);
        zhankai = findViewById(R.id.button4);
        etguangzhao = findViewById(R.id.etbaojing1);

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
        shouhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightControl("865820030399849","3311",0,1,5850,0);
            }
        });
        zhankai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightControl("865820030399849","3311",0,1,5850,1);
            }
        });
    }
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
                    if (shidu >= 80){
                        lightControl("865820030399849","3311",0,1,5850,0);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            }
        }).start();
    }
}
