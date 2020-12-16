package com.yourcitydate.poras.datingapp.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.yourcitydate.poras.datingapp.Adapters.MessageAdapter;
import com.yourcitydate.poras.datingapp.Models.message;
import com.yourcitydate.poras.datingapp.R;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

     TextView name, status;
     CircleImageView imageView;
     Toolbar toolbar;
     String UID, OUID, Oimage, Oname, ChatRoomID;
     EditText editText;
     Button button;
     DatabaseReference database, chatDatabase, userStatus, msgstat;
     Query databaseQuery, moreDataQuery, lastMessageRef;
     List<message> messages = new ArrayList<>();
     RecyclerView recyclerView;
     LinearLayoutManager layoutManager;
     MessageAdapter adapter;
     SwipeRefreshLayout swipeRefreshLayout;

    final String SharedPrefs = "prefs";
    SharedPreferences sharedPreferences;


    //Pagination stuff

    int itemPos = 0;
    String lastKey = "", prevKey = "";
    int totalMessagetoLoad = 100;

    //getting last message of the chat
    String lastmessagekey = "";
    boolean isLastMessageLoaded = false;

    //Time and date

    SimpleDateFormat timeformat,formatter;
    Calendar calendar,smsTime;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        sharedPreferences = getSharedPreferences(SharedPrefs,MODE_PRIVATE);
        UID = sharedPreferences.getString("UID","0");

        OUID = getIntent().getStringExtra("OUID");
        Oimage = getIntent().getStringExtra("image");
        Oname = getIntent().getStringExtra("name");

        calendar = Calendar.getInstance();


        database = FirebaseDatabase.getInstance().getReference().child("Chat");
        userStatus = FirebaseDatabase.getInstance().getReference().child("Users").child(OUID).child("s");
        msgstat = FirebaseDatabase.getInstance().getReference().child("Users").child(OUID).child("is").child(UID);



        toolbar = (Toolbar)findViewById(R.id.chat_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name = (TextView)findViewById(R.id.chat_activity_username);
        imageView = (CircleImageView) findViewById(R.id.chat_activity_image);
        editText = (EditText) findViewById(R.id.message_edittext);
        button = (Button) findViewById(R.id.msgSendButton);
        status = (TextView) findViewById(R.id.status);

        recyclerView = (RecyclerView)findViewById(R.id.message_Recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        name.setText(Oname);

        Glide.with(getApplicationContext())
                .load(Oimage)
                .into(imageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")){
                    SendMessage(UID,editText.getText().toString().trim());
                    editText.setText("");
                }
            }
        });

        fetchChatRoomId();
        userStatus.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   String st = dataSnapshot.getValue().toString();
                   if(st.equals("true")){
                       status.setText("Online");
                   }else{
                       formatter = new SimpleDateFormat("dd/MM/yyyy");
                       String dateString = formatter.format(new Date(Long.parseLong(st)));
                       timeformat = new SimpleDateFormat("HH:mm");
                       String time = timeformat.format(new Date(Long.parseLong(st)));

                       smsTime = Calendar.getInstance();
                       smsTime.setTimeInMillis(Long.parseLong(st));

                       if (calendar.get(Calendar.DATE) == smsTime.get(Calendar.DATE)){
                           status.setText("Last seen "+"today at "+ time);
                       }else if (calendar.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1){
                           status.setText("Last seen "+"yesterday at "+ time);
                       }else{
                           status.setText("Last seen "+dateString +" at "+ time);
                       }


                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemPos = 0;
               loadMoreMessages();
            }
        });




    }

    private void loadMoreMessages() {
        moreDataQuery = chatDatabase.orderByKey().endAt(lastKey).limitToLast(totalMessagetoLoad);
        if (!isLastMessageLoaded){
            moreDataQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot.exists()){
                        message m = new message(dataSnapshot.child("s").getValue().toString(),dataSnapshot.child("m").getValue().toString(),dataSnapshot.child("t").getValue().toString());
                        String nastyKey = dataSnapshot.getKey();
                        if (!prevKey.equals(nastyKey)){
                            messages.add(itemPos++,m);
                        }else {
                            prevKey = nastyKey;
                        }

                        if (itemPos == 1){
                            lastKey = nastyKey;
                        }

                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);

                        if (nastyKey.equals(lastmessagekey)){
                            isLastMessageLoaded = true;
                        }


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
        }else{
            Toast.makeText(getApplicationContext(),"No more messages left!",Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private void fetchChatRoomId() {
       final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               ChatRoomID =  dataSnapshot.child("matches").child(OUID).getValue().toString();
                chatDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(ChatRoomID);
                getMessages();
                getLastMessageKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLastMessageKey() {
        lastMessageRef = chatDatabase.limitToFirst(1);
        lastMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if (dataSnapshot.exists()){
                        lastmessagekey = ds.getKey();
                        lastMessageRef.removeEventListener(this);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void SendMessage(String sender, String msg){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("s",sender);
        hashMap.put("m",msg);
        hashMap.put("t", ServerValue.TIMESTAMP);

        database.child(ChatRoomID).push().setValue(hashMap);
        msgstat.setValue("n");
    }

    private void getMessages(){
        swipeRefreshLayout.setRefreshing(true);
        databaseQuery = chatDatabase.limitToLast(totalMessagetoLoad);
        databaseQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    message m = new message(dataSnapshot.child("s").getValue().toString(),dataSnapshot.child("m").getValue().toString(),dataSnapshot.child("t").getValue().toString());
                    messages.add(m);
                    itemPos++;
                    if (itemPos == 1){
                        String nastyKey = dataSnapshot.getKey();
                        lastKey = nastyKey;
                        prevKey = nastyKey;
                    }

                    adapter = new MessageAdapter(getApplicationContext(),messages,UID,OUID);
                    recyclerView.setAdapter(adapter);
                }
                swipeRefreshLayout.setRefreshing(false);

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


}