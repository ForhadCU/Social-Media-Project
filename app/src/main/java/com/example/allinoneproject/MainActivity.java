package com.example.allinoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity"; //type logt
    private static final String KEY_STATUS = "status";
    private FirebaseAuth.AuthStateListener mAuthSatateListener;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
//    private DocumentReference firestoreDBref = firestoreDB.document("Posts/Posts docs");
    private CollectionReference collectionReference = firestoreDB.collection("Post_Collection");
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "desc";
    private TextView showpostTextView;
    private String currentUid;
    private String userDocID;
    private DocumentReference userDocRef;  //ref

    private RecyclerView recyclerView;
    private List<Data_Handler> dataHandlerList;
    private MyAdapter_rvAllpost myAdapter_rvAllpost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Home");

        mAuth = FirebaseAuth.getInstance();
        //start Auth

        mAuthSatateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //check user sign in
                if (mAuth.getCurrentUser() == null)
                {
                    Intent signinIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    signinIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signinIntent);
                }
                else
                {
                    currentUid = mAuth.getCurrentUser().getUid();
                }

            }
        };//End

        toolbar = findViewById(R.id.toolBarID);
        setSupportActionBar(toolbar);
//        showpostTextView = findViewById(R.id.tv_showPostID);
        drawerLayout = findViewById(R.id.drawerLayID);
        navigationView = findViewById(R.id.navViewId);
        recyclerView = findViewById(R.id.rv_allPostID);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        dataHandlerList = new ArrayList<>();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    //First of all, check ur user signed in or not
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthSatateListener);
        mShowAllPosts();
//        mGetUserDocID();
    }

/*    private void mGetUserDocID() {
        firestoreDB.collection("USERS").document(currentUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userDocID = documentSnapshot.getId();
                Toast.makeText(getApplicationContext(), userDocID, Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private void mShowAllPosts() {
        mClearAll();
/*
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                String data = "";
                dataHandlerList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {

                    Data_Handler list = documentSnapshot.toObject(Data_Handler.class);
                    dataHandlerList.add(list);

                }
                myAdapter_rvAllpost = new MyAdapter_rvAllpost(dataHandlerList);
                recyclerView.setAdapter(myAdapter_rvAllpost);
//                showpostTextView.setText(data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }
        }); //it's also work
*/

        //very Real Time
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot documentSnapshot : value)
                {
                    Data_Handler data_handler = documentSnapshot.toObject(Data_Handler.class);
                    dataHandlerList.add(data_handler);
                }
                myAdapter_rvAllpost = new MyAdapter_rvAllpost(dataHandlerList);
                recyclerView.setAdapter(myAdapter_rvAllpost);
            }
        });
    }

    private void mClearAll() {
        if (dataHandlerList != null){
            dataHandlerList.clear();
            if (myAdapter_rvAllpost != null)
                myAdapter_rvAllpost.notifyDataSetChanged();
        }
        dataHandlerList = new ArrayList<>();
    }

    private void statusMethod(String status)
    {
//        userDocRef.update(KEY_STATUS, status);
        userDocRef = firestoreDB.collection("USERS").document(currentUid);
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_STATUS, status);
        userDocRef.update(map);

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            statusMethod("online");

        }catch (Exception e)
        {
            Log.d(TAG, "onResume: " + e.toString());;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            statusMethod("offline");
        }catch (Exception e)
        {
            Log.d(TAG, "onPause: "+ e.toString());
        }

    }

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusMethod("offline");
    }*/

  /*  @Override
    protected void onStop() {
        super.onStop();
        statusMethod("offline");
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settingID:
                Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.postID:
                startActivity(new Intent(getApplicationContext(), PostActivity.class));
                break;

            case R.id.onlineID:
                startActivity(new Intent(getApplicationContext(), OnlineStatusActivity.class));
                break;

            case R.id.userID:
                startActivity(new Intent(getApplicationContext(), UsersActivity.class));
                break;

            case R.id.signOutID:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

        }
        return false;
    }


}
