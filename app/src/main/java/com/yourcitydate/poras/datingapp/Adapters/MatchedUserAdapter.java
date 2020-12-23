package com.yourcitydate.poras.datingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yourcitydate.poras.datingapp.Chat.ChatActivity;
import com.yourcitydate.poras.datingapp.Models.matchedUsers;
import com.yourcitydate.poras.datingapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MatchedUserAdapter extends RecyclerView.Adapter<MatchedUserAdapter.MatchesViewHolder> {
    List<matchedUsers> users;
    Context context;
    String id;

    public MatchedUserAdapter(FragmentActivity activity, List<matchedUsers> users, String id) {
        this.users = users;
        this.context = activity;
        this.id = id;
    }

    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_layout,parent,false);
        return new MatchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchesViewHolder holder, final int position) {
        holder.textView.setText(users.get(position).getName());
        Glide.with(context)
                .load(users.get(position).getImage())
                .into(holder.imageView);

        if (users.get(position).getUid().equals(id)){
            holder.newmsg.setText("You've got a new message!");
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.newmsg.setText("");
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("OUID",users.get(position).getUid());
                i.putExtra("image",users.get(position).getImage());
                i.putExtra("name",users.get(position).getName());
                context.startActivity(i);
            }
        });

        //Finding the ID in the list and then change it


    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class MatchesViewHolder extends RecyclerView.ViewHolder{
        TextView textView,newmsg;
        CircleImageView imageView;

        public MatchesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.matches_list_name);
            imageView = (CircleImageView)itemView.findViewById(R.id.matches_list_profile);
            newmsg = (TextView)itemView.findViewById(R.id.lastmsg);
        }
    }
}
