package com.yourcitydate.poras.datingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourcitydate.poras.datingapp.Models.message;
import com.yourcitydate.poras.datingapp.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_LEFT = 0;

    int vt;

    String UID, OUID;

    Context context;
    List<message> messages;

    DatabaseReference read, write;



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
        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        message m = messages.get(position);
        holder.textView.setText(m.getMessage());

        //Message Time
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
        String time = timeformat.format(new Date(Long.parseLong(m.getTime())));
        holder.msgtime.setText(time);

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

    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView textView,msgstat,msgtime;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.chat_text);
            msgstat = (TextView)itemView.findViewById(R.id.msgstat);
            msgtime = (TextView)itemView.findViewById(R.id.msgTime);
        }
    }


}
