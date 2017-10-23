package com.example.qingqiserverapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qingqiserverapp.R;
import com.example.qingqiserverapp.entity.EI;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23 0023.
 */

public class EIAdapter extends RecyclerView.Adapter<EIAdapter.ViewHolder> {

    private List<EI> eiList;

    public EIAdapter(List<EI> eiList) {
        this.eiList = eiList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_ei_single_item, parent, false);
        EIAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
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

        TextView awb_text;
        TextView name_text;

        public ViewHolder(View itemView) {
            super(itemView);
            this.awb_text = itemView.findViewById(R.id.awb);
            this.name_text = itemView.findViewById(R.id.name);
        }


    }
}
