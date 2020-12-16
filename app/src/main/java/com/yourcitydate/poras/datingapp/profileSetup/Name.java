package com.yourcitydate.poras.datingapp.profileSetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourcitydate.poras.datingapp.R;

import java.util.HashMap;
import java.util.Map;

public class Name extends AppCompatActivity {
    private EditText fname,lname;
    private ImageButton imageButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        mAuth = FirebaseAuth.getInstance();
        fname = (EditText)findViewById(R.id.firstname);
        lname = (EditText)findViewById(R.id.lastname);
        imageButton = (ImageButton)findViewById(R.id.Name_Next);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBarName);
        progressBar.setProgress(25);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (lname.getText().toString().matches("") || fname.getText().toString().matches("")){
                   Snackbar snackbar = Snackbar.make(v,"Name cant be blank!",Snackbar.LENGTH_SHORT);
                   snackbar.show();
               }else{
                   sendNameToDatabase(fname.getText().toString(),lname.getText().toString());
               }
            }
        });
    }

    private void sendNameToDatabase(String fname, String lname) {
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();

        Map<String,String> map = new HashMap<>();
        map.put("fname",fname);
        map.put("lname",lname);

        reference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(getApplicationContext(),Gender.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),"Something went wrong :( \n Try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
