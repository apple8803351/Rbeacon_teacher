package com.example.user.rbeacon_teacher;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, StudentMainActivity.class);
        startActivity(intent);
/*
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        },1000);*/
    }
}
