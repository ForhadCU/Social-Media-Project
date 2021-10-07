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

public class OnlineStatusActivity extends AppCompatActivity {
    private static final String TAG = "OnlineStatusActivity";
    private static final String KEY_STATUS = "status";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MyAdapter_rvAcitveStatus myAdapter_rvAcitveStatus;
    private List<User_data_handler> userDataHandlerList;
    private String currentUid;

    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestoreDB.collection("USERS");
    private FirebaseAuth mAuth;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_status);

        this.setTitle("Active");
        toolbar = findViewById(R.id.appBarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.rv_onlineStatusID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    protected void onStart() {
        super.onStart();
        mRetrieveUserStatus();
    }

    private void mRetrieveUserStatus() {
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                {
                    Toast.makeText(OnlineStatusActivity.this, "Error in Loading, check logd", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onEvent: "+error.toString());
                }
                mClearAll();
                for (QueryDocumentSnapshot documentSnapshot: value)
                {
                    User_data_handler list = documentSnapshot.toObject(User_data_handler.class);
                    userDataHandlerList.add(list);
                }
                myAdapter_rvAcitveStatus = new MyAdapter_rvAcitveStatus(userDataHandlerList, getApplicationContext());
                recyclerView.setAdapter(myAdapter_rvAcitveStatus);
            }
        });
    }

    private void mClearAll() {
        if (userDataHandlerList != null)
        {
            userDataHandlerList.clear();
            if (myAdapter_rvAcitveStatus != null)
            {
                myAdapter_rvAcitveStatus.notifyDataSetChanged();
            }
        }
        userDataHandlerList = new ArrayList<>();
    }

    void statusMehtod(String status)
    {
        docRef = firestoreDB.collection("USERS").document(currentUid);

        Map<String, Object> map = new HashMap<>();
        map.put(KEY_STATUS, status);
        docRef.update(map);


    }

    @Override
    protected void onResume() {
        super.onResume();
        statusMehtod("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        statusMehtod("offline");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }
}
