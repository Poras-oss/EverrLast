package com.yourcitydate.poras.datingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.yourcitydate.poras.datingapp.Adapters.MatchesAdapter;
import com.yourcitydate.poras.datingapp.DataCaching.MatchesDao;
import com.yourcitydate.poras.datingapp.DataCaching.MatchesEntity;
import com.yourcitydate.poras.datingapp.DataCaching.MatchesViewModel;
import com.yourcitydate.poras.datingapp.Models.MatchModel;
import com.yourcitydate.poras.datingapp.Profile.profileViewer;
import com.yourcitydate.poras.datingapp.R;
import com.yourcitydate.poras.datingapp.utils.limitLikesDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Matches extends Fragment {
    private MatchesAdapter adapter;
    private List<MatchModel> data_list;
    int i, numOfReq = 25 , currentAge;
    static int minAge, maxAge;
    private String currentUserId, currentUserGender;
    DatabaseReference dref, keyref, tokenRef;
    private int likeCounts = 0;
    String lastKey, existingkey = "null", onDestrykey = "null";
    Query reference;
    SharedPreferences.Editor editor, AssEditor;
    private ImageButton like, unlike;
    SwipeFlingAdapterView flingContainer;
    private final String SharedPrefs = "prefs";
    private SharedPreferences sharedPreferences;
    TextView textView;
    int likeThreshold = 10;
    static final long START_TIME_IN_MILLIS = 3600000;
    long mTimeLeftInMillis;
     Boolean isTimerRunning = false;
    public static Boolean isOnCurrentFrag = true;
    CountDownTimer countDownTimer;
    long mEndTime;


    public Matches() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isOnCurrentFrag = false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        isOnCurrentFrag = true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefs, getActivity().MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("UID", "null");
        minAge = sharedPreferences.getInt("minAge", 16);
        maxAge = sharedPreferences.getInt("maxAge", 55);
        dref = FirebaseDatabase.getInstance().getReference().child("Users");
        keyref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        editor = sharedPreferences.edit();
        likeCounts = sharedPreferences.getInt("likeCounts", 0);
        textView = (TextView) view.findViewById(R.id.matchname);
        isTimerRunning = sharedPreferences.getBoolean("timer", false);
        currentUserGender = sharedPreferences.getString("gender", "null");




        if (currentUserGender.equals("null")) {
            if (!currentUserId.equals("null")) {
                // -------------------------Token generation for push notifications----------------------------------
                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful()){
                                editor.putString("token",task.getResult().getToken());
                                editor.commit();
                                tokenRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("t");
                                tokenRef.setValue(task.getResult().getToken());
                            }
                        }
                    });

                getCurrentUserGender();
            }
        } else {
            fetchExistingKey();
        }

        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.frame1);
        data_list = new ArrayList<MatchModel>();

        like = (ImageButton) view.findViewById(R.id.like);
        unlike = (ImageButton) view.findViewById(R.id.skip);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right();
            }
        });
        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });


        //choose your favorite adapter
        adapter = new MatchesAdapter(getActivity(), R.layout.item, data_list);

        //set the listener and the adapter
        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                if (isOnCurrentFrag){
                    if (likeCounts > likeThreshold) {
                        //Dialog will open here
                        limitLikesDialog likesDialog = new limitLikesDialog();
                        startTimer();
                        likesDialog.show(getActivity().getSupportFragmentManager(), "nasty dialog");
                        Toast.makeText(getActivity(), "you are out of swipes", Toast.LENGTH_SHORT).show();
                        // data_list.remove(0);
                        data_list.add(data_list.get(0));
                    } else {
                        data_list.remove(0);
                    }

                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                if (isOnCurrentFrag) {
                    MatchModel card = (MatchModel) dataObject;
                    String userID = card.getUID();
                    dref.child(userID).child("connections").child("skip").child(currentUserId).setValue(true);
                    onDestrykey = userID;
                    if (data_list.size() <= 0) {
                        loadMoreData();
                    }
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                if (isOnCurrentFrag) {
                    if (likeCounts > likeThreshold) {
                        return;
                    } else {
                        MatchModel card = (MatchModel) dataObject;
                        String userID = card.getUID();
                        dref.child(userID).child("connections").child("like").child(currentUserId).setValue(true);
                        onDestrykey = userID;
                        isConnectionMatched(userID);
                        likeCounts++;

                        if (data_list.size() <= 0) {
                            loadMoreData();
                        }
                    }
                }
            }


            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                adapter.notifyDataSetChanged();
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                if (isOnCurrentFrag){
                    View view = flingContainer.getSelectedView();
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0.5 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0.5 ? scrollProgressPercent : 0);

                }
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                if (isOnCurrentFrag){
                    MatchModel card = (MatchModel) dataObject;
                    String userID = card.getUID();
                    Intent i = new Intent(getActivity(), profileViewer.class);
                    i.putExtra("UID", userID);
                    getActivity().startActivity(i);
                }

            }
        });
    }


    private void isConnectionMatched(final String userID) {
        final DatabaseReference databaseReference = dref.child(currentUserId).child("connections").child("like").child(userID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String chatRoomId = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
                    db.child(userID).child("matches").child(currentUserId).setValue(chatRoomId);
                    db.child(currentUserId).child("matches").child(userID).setValue(chatRoomId);
                    Toast.makeText(getActivity(), "Its a match", Toast.LENGTH_LONG).show();

                    editor.putBoolean("isMatchedUsersExist",false);
                    editor.commit();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void left() {
        try {
            flingContainer.getTopCardListener().selectLeft();
        } catch (Exception e) {

        }
    }

    private void right() {
        try {
            flingContainer.getTopCardListener().selectRight();
        } catch (Exception e) {
        }
    }

    private void getCurrentUserGender() {
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("gender", dataSnapshot.child("gender").getValue().toString());
                    editor.commit();
                    currentUserGender = dataSnapshot.child("gender").getValue().toString();
                    fetchExistingKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchExistingKey() {
        keyref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("lastkey")) {
                    if (existingkey.equals("null")) {
                        existingkey = dataSnapshot.child("lastkey").getValue().toString();
                        reference = dref.orderByKey().startAt(existingkey).limitToFirst(numOfReq);
                    } else {
                        return;
                    }

                } else {
                    reference = dref.limitToFirst(numOfReq);
                }
                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void loadData() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                currentAge = Integer.parseInt(String.valueOf(dataSnapshot.child("age").getValue()));
                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("skip").hasChild(currentUserId) && !dataSnapshot.child("connections").child("like").hasChild(currentUserId) && !dataSnapshot.child("gender").getValue().toString().equals(currentUserGender)
                        && currentAge >= minAge && currentAge <= maxAge) {
                    MatchModel item = new MatchModel(dataSnapshot.child("fname").getValue().toString(), dataSnapshot.child("profileImages").child("one").getValue().toString(), dataSnapshot.getKey(), dataSnapshot.child("dob").getValue().toString());
                    data_list.add(item);
                    recordKey(data_list.size());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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

    private void recordKey(int size) {
        try {
            MatchModel matchModel = data_list.get(size - 1);
            lastKey = matchModel.getUID();
        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {

        }

    }

    private void loadMoreData() {
        Query db = FirebaseDatabase.getInstance().getReference().child("Users").orderByKey().startAt(lastKey).limitToFirst(numOfReq);
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                currentAge = Integer.parseInt(String.valueOf(dataSnapshot.child("age").getValue()));
                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("skip").hasChild(currentUserId) && !dataSnapshot.child("connections").child("like").hasChild(currentUserId) && !dataSnapshot.child("gender").getValue().toString().equals(currentUserGender) && currentAge >= minAge && currentAge <= maxAge) {
                    MatchModel item = new MatchModel(dataSnapshot.child("fname").getValue().toString(), dataSnapshot.child("profileImages").child("one").getValue().toString(), dataSnapshot.getKey(), dataSnapshot.child("dob").getValue().toString());
                    data_list.add(item);
                    adapter.notifyDataSetChanged();
                    recordKey(data_list.size());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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


    @Override
    public void onPause() {
        super.onPause();

        if (onDestrykey.equals("null")) {
            return;
        } else {
            keyref.child("lastkey").setValue(onDestrykey);
        }
        SharedPreferences.Editor editorone = sharedPreferences.edit();
        editorone.putInt("likeCounts", likeCounts);
        editorone.putLong("timeleft", mTimeLeftInMillis);
        editorone.putLong("endtime", mEndTime);
        editorone.putBoolean("timer", isTimerRunning);
        editorone.commit();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("VOILA", "onStop: ");
        SharedPreferences.Editor editorone = sharedPreferences.edit();

        //countDownTimer.cancel();
        editorone.putInt("likeCounts", likeCounts);
        editorone.putLong("timeleft", mTimeLeftInMillis);
        editorone.putLong("endtime", mEndTime);
        editorone.putBoolean("timer", isTimerRunning);
        editorone.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        likeCounts = sharedPreferences.getInt("likeCounts", 0);
        mTimeLeftInMillis = sharedPreferences.getLong("timeleft", START_TIME_IN_MILLIS);
        isTimerRunning = sharedPreferences.getBoolean("timer", false);

    }

    @Override
    public void onStart() {
        super.onStart();
        mTimeLeftInMillis = sharedPreferences.getLong("timeleft", START_TIME_IN_MILLIS);
        isTimerRunning = sharedPreferences.getBoolean("timer", false);
        likeCounts = sharedPreferences.getInt("likeCounts", 0);

        // updateCountDownTimer();

        if (isTimerRunning) {
            mEndTime = sharedPreferences.getLong("endtime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                isTimerRunning = false;
                editor.putInt("likeCounts", 0);
                editor.commit();
                //   updateCountDownTimer();
            } else {
                startTimer();
            }

        }
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                //  updateCountDownTimer();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                AssEditor = sharedPreferences.edit();
                AssEditor.putLong("timeleft", START_TIME_IN_MILLIS);
                AssEditor.putBoolean("timer", isTimerRunning);
                AssEditor.putInt("likeCounts", 0);
                AssEditor.commit();
                likeCounts = 0;
                resetTimer();


            }
        }.start();

        isTimerRunning = true;


    }



    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        // updateCountDownTimer();
    }


}