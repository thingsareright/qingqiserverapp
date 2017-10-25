package com.example.qingqiserverapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qingqiserverapp.R;
import com.example.qingqiserverapp.entity.EI;
import com.example.qingqiserverapp.utils.Constant;
import com.example.qingqiserverapp.utils.JsonUtils;
import com.example.qingqiserverapp.utils.SmsUtil;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;

import devliving.online.securedpreferencestore.DefaultRecoveryHandler;
import devliving.online.securedpreferencestore.SecuredPreferenceStore;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.GONE;

public class Single_EI_Info extends AppCompatActivity implements View.OnClickListener{

    //定义控件
    private TextView awb_state; //物流编号和状态
    private TextView tel;       //电话号码
    private TextView sms;       //短信内容
    private TextView smsaddress;    //短信里的取货地址
    private TextView name;  //收件人姓名
    private TextView address;   //收件人收货地址
    private Button get_package;
    private Button unget_package;
    private Button dispatching_package;
    private Button dispatched_package;

    //必要的全局变量
    private long id;        //EI数据的id
    private String token; //口令
    private EI ei;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single__ei__info);

        //初始化各控件
        awb_state = (TextView) findViewById(R.id.awb_state);
        tel = (TextView) findViewById(R.id.tel);
        sms = (TextView) findViewById(R.id.sms);
        smsaddress = (TextView) findViewById(R.id.smsaddress);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        get_package = (Button) findViewById(R.id.get_package);
        unget_package = (Button) findViewById(R.id.unget_package);
        dispatching_package = (Button) findViewById(R.id.dispatching_package);
        dispatched_package = (Button) findViewById(R.id.dispatched_package);
        get_package.setOnClickListener(this);
        unget_package.setOnClickListener(this);
        dispatching_package.setOnClickListener(this);
        dispatched_package.setOnClickListener(this);


        id = getIntent().getLongExtra("id",1);
        Log.e("Single", String.valueOf(id));
        //获得token
        //要先对加密开源库进行初始化
        try {
            SecuredPreferenceStore.init(this.getApplicationContext(), new DefaultRecoveryHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        token = SecuredPreferenceStore.getSharedInstance().getString("token", "000");
        if (token.equals("000")){
            Toast.makeText(this, "口令错误", Toast.LENGTH_SHORT).show();
            return;
        }


        //调用网络方法，获取信息
        sendRequestForEIInfo(token, id);
    }

    /**
     * 获取ei的信息
     * @param token
     * @param id
     */
    private void sendRequestForEIInfo(final String token, final long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = Constant.getServer() + "/ei/getSingleEIDataByIdWithToken?token=" + token + "&id=" + id;
                    //发送请求，获得数字
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的远程服务器
                            .url(url).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    response.body().close();
                    ei = JsonUtils.parseEIWithGSON(responseData);
                    //在下面这个方法中进行界面更新
                    doWithUi(ei);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void doWithUi(final EI ei) {
        //获取信息后对视图进行操作
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ei == null)
                    return;

                //根据ei数据来填充UI
                awb_state.setText("物流单号：" + ei.getAwb() + "   （" + Constant.stateString.get(ei.getState().intValue()) +"）");
                tel.setText("电话号码：" + ei.getTel());
                sms.setText("短信信息：" + ei.getSms());
                name.setText("收件人姓名：" + ei.getName());
                Log.e("*******************", ei.getId().toString());
                smsaddress.setText("取货地址（快递被快递公司送到的位置）：" + Constant.smsAddress.get(ei.getSmsaddress().intValue()));
                address.setText("您指定的收货地址： " + ei.getAddress());
                doWithButton(ei.getState().intValue());    //依据状态对按钮进行UI变动
            }
        });
    }

    private void doWithButton(int state) {
        //对按钮的隐藏与显示进行操作
        switch (state){
            case 0:
                get_package.setVisibility(View.VISIBLE);
                unget_package.setVisibility(View.VISIBLE);
                dispatching_package.setVisibility(GONE);
                dispatched_package.setVisibility(GONE);
                break;
            case 1:
                get_package.setVisibility(GONE);
                unget_package.setVisibility(GONE);
                dispatching_package.setVisibility(View.VISIBLE);
                dispatched_package.setVisibility(View.GONE);
                break;
            case 2:
                get_package.setVisibility(GONE);
                unget_package.setVisibility(GONE);
                dispatching_package.setVisibility(GONE);
                dispatched_package.setVisibility(GONE);
                break;
            case 3:
                get_package.setVisibility(GONE);
                unget_package.setVisibility(GONE);
                dispatching_package.setVisibility(View.GONE);
                dispatched_package.setVisibility(View.VISIBLE);
                break;
            case 4:
                get_package.setVisibility(GONE);
                unget_package.setVisibility(GONE);
                dispatching_package.setVisibility(View.GONE);
                dispatched_package.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        //设置各按钮的点击事件
        switch (view.getId()){
            case R.id.get_package:
                //发送网络请求更改ei状态
                sendRequestChangeEIState(id,1);
                break;
            case R.id.unget_package:
                //发送网络请求更改ei状态
                sendRequestChangeEIState(id,2);
                break;
            case R.id.dispatching_package:
                //发送网络请求更改ei状态
                sendRequestChangeEIState(id,3);
                break;
            case R.id.dispatched_package:
                //发送网络请求更改ei状态
                sendRequestChangeEIState(id,4);
                break;
        }
    }

    /**
     * 发送改变状态的请求
     * @param id
     * @param state
     */
    private void sendRequestChangeEIState(final long id, final int state) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = Constant.getServer() + "/ei/setEIofOneState?token=" + token + "&id=" + id + "&state=" + state;
                    //发送请求，获得数字
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的远程服务器
                            .url(url).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    response.body().close();
                    if (responseData.equals("1")){
                        //在下面这个方法中进行界面更新
                        if (ei != null){
                            //向服务端请求发送一条通知短信
                            SmsUtil.sendMessage(token,state,ei.getTel(),ei.getAwb(),ei.getName());
                            doWithUiStateChange(ei.getSmsaddress().intValue());
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Single_EI_Info.this, "信息更改失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    /**
     * 请求发送之后改变UI，其实是实现界面活动跳转
     * @param smsaddress
     */
    private void doWithUiStateChange(final int smsaddress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //跳转到PackageState的活动
                Intent intent = new Intent(Single_EI_Info.this, PackageState.class);
                intent.putExtra("smsaddress", smsaddress);
                Log.e("SmsAddressAdapter", String.valueOf(smsaddress));
                startActivity(intent);
            }
        });
    }
}
