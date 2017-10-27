package com.example.qingqiserverapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.qingqiserverapp.R;
import com.example.qingqiserverapp.adapter.SmsAddressAdapter;

import java.util.ArrayList;
import java.util.List;

public class SmsSquare extends AppCompatActivity {

    private static List<String> smsAddressList = new ArrayList<>();

    static {
        smsAddressList.add(0,"西门");
        smsAddressList.add(1,"东门");
        smsAddressList.add(2,"南门");
        smsAddressList.add(3,"菊园二号楼分拣区");
        smsAddressList.add(4,"菊园二号楼自助（东）");
        smsAddressList.add(5,"菊园二号楼自助（北）");
        smsAddressList.add(6,"菊园二号楼顺丰专区");
        smsAddressList.add(7,"菊园二号楼2-2延时区");
        smsAddressList.add(8,"菊园二号楼前台");
        smsAddressList.add(9,"京东快递");
        smsAddressList.add(10,"菊园五号楼自助一区");
        smsAddressList.add(11,"菊园五号楼自助二区");
        smsAddressList.add(12,"菊园五号楼分拣区");
        smsAddressList.add(13,"荷园洗浴中心二楼");
        smsAddressList.add(14,"其它");
    }

    private SmsAddressAdapter smsAddressAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_square);

        //对RecyclerView进行初始化
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        smsAddressAdapter = new SmsAddressAdapter(smsAddressList);
        recyclerView.setAdapter(smsAddressAdapter);
    }
}
