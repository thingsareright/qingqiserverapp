package com.example.qingqiserverapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.qingqiserverapp.R;
import com.example.qingqiserverapp.adapter.EIAdapter;
import com.example.qingqiserverapp.entity.EI;
import com.example.qingqiserverapp.utils.Constant;
import com.example.qingqiserverapp.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import devliving.online.securedpreferencestore.DefaultRecoveryHandler;
import devliving.online.securedpreferencestore.SecuredPreferenceStore;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllEIofOneStateandSmsaddress extends AppCompatActivity {

    public List<EI> eiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_eiof_one_stateand_smsaddress);

        //下面开始取出token
        //要先对加密开源库进行初始化
        try {
            SecuredPreferenceStore.init(this.getApplicationContext(), new DefaultRecoveryHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //要注意先把tel和password的值存入SharedPreferences中，我们这里用了一个开源库进行加密
        SecuredPreferenceStore preferenceStore = SecuredPreferenceStore.getSharedInstance();
        String token = preferenceStore.getString("token","0");
        Log.e("AllEI:token", token);
        //取得smsaddress与state
        Long smsAddress = new Long(getIntent().getIntExtra("smsaddress",-1));
        Long state = new Long(getIntent().getIntExtra("state", -1));
        Log.e("AllEI:smsaddress", smsAddress.toString());
        Log.e("AllEI:state", state.toString());
        //如果无效，提示让他返回上个界面
        //TODO long型对象的好多方法都没有被重写，要特别注意
        if (smsAddress == -1 || state == -1)
            return;
        if (token.equals("0")) {
            //如果无效，或者等于"0"，那么我们就不做处理
        } else {
            //否则我们发送网络请求
            String url = Constant.getServer() + "/ei/getEIofOneState?token=" + token + "&state=" + state + "&smsaddress=" + smsAddress;
            Log.e("AllEI", url);
            sendRequestWithOkHttp(url);
        }
    }

    //这是一个网络请求的方法
    private void sendRequestWithOkHttp(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //发送请求，获得数字
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的远程服务器
                            .url(url).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    response.body().close();
                    eiList = JsonUtils.parseEIListWithGSON(responseData);
                    //在下面这个方法中进行界面更新
                    doWithUi();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 这个方法进行界面更新
     */
    private void doWithUi() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //先判断eiList是否为空
                if (eiList.isEmpty()){
                    Toast.makeText(getApplicationContext(), "数据请求错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_all_ei);
                Log.e("AllEI", eiList.get(0).toString());
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());   //TODO不知有没有错误
                recyclerView.setLayoutManager(layoutManager);
                EIAdapter eiAdapter =new EIAdapter(eiList);
                recyclerView.setAdapter(eiAdapter);
            }
        });

    }
}
