package com.yourcitydate.poras.datingapp.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourcitydate.poras.datingapp.Adapters.MatchedUserAdapter;
import com.yourcitydate.poras.datingapp.DataCaching.MatchesEntity;
import com.yourcitydate.poras.datingapp.DataCaching.MatchesViewModel;
import com.yourcitydate.poras.datingapp.Models.matchedUsers;
import com.yourcitydate.poras.datingapp.R;

import java.util.ArrayList;
import java.util.List;


public class matches_chat extends Fragment {

    String UID, ID = "blank";
    final String SharedPrefs = "prefs";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference databaseReference, newmsgref;
    List<MatchesEntity> matches;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    boolean isMatchedUserExists = false;
    // Data caching MVVM architecture
    private MatchesViewModel viewModel;

    public matches_chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPreferences = getActivity().getSharedPreferences(SharedPrefs, getActivity().MODE_PRIVATE);
        UID = sharedPreferences.getString("UID", "0");
        isMatchedUserExists = sharedPreferences.getBoolean("isMatchedUsersExist",false);
        editor = sharedPreferences.edit();
        viewModel = ViewModelProviders.of(this).get(MatchesViewModel.class);



        recyclerView = (RecyclerView) view.findViewById(R.id.userMatches);

        if (isMatchedUserExists){
            loadFromCache();
             listenToNewMessages();
        }else{
            LoadMatchesIds();
        }




    }

    private void LoadMatchesIds() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("matches");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot matchid : dataSnapshot.getChildren()) {
                    loadMatches(matchid.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadFromCache(){

        viewModel.getAllMatchedUsers().observe(getViewLifecycleOwner(), new Observer<List<MatchesEntity>>() {
           @Override
           public void onChanged(List<MatchesEntity> matchesEntities) {
               matches = matchesEntities;
              setAdapter(matchesEntities);
           }
       });



    }

    private void loadMatches(final String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //copy this
                    MatchesEntity mUsers = new MatchesEntity(id, dataSnapshot.child("fname").getValue().toString() + " " + dataSnapshot.child("lname").getValue().toString(), dataSnapshot.child("profileImages").child("one").getValue().toString());
                    viewModel.insert(mUsers);
                    editor.putBoolean("isMatchedUsersExist",true);
                    editor.commit();
                    loadFromCache();
                }else{
                    return;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Something went wrong!", Toast.LENGTH_SHORT).show();
            }


        });



    }

    private void setAdapter(List<MatchesEntity> stuff) {
        adapter = new MatchedUserAdapter(getActivity(), stuff, ID);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        recyclerView.setAdapter(adapter);
    }


    private void listenToNewMessages() {
        //--------------LISTENING TO NEW MESSAGES---------------------------------------------------------

        newmsgref = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("is");
        newmsgref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue().toString().equals("n")) {
                        //Sending the ID into adapter
                        ID = ds.getKey();
                        setAdapter(matches);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null){
            setAdapter(matches);
        }

    }
}