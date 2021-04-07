package com.yourcitydate.poras.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.yourcitydate.poras.datingapp.Registration.Registration;
import com.yourcitydate.poras.datingapp.fragments.Chat;
import com.yourcitydate.poras.datingapp.fragments.MapsFragment;
import com.yourcitydate.poras.datingapp.fragments.Matches;
import com.yourcitydate.poras.datingapp.fragments.Profile;
import com.yourcitydate.poras.datingapp.utils.Settings;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private  String uid = "null", token="null";
    private final String SharedPrefs = "prefs";
    private SharedPreferences sharedPreferences;
    private DatabaseReference statusRef;
    SharedPreferences.Editor editor;
    //Navigation
    Toolbar toolbar;
    Switch datingModeSwitch;
    static DrawerLayout drawerLayout;
    boolean mode = false;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // APPBAR
        toolbar = (Toolbar) findViewById(R.id.MainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        datingModeSwitch = (Switch)findViewById(R.id.datingModeSwitch);


        // DRAWER STUFF
        drawerLayout = (DrawerLayout) findViewById(R.id.Drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        ImageView sanwichIcon = (ImageView)findViewById(R.id.sandwhich);
        sanwichIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });




        //--------------CHECKING IF USER ALREADY LOGGED IN----------------------------------------------------
        sharedPreferences = getSharedPreferences(SharedPrefs, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (!(user == null)) {
            uid = mAuth.getCurrentUser().getUid();
            statusRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("s");
            statusRef.setValue(true);

            statusRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
        }


        editor = sharedPreferences.edit();
        editor.putString("UID", uid);
        editor.commit();

        if (!uid.equals("null")) {
            //-----------------------------APP MAIN NAVIGATION---------------------------------------------


          navigationView.setCheckedItem(R.id.nav_home);
            navigationView.setNavigationItemSelectedListener(this);

            datingModeSwitch.setChecked(mode);
            datingModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mode = isChecked;
                    if (isChecked){
                        MapsFragment map = new MapsFragment();
                        show(map);
                    }else{
                        Fragment matches = new Matches();
                        show(matches);
                    }

                }
            });




        }







    }


    @Override
    protected void onStart() {
        super.onStart();
        if (user == null) {
            Intent i = new Intent(this, Registration.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //   statusRef.setValue(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
     //   Toast.makeText(getApplication(),"onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  Toast.makeText(getApplication(),"onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (!mode){
                Fragment matches = new Matches();
                show(matches);
            }     else{
                MapsFragment map = new MapsFragment();
                show(map);
            }

            datingModeSwitch.setVisibility(View.VISIBLE);
            datingModeSwitch.setClickable(true);
        } else if (id == R.id.nav_profile) {
            Matches.isOnCurrentFrag = false;
            Fragment profile = new Profile();
            show(profile);
            datingModeSwitch.setVisibility(View.INVISIBLE);
            datingModeSwitch.setClickable(false);
        } else if (id == R.id.nav_chat) {
            Matches.isOnCurrentFrag = false;
            Fragment chat = new Chat();
            show(chat);
            datingModeSwitch.setVisibility(View.INVISIBLE);
            datingModeSwitch.setClickable(false);
        }


            drawerLayout.closeDrawer(GravityCompat.START);
            return true;

    }

    public void show(Fragment frags) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, frags)
                .commit();

        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
