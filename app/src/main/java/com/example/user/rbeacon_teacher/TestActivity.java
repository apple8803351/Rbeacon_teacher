package com.example.user.rbeacon_teacher;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class TestActivity extends AppCompatActivity {

    Button testbutton;
    TextView testtextView;
    OkHttpClient client = new OkHttpClient();

    private String path = "http://172.30.4.231:8080/SQL/student.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_class_table);

    }
}
