package com.yourcitydate.poras.datingapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Calendar;
import java.util.Map;

public class profileViewer extends AppCompatActivity {
     String anotherUserId,UID;
     DatabaseReference databaseReference;
     String SharedPrefs = "prefs";
     SharedPreferences sharedPreferences;
     TextView nameage,BIO;
    SliderView sliderView;
    ImageSliderAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_viewer);

       /* toolbar = (Toolbar)findViewById(R.id.userProfileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        sharedPreferences = getSharedPreferences(SharedPrefs,MODE_PRIVATE);
        UID = sharedPreferences.getString("UID","0");
        anotherUserId = getIntent().getStringExtra("UID");
        assignViews();
    }
    private void getData(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(anotherUserId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fname = null, lname = null, age=null, bio=null;
                if (dataSnapshot.exists()){
                    fname = dataSnapshot.child("fname").getValue().toString();
                    lname = dataSnapshot.child("lname").getValue().toString();
                    age = dataSnapshot.child("dob").getValue().toString();
                    bio = dataSnapshot.child("bio").getValue().toString();
                }
                setData(fname,lname,age,bio);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void setData(String fname, String lname, String age, String bio){
        nameage.setText(fname+" "+lname+", "+getAge(age));
        BIO.setText(bio);
    }
    private void assignViews(){
        nameage = (TextView)findViewById(R.id.Name);
        BIO = (TextView)findViewById(R.id.userBio);
        sliderView = (SliderView)findViewById(R.id.userImageSlider);

        loadImageSlider();

        sliderView.setIndicatorAnimation(IndicatorAnimations.SCALE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.WHITE);

        adapter = new ImageSliderAdapter(this);
        getData();
    }
    private void loadImageSlider() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(anotherUserId).child("profileImages");

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
    private  String getAge(String date){

        String[] parts = date.split("/");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];


        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}