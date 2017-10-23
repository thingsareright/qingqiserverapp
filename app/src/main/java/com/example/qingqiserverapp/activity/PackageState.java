package com.example.qingqiserverapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qingqiserverapp.R;

public class PackageState extends AppCompatActivity implements View.OnClickListener{

    //设置各种控件
    private Button wait_package_btn;
    private Button get_package_btn;
    private Button unget_package_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_state);

        //初始化各组件
        wait_package_btn = (Button) findViewById(R.id.wait_package);
        get_package_btn = (Button) findViewById(R.id.get_package);
        unget_package_btn = (Button) findViewById(R.id.unget_package);

        wait_package_btn.setOnClickListener(this);
        get_package_btn.setOnClickListener(this);
        unget_package_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //获取上个界面传来的smsaddress
        int smsaddress = getIntent().getIntExtra("smsaddress", -1);
        Log.e("PackageState:smsaddress", String.valueOf(smsaddress));
        if (smsaddress == -1){
            Toast.makeText(this, "请返回上一个界面选择地点", Toast.LENGTH_SHORT).show();
        }

        switch (view.getId()){
            case R.id.wait_package:
                //我们要接收上一个界面传过来的smsaddress，同时给下一个页面发送state
                //为了方便，我在这里使用了一个方法来封装
                navigateToAnotherActivity(smsaddress,0);
                break;
            case R.id.get_package:
                navigateToAnotherActivity(smsaddress,1);
                break;
            case R.id.unget_package:
                navigateToAnotherActivity(smsaddress,2);
                break;
            default:
                break;
        }
    }

    //我们要接收上一个界面传过来的smsaddress，同时给下一个页面发送state
    public void navigateToAnotherActivity(int smsaddress, int state){
        Intent intent = new Intent(this, AllEIofOneStateandSmsaddress.class);
        intent.putExtra("smsaddress", smsaddress);
        Log.e("PackageState", String.valueOf(smsaddress));
        intent.putExtra("state", state);
        Log.e("PackageState", String.valueOf(state));
        startActivity(intent);
    }
}
