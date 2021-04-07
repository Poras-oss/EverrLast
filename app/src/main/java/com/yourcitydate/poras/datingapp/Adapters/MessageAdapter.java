package com.yourcitydate.poras.datingapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourcitydate.poras.datingapp.Chat.ChatActivity;
import com.yourcitydate.poras.datingapp.DataCaching.MatchesViewModel;
import com.yourcitydate.poras.datingapp.Models.message;
import com.yourcitydate.poras.datingapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_LEFT = 0;

    SimpleDateFormat dateFormat, timeFormat;
    Calendar calendar, smstime;

    int vt;

    String UID, OUID, time;

    Context context;
    List<message> messages;

    DatabaseReference read, write;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    public MessageAdapter(Context applicationContext, List<message> messages, String UID, String OUID) {
        this.context = applicationContext;
        this.messages = messages;
        this.UID = UID;
        this.OUID = OUID;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout_right, parent, false);
            vt = 1;
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout_left, parent, false);
            vt = 0;
        }
        sharedPreferences = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        message m = messages.get(position);
        holder.textView.setText(m.getMessage());


        //Message Time
         time = getTime(m.getTime());
        holder.msgtime.setText(time);

        //Message Date
       if (position > 1){
           message m1 = messages.get(position-1);
           if (getDate(m1.getTime()).equals(getDate(m.getTime()))){
               holder.dateview.setVisibility(View.GONE);
               holder.dateview.setText("");
           }else{
               holder.dateview.setVisibility(View.VISIBLE);
              String d =  checkDate(getDate(m.getTime()), m.getTime());
               holder.dateview.setText(d);
           }
       }




        //Seen or delivered
       read = FirebaseDatabase.getInstance().getReference().child("Users").child(OUID).child("is").child(UID);
        write = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("is").child(OUID);



        if (position == messages.size()-1){
            // is = isSeen and y = seen or yes n = delivered
            if (vt == 1){
                holder.msgstat.setVisibility(View.VISIBLE);
            }else{
                holder.msgstat.setVisibility(View.GONE);
            }

            //Saving Users Last Message
            editor.putString(OUID,m.getMessage());
            editor.putString(OUID+"lastmsgtime",time);
            editor.commit();
        }else{
            holder.msgstat.setVisibility(View.GONE);
            write.setValue("y");
        }

        read.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.getValue().toString().equals("y")){
                        holder.msgstat.setText(" seen");
                    }

                    if (dataSnapshot.getValue().toString().equals("n")){
                        holder.msgstat.setText(" delivered");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (messages.get(position).getSender().equals(UID)){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }

    }

    private String checkDate(String date, String timestamp) {
        smstime = Calendar.getInstance();
        calendar = Calendar.getInstance();

        smstime.setTimeInMillis(Long.parseLong(timestamp));

        if (calendar.get(Calendar.DATE) == smstime.get(Calendar.DATE)){
            return "Today";
        }else if (calendar.get(Calendar.DATE) - smstime.get(Calendar.DATE) == 1){
            return "Yesterday";
        }else{
           return date;
        }

    }

    private String getDate(String timestamp){
           dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(new Date(Long.parseLong(timestamp)));
        return date;
    }

    private String getTime(String timestamp){
          timeFormat = new SimpleDateFormat("hh:mm aa");
        String time = timeFormat.format(new Date(Long.parseLong(timestamp)));
        return  time;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView textView,msgstat,msgtime,dateview;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.chat_text);
            msgstat = itemView.findViewById(R.id.msgstat);
            msgtime = itemView.findViewById(R.id.msgTime);
            dateview = itemView.findViewById(R.id.dateView);
        }
    }


}
