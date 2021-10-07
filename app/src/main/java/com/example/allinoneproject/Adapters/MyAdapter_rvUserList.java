package com.example.allinoneproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter_rvUserList extends RecyclerView.Adapter<MyAdapter_rvUserList.MyViewHolder> {
    private List<User_data_handler> userDataHandlerList;
    private Context context;

    public MyAdapter_rvUserList(List<User_data_handler> userDataHandlerList, Context context) {
        this.userDataHandlerList = userDataHandlerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_allusers_custm_lay, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User_data_handler current = userDataHandlerList.get(position);
        holder.vTextView.setText(current.getUserName());

        if (current.getStatus().equals("online"))
        {
            holder.img_on.setVisibility(View.VISIBLE);
            holder.img_off.setVisibility(View.GONE);
        }
        else
        {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
//        return userDataHandlerList.size()-(userDataHandlerList.size()/2);
//        return counter;
        return userDataHandlerList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView vTextView;
        ImageView img_on;
        ImageView img_off;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vTextView = itemView.findViewById(R.id.tv_userNameId);
            img_on = itemView.findViewById(R.id.img_onID);
            img_off = itemView.findViewById(R.id.img_offID);

        }
    }
}
