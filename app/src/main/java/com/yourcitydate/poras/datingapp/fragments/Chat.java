package com.yourcitydate.poras.datingapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourcitydate.poras.datingapp.Adapters.MatchedUserAdapter;
import com.yourcitydate.poras.datingapp.Models.matchedUsers;
import com.yourcitydate.poras.datingapp.R;

import java.util.ArrayList;
import java.util.List;


public class Chat extends Fragment {
     String UID;
     final String SharedPrefs = "prefs";
     SharedPreferences sharedPreferences;
     DatabaseReference databaseReference, newmsgref;
     List<matchedUsers> matches = new ArrayList<>();
     RecyclerView recyclerView;
     RecyclerView.Adapter adapter;



    public Chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefs,getActivity().MODE_PRIVATE);
        UID = sharedPreferences.getString("UID","0");
        recyclerView = (RecyclerView)view.findViewById(R.id.userMatches);
       LoadMatchesIds();

       //--------------LISTENING TO NEW MESSAGES---------------------------------------------------------

        newmsgref = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("is");
        newmsgref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    Log.d("NASTYNIGGA", "onChildAdded: "+dataSnapshot.getKey());
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    Log.d("NASTYNIGGA", "onChildChanged: "+dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void LoadMatchesIds(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("matches");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot matchid: dataSnapshot.getChildren()){
                    loadMatches(matchid.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startListeningForNewMessages() {
        Log.d("NASTYNIGGA", "startListeningForNewMessages: "+UID);
           }


    private void loadMatches(final String id) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        //copy this
                            matchedUsers mUsers = new matchedUsers(id,dataSnapshot.child("fname").getValue().toString()+" "+dataSnapshot.child("lname").getValue().toString(),dataSnapshot.child("profileImages").child("one").getValue().toString());
                            matches.add(mUsers);
                           setAdapter();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });



        }

    private void setAdapter() {
        adapter = new MatchedUserAdapter(getActivity(),matches);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }

}
