package com.shiqi.lianjieonenet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DengLu extends AppCompatActivity {
    private Button btn_log;
    private EditText etzhangzhao,etmima;
    private String userName,psw;
    private TextView textView;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_denglu);
        textView = findViewById(R.id.tishi);
        init();

    }
    private void init() {
        btn_log = findViewById(R.id.btn_log);
        etzhangzhao = findViewById(R.id.etzhangzhao);
        etmima = findViewById(R.id.etmima);
        TextView show_tv =  (TextView) textView.findViewById(R.id.tishi);
        show_tv.setSelected(true);
        show_tv.setText("    欢迎使用智能温湿度云管端系统!");
        show_tv.setTextColor(Color.WHITE);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(DengLu.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(DengLu.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(psw.equals("1234") && userName.equals("admin")){
                    Toast.makeText(DengLu.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //登录成功后关闭此页面进入主页
                    Intent data=new Intent();
                    //RESULT_OK为Activity系统常量，状态码为-1
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    setResult(RESULT_OK,data);
                    //销毁登录界面
                    DengLu.this.finish();
                    //页面跳转
                    startActivity(new Intent(DengLu.this, MainActivity.class));
                    return;
                }else if(!psw.equals("1234") || !userName.equals("admin")){
                Toast.makeText(DengLu.this, "密码错误或用户名不存在", Toast.LENGTH_SHORT).show();
                return;
            }
            }
        });

    }
    /**
     * 获取控件中的字符串
     */
    private void getEditString(){
        userName=etzhangzhao.getText().toString().trim();
        psw=etmima.getText().toString().trim();

    }

}