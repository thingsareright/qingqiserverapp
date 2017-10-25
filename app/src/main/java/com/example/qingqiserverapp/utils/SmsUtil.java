package com.example.qingqiserverapp.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/25 0025.
 * 这个模板主要用来向我们的服务器发送请求，进而给用户发送一条通知短信
 */

public class SmsUtil {

    /**
     * 向我们的服务器发送一条请求，请求发送一条短信通知给用户
     * @param token 服务端验证口令，一天一变
     * @param state 要变化到的状态
     * @param tel   电话号码
     * @param awb   物流信息
     * @param name  收件人名字
     * @return
     */
    public static String sendMessage(final String token, int state, String tel, final String awb, final String name) {
        //请求路径
        String url = null;
        //先根据状态判断向哪个路径请求
        switch (state){
            case 1:
                //请求发送已取到货的通知
                url = Constant.getServer() + "/sms/sendgetpackage?token=" + token + "&tel=" +
                        tel + "&param1=" + name + "&param2=" + awb;
                break;
            case 2:
                //请求发送已取到货的通知
                url = Constant.getServer() + "/sms/sendungetpackage?token=" + token + "&tel=" +
                        tel + "&param1=" + name + "&param2=" + awb;
                break;
            case 3:
                //请求发送已取到货的通知
                url = Constant.getServer() + "/sms/senddispatching?token=" + token + "&tel=" +
                        tel + "&param1=" + name + "&param2=" + awb;
                break;
            default:
                break;
        }

        //如果是除了这几种请求，那么我们不发
        if (url == null){
            return "0";
        }
        final String finalUrl = url;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的远程服务器
                            .url(finalUrl).build();
                    Response response = client.newCall(request).execute();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return "1";
    }
}
