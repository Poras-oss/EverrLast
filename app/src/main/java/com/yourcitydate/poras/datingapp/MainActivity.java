package com.yourcitydate.poras.datingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.yourcitydate.poras.datingapp.Adapters.viewPagerAdapter;
import com.yourcitydate.poras.datingapp.Registration.Registration;
import com.yourcitydate.poras.datingapp.fragments.Chat;
import com.yourcitydate.poras.datingapp.fragments.Matches;
import com.yourcitydate.poras.datingapp.fragments.Profile;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uid="null";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private viewPagerAdapter pagerAdapter;
    private final String SharedPrefs = "prefs";
    private SharedPreferences sharedPreferences;
    private DatabaseReference statusRef;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //--------------CHECKING IF USER ALREADY LOGGED IN----------------------------------------------------
        sharedPreferences = getSharedPreferences(SharedPrefs,MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (!(user == null)){
            uid = mAuth.getCurrentUser().getUid();
            statusRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("s");
            statusRef.setValue(true);

            statusRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UID", uid);
        editor.commit();

        if (!uid.equals("null")){
            //-----------------------------APP MAIN NAVIGATION---------------------------------------------
            getSupportFragmentManager().beginTransaction().replace(R.id.viewPager,new Matches()).commit();
            viewPager = (ViewPager)findViewById(R.id.viewPager);

            pagerAdapter = new viewPagerAdapter(getSupportFragmentManager());

            pagerAdapter.addFragment(new Profile(),"Profile");
            pagerAdapter.addFragment(new Matches(),"Matches");
            pagerAdapter.addFragment(new Chat(),"Chat");

            viewPager.setOffscreenPageLimit(3);

            viewPager.setAdapter(pagerAdapter);
            tabLayout = (TabLayout)findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.selectTab(tabLayout.getTabAt(1));
            setupTabIcons();
        }


    }




    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_user);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_whatshot_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat);
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (user == null){
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
}
