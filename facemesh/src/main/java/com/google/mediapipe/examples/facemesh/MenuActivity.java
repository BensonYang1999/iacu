package com.google.mediapipe.examples.facemesh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {
    GlobalVariable gv = GlobalVariable.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btn_learning = (Button) findViewById(R.id.btn_learning);
        btn_learning.setOnClickListener((v) ->{
            Intent intent = new Intent();
            intent.setClass(MenuActivity.this, LearningActivity.class);
            startActivity(intent);
        });

        Button btn_diag = (Button) findViewById(R.id.btn_diag);
        btn_diag.setOnClickListener((v) ->{
            Intent intent = new Intent();
            intent.setClass(MenuActivity.this, DiagnosisActivity.class);
            startActivity(intent);
        });

        try {
            gv.setAcuJson(loadJSONFromAsset("acupoint.json"));
            gv.setDisJson(loadJSONFromAsset("disease_to_acu.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public String loadJSONFromAsset(String filename) {
        String json_data = null;
        try {
            InputStream inputStream = this.getAssets().open(filename);
            int size = inputStream.available();
            byte buffer[] = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json_data = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json_data;
    }
}