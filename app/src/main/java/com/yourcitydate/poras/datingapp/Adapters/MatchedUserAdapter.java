package com.yourcitydate.poras.datingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yourcitydate.poras.datingapp.Chat.ChatActivity;
import com.yourcitydate.poras.datingapp.DataCaching.MatchesEntity;
import com.yourcitydate.poras.datingapp.Models.matchedUsers;
import com.yourcitydate.poras.datingapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MatchedUserAdapter extends RecyclerView.Adapter<MatchedUserAdapter.MatchesViewHolder> {
    List<MatchesEntity> users;
    Context context;
    String id, lastMessage, lastMessageTime;

    final String SharedPrefs = "prefs";
    SharedPreferences sharedPreferences;




    public MatchedUserAdapter(FragmentActivity activity, List<MatchesEntity> users, String id) {
        this.users = users;
        this.context = activity;
        this.id = id;
    }

    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_layout,parent,false);
        sharedPreferences = context.getSharedPreferences(SharedPrefs,MODE_PRIVATE);
        return new MatchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchesViewHolder holder, final int position) {
        holder.textView.setText(users.get(position).getName());
        Glide.with(context)
                .load(users.get(position).getImage())
                .into(holder.imageView);

        if (users.get(position).getUID().equals(id)){
            holder.newmsg.setText("You've got a new message!");
            holder.newmsg.setTypeface(Typeface.DEFAULT_BOLD);
        }



        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.newmsg.setText("");
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("OUID",users.get(position).getUID());
                i.putExtra("image",users.get(position).getImage());
                i.putExtra("name",users.get(position).getName());
                context.startActivity(i);
            }
        });

       //Searching for last messages in shared prefs
        lastMessage  = sharedPreferences.getString(users.get(position).getUID(),"");
        lastMessageTime = sharedPreferences.getString(users.get(position).getUID()+"lastmsgtime","");
        if (!lastMessage.equals("") && !users.get(position).getUID().equals(id)){
            holder.newmsg.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.newmsg.setText(lastMessage);
            holder.time.setText(lastMessageTime);
        }


    }

    @Override
    public int getItemCount() {
        return users.size();
    }




    public class MatchesViewHolder extends RecyclerView.ViewHolder{
        TextView textView,newmsg,time;
        CircleImageView imageView;
        ConstraintLayout constraintLayout;

        public MatchesViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = (ConstraintLayout)itemView.findViewById(R.id.matched_user_layout);
            textView = (TextView)itemView.findViewById(R.id.matches_list_name);
            imageView = (CircleImageView)itemView.findViewById(R.id.matches_list_profile);
            newmsg = (TextView)itemView.findViewById(R.id.lastmsg);
            time = (TextView)itemView.findViewById(R.id.lastMessageTime);
        }
    }

}
