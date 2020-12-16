package com.yourcitydate.poras.datingapp.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.yourcitydate.poras.datingapp.MainActivity;
import com.yourcitydate.poras.datingapp.R;

import org.w3c.dom.Text;

import java.util.Locale;

public class limitLikesDialog extends AppCompatDialogFragment implements RewardedVideoAdListener {
    static final long START_TIME_IN_MILLIS = 3600000;
    TextView timerTextView;
    long mTimeLeftInMillis;
    Boolean isTimerRunning = false;
    CountDownTimer countDownTimer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    final String mPrefs = "prefs";
    long mEndTime;
    RewardedVideoAd videoAd;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)  {



        sharedPreferences = getActivity().getSharedPreferences(mPrefs, Context.MODE_PRIVATE);
        isTimerRunning = sharedPreferences.getBoolean("timer",false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog,null);

        timerTextView = (TextView)view.findViewById(R.id.timerTextView);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                startTimer();
            }
        }, 1000);

        InitializeAd();


        builder.setView(view)
                .setTitle("Likes limit Reached")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                            getActivity().finish();
                    }
                })
                .setPositiveButton("Watch an ad", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(videoAd.isLoaded()){
                            videoAd.show();
                        }
                    }
                });



        return  builder.create();

    }


    private void startTimer() {
        mEndTime = System.currentTimeMillis()+mTimeLeftInMillis;
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownTimer();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;

                resetTimer();

            }
        }.start();

        isTimerRunning = true;


    }

    private void updateCountDownTimer() {
        int hours = (int)(mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int)((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int)mTimeLeftInMillis / 1000 % 60;

        String timeLeftFormatted;
        if (hours > 0){
            timeLeftFormatted = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, seconds);
        }else{
            timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        }

        timerTextView.setText(timeLeftFormatted);
    }



    @Override
    public void onStop() {
        super.onStop();
       // countDownTimer.cancel();
      /* editor.putLong("timeleft",mTimeLeftInMillis);
        editor.putLong("endtime",mEndTime);
        editor.putBoolean("timer",isTimerRunning);
        editor.commit();*/

    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownTimer();
    }

    @Override
    public void onPause() {
        super.onPause();



    }

    @Override
    public void onStart() {
        super.onStart();
        mTimeLeftInMillis = sharedPreferences.getLong("timeleft",START_TIME_IN_MILLIS);
        isTimerRunning = sharedPreferences.getBoolean("timer",false);
        updateCountDownTimer();

        if (isTimerRunning){
            mEndTime = sharedPreferences.getLong("endtime",0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0){
                mTimeLeftInMillis = 0;
                isTimerRunning = false;

                updateCountDownTimer();
            }else{
                startTimer();
            }

        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {


    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(getActivity(),"started",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {


    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(getActivity(),"You got some stuff",Toast.LENGTH_SHORT).show();
        Log.d("REWARDED", "onRewarded: holy amount");
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {


        Toast.makeText(getActivity(),"AD failed to load try again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    private void InitializeAd(){
        MobileAds.initialize(getActivity(),"ca-app-pub-3940256099942544~3347511713");
        videoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        videoAd.setRewardedVideoAdListener(this);

        videoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }}
