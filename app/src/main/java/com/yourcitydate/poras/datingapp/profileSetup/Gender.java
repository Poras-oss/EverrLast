package com.yourcitydate.poras.datingapp.profileSetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourcitydate.poras.datingapp.R;

import java.util.HashMap;
import java.util.Map;

public class Gender extends AppCompatActivity {
    private ImageButton next,back;
    private Button male,female;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        mAuth = FirebaseAuth.getInstance();
        male = (Button)findViewById(R.id.MaleButton);
        female = (Button)findViewById(R.id.FemaleButton);
        next = (ImageButton)findViewById(R.id.Gender_Next);
        back = (ImageButton)findViewById(R.id.Gender_back);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBarGender);
        progressBar.setProgress(50);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please select your gender",Toast.LENGTH_SHORT).show();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendGenderToDatabase(female.getText().toString());
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendGenderToDatabase(male.getText().toString());
            }
        });

    }

    private void SendGenderToDatabase(String Gender){
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();

        Map map = new HashMap<>();
        map.put("gender",Gender);

        reference.child("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(getApplicationContext(),Age.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),"Something went wrong :(",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
