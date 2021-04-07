package com.yourcitydate.poras.datingapp.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.yourcitydate.poras.datingapp.MainActivity;
import com.yourcitydate.poras.datingapp.R;

public class Settings extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
         toolbar = (Toolbar)findViewById(R.id.settings_toolbar);
         setSupportActionBar(toolbar);
         getSupportActionBar().setTitle("");

         ImageButton imageButton = (ImageButton)findViewById(R.id.settingsBackBtn);
         imageButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(getApplicationContext(), MainActivity.class);
                 startActivity(i);
                 finish();
                 overridePendingTransition(R.anim.slide_up,R.anim.slide_up_in);
             }
         });

    }

}