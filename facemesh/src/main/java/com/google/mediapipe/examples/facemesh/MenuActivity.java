package com.google.mediapipe.examples.facemesh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.material.tabs.TabLayout;

public class MenuActivity extends AppCompatActivity {

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
    }
}