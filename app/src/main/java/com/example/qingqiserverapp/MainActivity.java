package com.example.qingqiserverapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qingqiserverapp.activity.SmsSquare;
import com.example.qingqiserverapp.utils.Constant;
import com.example.qingqiserverapp.utils.JsonUtils;

import java.io.IOException;

import devliving.online.securedpreferencestore.DefaultRecoveryHandler;
import devliving.online.securedpreferencestore.SecuredPreferenceStore;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //下面开始定义各组件
    private EditText token_edit;
    private Button submmit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化组件
        token_edit = (EditText) findViewById(R.id.token);
        submmit_btn = (Button) findViewById(R.id.submmit);
        submmit_btn.setOnClickListener(this);

        //要先对加密开源库进行初始化
        try {
            SecuredPreferenceStore.init(this.getApplicationContext(), new DefaultRecoveryHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submmit:
                //先从token_edit中拿出token文本
                String token = token_edit.getText().toString();
                if (token.isEmpty()){
                    //如果验证口令为空，那么就弹出提示
                    token_edit.setText("");
                    Toast.makeText(this, "验证口令不能为空", Toast.LENGTH_SHORT).show();

                    break;
                }
                //这里是不用验证的，只是为了方便而把token存放起来
                //要注意先把tel和password的值存入SharedPreferences中，我们这里用了一个开源库进行加密
                SecuredPreferenceStore preferenceStore = SecuredPreferenceStore.getSharedInstance();
                preferenceStore.edit().putString("token",token_edit.getText().toString()).apply();
                Log.e("MainActivity", token_edit.getText().toString());
                //然后我们就跳转到展示快递点分区的界面
                Intent intent = new Intent(this, SmsSquare.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


}
