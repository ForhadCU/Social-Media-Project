package com.example.allinoneproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.allinoneproject.Interfaces.IMainActivityCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter_rvAllpost extends RecyclerView.Adapter<MyAdapter_rvAllpost.MyViewHolder> {
    private List<Data_Handler> dataHandlerList;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private ArrayList<String> uriList;
    private ArrayList<String> mainImgUriList;
    private Nested_rv_Adapter nested_rv_adapter;

    private IMainActivityCallback callback;


    public MyAdapter_rvAllpost(List<Data_Handler> dataHandlerList, Context context, IMainActivityCallback callback) {
        this.dataHandlerList = dataHandlerList;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_allpost_custm_lay, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Parent RV Position: " + myViewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Data_Handler current = dataHandlerList.get(position);
        holder.vCurrentUserName.setText(current.getCurrentUserName());
        holder.vDesc.setText(current.getDesc());

        holder.currentItemUId = current.getCurrentUid();
        holder.currentItemDocId = current.getDocumentID();

        String currentUid = mAuth.getCurrentUser().getUid();
        if (holder.currentItemUId.equals(currentUid))
        {
            holder.vDeleteBtn.setVisibility(View.VISIBLE);
            holder.vEditBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.vDeleteBtn.setVisibility(View.GONE);
            holder.vEditBtn.setVisibility(View.GONE);
        }


        //Go to nested recyclerView
        uriList = new ArrayList<>();
        mainImgUriList = new ArrayList<>();

        holder.vRecyclerView.setHasFixedSize(true);
        holder.vRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        uriList = current.getThumbUriList();
        mainImgUriList = current.getUriList();
        nested_rv_adapter = new Nested_rv_Adapter(uriList, mainImgUriList, context, callback);
        holder.vRecyclerView.setAdapter(nested_rv_adapter);
        nested_rv_adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataHandlerList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vCurrentUserName, vDesc;
        ImageView vDeleteBtn;
        RecyclerView vRecyclerView;
        ImageView vEditBtn;

        String currentItemDocId;
        String currentItemUId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vCurrentUserName = itemView.findViewById(R.id.tvUserNameId);
            vDesc = itemView.findViewById(R.id.tvDescID);
            vDeleteBtn = itemView.findViewById(R.id.imgBtn_deletePostID);
            vRecyclerView = itemView.findViewById(R.id.rv_nestedId);
            vEditBtn = itemView.findViewById(R.id.imgBtn_editPostId);

            vDeleteBtn.setOnClickListener(this);
            vEditBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgBtn_deletePostID:
                        mDelete(getAdapterPosition());
                        DocumentReference documentReference = firestoreDB.collection("Post_Collection").document(currentItemDocId);
                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    break;

                case R.id.imgBtn_editPostId:
                        String currentItemDesc = vDesc.getText().toString();
                        callback.iEditPostDesc(currentItemDesc, currentItemDocId);
                    break;
            }
        }
    }

    private void mDelete(int adapterPosition) {
        dataHandlerList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }
}
