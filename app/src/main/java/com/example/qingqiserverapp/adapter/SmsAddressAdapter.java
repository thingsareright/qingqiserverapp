package com.example.qingqiserverapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qingqiserverapp.R;
import com.example.qingqiserverapp.activity.PackageState;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 这个类用于在地点选择界面中做RecyclerView的适配器
 */

public class SmsAddressAdapter extends RecyclerView.Adapter<SmsAddressAdapter.ViewHolder> {

    private List<String> smsAddressList;

    public SmsAddressAdapter(List<String> smsAddress) {
        this.smsAddressList = smsAddress;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.smsaddress_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //设置recyclerview的每项点击事件
        holder.smsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                //跳转到货物状态选择页面，货物地点编号采用intent传递
                Intent intent = new Intent(parent.getContext(), PackageState.class);
                intent.putExtra("smsaddress", position);
                Log.e("SmsAddressAdapter", String.valueOf(position));
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String smsAddress = smsAddressList.get(position);
        holder.textView.setText(smsAddress);
    }

    @Override
    public int getItemCount() {
        return smsAddressList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View smsView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            smsView = view;
            textView = view.findViewById(R.id.smsaddress_name);
        }
    }


}
