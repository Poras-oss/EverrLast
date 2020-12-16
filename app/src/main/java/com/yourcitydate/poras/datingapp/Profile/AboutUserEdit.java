package com.yourcitydate.poras.datingapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourcitydate.poras.datingapp.R;

import java.util.HashMap;
import java.util.Map;

public class AboutUserEdit extends AppCompatActivity {

    EditText Fname, Lname, About;
    TextInputLayout inputLayoutFname, inputLayoutLname, inputLayoutAbout;
    String fnmae,lname,about,UID;
    private final String SharedPrefs = "prefs";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_user_edit);
        sharedPreferences = getSharedPreferences(SharedPrefs,MODE_PRIVATE);
        UID = sharedPreferences.getString("UID","");


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarabout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadDetails();

        Fname = (EditText)findViewById(R.id.etFname);
        Lname = (EditText)findViewById(R.id.etLname);
        About = (EditText)findViewById(R.id.bio);

        inputLayoutFname = (TextInputLayout)findViewById(R.id.fname_text_input_layout);
        inputLayoutLname = (TextInputLayout)findViewById(R.id.lname_text_input_layout);
        inputLayoutAbout = (TextInputLayout)findViewById(R.id.bio_text_input_layout);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imagemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                // User chose the "Settings" item, show the app settings UI...
                SendDetailsToDatabase();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                finish();
                return super.onOptionsItemSelected(item);
        }
    }

    private void SendDetailsToDatabase() {

        fnmae = Fname.getText().toString();
        lname = Lname.getText().toString();
        about = About.getText().toString();

        if (!validateFname()) {
            return;
        }

        if (!validateLname()) {
            return;
        }

        if (!validateAbout()) {
            return;
        }
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

            Map map = new HashMap<>();
            map.put("fname",fnmae);
            map.put("lname",lname);
            map.put("bio",about);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("fname",fnmae);
            editor.putString("lname",lname);
            editor.putString("bio",about);
            editor.commit();

            databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                }
            });

    }



    private Boolean validateFname(){
        if (TextUtils.isEmpty(fnmae)){
            inputLayoutFname.setError("First Name Required");
            requestFocus(Fname);
            return false;
        }
        return true;
    }
    private Boolean validateLname(){
        if (TextUtils.isEmpty(lname)){
            inputLayoutLname.setError("Last Name required");
            requestFocus(Lname);
            return false;
        }
        return true;
    }
    private Boolean validateAbout() {
        if (TextUtils.isEmpty(about)) {
            about = "";
            return true;
        }
        if (about.length() > 250){
            inputLayoutAbout.setError("Limit reached");
            requestFocus(About);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void loadDetails(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    Fname.setText(dataSnapshot.child("fname").getValue().toString());
                    Lname.setText(dataSnapshot.child("lname").getValue().toString());
                    About.setText(dataSnapshot.child("bio").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}