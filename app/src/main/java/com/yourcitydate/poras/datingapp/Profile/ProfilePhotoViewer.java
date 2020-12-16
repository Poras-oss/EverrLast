package com.yourcitydate.poras.datingapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.yourcitydate.poras.datingapp.Adapters.ImageSliderAdapter;
import com.yourcitydate.poras.datingapp.Models.SliderItem;
import com.yourcitydate.poras.datingapp.R;

import java.util.Map;

public class ProfilePhotoViewer extends AppCompatActivity {
    SliderView sliderView;
    ImageSliderAdapter adapter;
    Toolbar toolbar;
    String SharedPrefs = "prefs",UID;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo_viewer);
        sharedPreferences = getSharedPreferences(SharedPrefs,MODE_PRIVATE);
        UID = sharedPreferences.getString("UID","0");

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ProfileEdit.class);
                startActivity(i);
                finish();
            }
        });
        toolbar = (Toolbar)findViewById(R.id.imgtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sliderView = (SliderView)findViewById(R.id.imageslider);

        loadImageSlider();

        sliderView.setIndicatorAnimation(IndicatorAnimations.SCALE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.WHITE);

        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        adapter = new ImageSliderAdapter(this);
    }

    private void loadImageSlider() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("profileImages");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("one") != null){
                        adapter.addItem(new SliderItem(map.get("one").toString()));
                    }
                    if (map.get("two") != null){
                        adapter.addItem(new SliderItem(map.get("two").toString()));
                    }
                    if (map.get("three") != null){
                        adapter.addItem(new SliderItem(map.get("three").toString()));
                    }
                    if (map.get("four") != null){
                        adapter.addItem(new SliderItem(map.get("four").toString()));
                    }
                    if (map.get("five") != null){
                        adapter.addItem(new SliderItem(map.get("five").toString()));
                    }
                    if (map.get("six") != null){
                        adapter.addItem(new SliderItem(map.get("six").toString()));
                    }
                    if (map.get("seven") != null){
                        adapter.addItem(new SliderItem(map.get("seven").toString()));
                    }
                    if (map.get("eight") != null){
                        adapter.addItem(new SliderItem(map.get("eight").toString()));
                    }
                    if (map.get("nine") != null){
                        adapter.addItem(new SliderItem(map.get("nine").toString()));
                    }

                    sliderView.setSliderAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something went wrong :(",Toast.LENGTH_LONG).show();            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);

    }
}