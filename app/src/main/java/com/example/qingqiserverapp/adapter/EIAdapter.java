package com.example.qingqiserverapp.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qingqiserverapp.R;
import com.example.qingqiserverapp.activity.Single_EI_Info;
import com.example.qingqiserverapp.entity.EI;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23 0023.
 */

public class EIAdapter extends RecyclerView.Adapter<EIAdapter.ViewHolder> {

    private  List<EI> eiList;

    public EIAdapter(List<EI> eiList) {
        this.eiList = eiList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_ei_single_item, parent, false);

        //设置点击事件
        final EIAdapter.ViewHolder holder = new ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                EI ei = eiList.get(position);
                //下面开始跳转到单个详细信息页面，注意要传递id
                Intent intent = new Intent(view.getContext(), Single_EI_Info.class);
                intent.putExtra("id", ei.getId());
                Log.e("EIAdapter", ei.getId().toString());

                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EI ei = eiList.get(position);
        holder.awb_text.setText(ei.getAwb());
        holder.name_text.setText(ei.getName());
    }

    @Override
    public int getItemCount() {
        return eiList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView awb_text;
        TextView name_text;

        public ViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            this.awb_text = itemView.findViewById(R.id.awb);
            this.name_text = itemView.findViewById(R.id.name);
        }


    }
}
