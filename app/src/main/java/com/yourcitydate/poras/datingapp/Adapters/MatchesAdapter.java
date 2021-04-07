package com.yourcitydate.poras.datingapp.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.yourcitydate.poras.datingapp.Models.MatchModel;
import com.yourcitydate.poras.datingapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MatchesAdapter extends ArrayAdapter<MatchModel> {


    public MatchesAdapter(@NonNull Context context, int resource, @NonNull List<MatchModel> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MatchModel cards = (MatchModel) getItem(position);



        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
        }



        TextView name = (TextView)convertView.findViewById(R.id.matchname);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.match_image);

        name.setText(cards.getName()+", "+getAge(cards.getAge()));
        Glide.with(getContext())
                .load(cards.getImage())
                .into(imageView);

        return convertView;


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
}

