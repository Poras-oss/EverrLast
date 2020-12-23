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
     String UID, ID="blank";
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
        listenToNewMessages();
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
        adapter = new MatchedUserAdapter(getActivity(),matches, ID);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }

    private void listenToNewMessages(){
        //--------------LISTENING TO NEW MESSAGES---------------------------------------------------------

        newmsgref = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("is");
        newmsgref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("NASTYNIGGA", "NewMessageNotification: "+"ID="+ds.getKey()+ds.getValue().toString());

                    if (ds.getValue().toString().equals("n")){
                        //Sending the ID into adapter
                        ID = ds.getKey();
                        setAdapter();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
