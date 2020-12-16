package com.yourcitydate.poras.datingapp.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.yourcitydate.poras.datingapp.R;

import java.util.concurrent.TimeUnit;

public class Registration extends AppCompatActivity {
    private EditText phonenumber,countrycode;
    private Button sendOtp;
    private String phoneNumber,cc;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        phonenumber = (EditText)findViewById(R.id.mobilenumber);
        countrycode = (EditText)findViewById(R.id.countrycode);
        sendOtp = (Button)findViewById(R.id.sendOtpBtn);
        countrycode.setText("+91");

        progressBar = (ProgressBar)findViewById(R.id.sendOtpProgress);
        progressBar.setVisibility(View.INVISIBLE);

        phonenumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (phonenumber.getText().toString().length()>9){
                    sendOtp.setClickable(true);
                    sendOtp.setBackgroundResource(R.drawable.btnbg);
                    phoneNumber = phonenumber.getText().toString();
                }else{
                    sendOtp.setClickable(false);
                    sendOtp.setBackgroundResource(R.drawable.sleepingbtnbg);
                }
                return false;
            }
        });

        countrycode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (countrycode.getText().toString().length()>1){
                    sendOtp.setClickable(true);
                    sendOtp.setBackgroundResource(R.drawable.btnbg);
                    cc = countrycode.getText().toString();
                }else{
                    sendOtp.setClickable(false);
                    sendOtp.setBackgroundResource(R.drawable.sleepingbtnbg);
                }
                return false;
            }
        });



        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                sendOtp.setText("");
                Intent i = new Intent(getApplicationContext(),VerifyOTP.class);
                i.putExtra("phone","+91"+phoneNumber);
                startActivity(i);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }




}
