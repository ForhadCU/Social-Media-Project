package com.example.allinoneproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersActivity extends AppCompatActivity {
    private static final String TAG = "UsersActivity";
    private static final String KEY_STATUS = "status";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MyAdapter_rvUserList myAdapter_rvUserList;
    private List<User_data_handler> userDataHandlerList;

    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestoreDB.collection("USERS");
    private FirebaseAuth mAuth;
    private String currentUid;
    private DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        this.setTitle("Users");
        toolbar = findViewById(R.id.appBarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.rv_usersID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userDataHandlerList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRetrieveUsers();
    }

    private void mRetrieveUsers() {
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(UsersActivity.this, "Error in Loading, check logd", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onEvent: " + error.toString());
                }
                mCLearAll();

                for (QueryDocumentSnapshot documentSnapshot : value) {
                    User_data_handler user_data_handler = documentSnapshot.toObject(User_data_handler.class);
                    userDataHandlerList.add(user_data_handler);
                }
                myAdapter_rvUserList = new MyAdapter_rvUserList(userDataHandlerList, getApplicationContext());
                recyclerView.setAdapter(myAdapter_rvUserList);
            }
        });
/*
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null)
                {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                    {
                        User_data_handler user_data_handler = documentSnapshot.toObject(User_data_handler.class);

                    }
                }
            }
        });
*/
    }

    private void mCLearAll() {
        if (userDataHandlerList != null) {
            userDataHandlerList.clear();
            if (myAdapter_rvUserList != null) {
                myAdapter_rvUserList.notifyDataSetChanged();
            }
        }
        userDataHandlerList = new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }

    //Online, Offline Method
    private void statusMethod(String status) {
        userDocRef = firestoreDB.collection("USERS").document(currentUid);
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_STATUS, status);
        userDocRef.update(map);
//        userDocRef.update(KEY_STATUS, status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        statusMethod("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        statusMethod("offline");
    }
}
