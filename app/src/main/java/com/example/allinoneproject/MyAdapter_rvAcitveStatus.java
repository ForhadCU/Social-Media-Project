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

public class MyAdapter_rvAcitveStatus extends RecyclerView.Adapter<MyAdapter_rvAcitveStatus.MyViewHolder> {
    private List<User_data_handler> userDataHandlerList;
    private Context context;
    private View view;

    public MyAdapter_rvAcitveStatus(List<User_data_handler> userDataHandlerList, Context context) {
        this.userDataHandlerList = userDataHandlerList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_allusers_custm_lay, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User_data_handler current = userDataHandlerList.get(position);
        if (current.getStatus().equals("online"))
        {
            holder.vName.setText(current.getUserName());
            holder.vActiveStatus.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.vName.setText("");
            holder.vActiveStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userDataHandlerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView vName;
        ImageView vActiveStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            vName = itemView.findViewById(R.id.tv_userNameId);
            vActiveStatus = itemView.findViewById(R.id.img_onID);
        }
    }
}
