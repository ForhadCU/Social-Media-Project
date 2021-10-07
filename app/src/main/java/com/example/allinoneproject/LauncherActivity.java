package com.example.allinoneproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.Objects;

public class LauncherActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*//hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        //hide ActionBar/AppBar
/*
        Objects.requireNonNull(getSupportActionBar()).hide();
*/
        //hide statusBar | NavigationBar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions); //End
        setContentView(R.layout.activity_launcher);

        progressBar = findViewById(R.id.progressBarID);

        //creating a Thread to set procgress
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mThread();
                mIntent();
            }
        });
        thread.start();



    }

    private void mIntent() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    private void mThread() {
        for (progress = 1; progress <= 100; progress++)
        {
            try {
                Thread.sleep(35);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
