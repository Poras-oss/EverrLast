package com.yourcitydate.poras.datingapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourcitydate.poras.datingapp.Profile.AboutUserEdit;
import com.yourcitydate.poras.datingapp.Profile.ProfileEdit;
import com.yourcitydate.poras.datingapp.Profile.ProfilePhotoViewer;
import com.yourcitydate.poras.datingapp.R;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {
    private TextView Name,Age,Bio,likes,dislikes;
    private ImageView editImage;
    CircleImageView profilePic;
    private DatabaseReference databaseReference;
    private String UID;
    private ArrayList<String> data;
    private final String SharedPrefs = "prefs";
    private SharedPreferences sharedPreferences;
    ImageButton dropDownToggle;
    public Profile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefs,getActivity().MODE_PRIVATE);
        UID = sharedPreferences.getString("UID","0");
        data = new ArrayList<>();
        //Assigning views
        Name = (TextView)view.findViewById(R.id.NAME);
        Age = (TextView)view.findViewById(R.id.AGE);
        Bio = (TextView)view.findViewById(R.id.BIO);
        profilePic = (CircleImageView)view.findViewById(R.id.profile_image);
        editImage = (ImageView)view.findViewById(R.id.editImage);
        dropDownToggle = (ImageButton)view.findViewById(R.id.profileDropDown);

        dropDownToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownToggle.animate().rotationXBy(1).rotationBy(1).setDuration(3000).start();
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfileEdit.class);
                startActivity(i);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfilePhotoViewer.class);
                startActivity(i);
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(getActivity(), AboutUserEdit.class);
                startActivity(i);

            }
        });

        getProfileData();
        setProfileImage();
        setValues();

    }

    private void setProfileImage() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("profileImages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Glide.with(getActivity())
                            .load(dataSnapshot.child("one").getValue().toString())
                            .into(profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getProfileData() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fname",dataSnapshot.child("fname").getValue().toString());
                    editor.putString("lname",dataSnapshot.child("lname").getValue().toString());
                    editor.putString("dob",dataSnapshot.child("dob").getValue().toString());
                    editor.putString("bio",dataSnapshot.child("bio").getValue().toString());
                    editor.commit();
                    setValues();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
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

    private void setValues(){

      Name.setText(sharedPreferences.getString("fname","")+" "+sharedPreferences.getString("lname","")+",");
      Bio.setText(sharedPreferences.getString("bio",""));
      if (!sharedPreferences.getString("dob","").equals("")){
          Age.setText(getAge(sharedPreferences.getString("dob","")));
      }
    }

}
