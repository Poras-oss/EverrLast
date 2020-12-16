package com.yourcitydate.poras.datingapp.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yourcitydate.poras.datingapp.MainActivity;
import com.yourcitydate.poras.datingapp.R;
import com.yourcitydate.poras.datingapp.profileSetup.Name;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    private ImageButton backArrow;
    private String otp;
    private Button button;
    private String verificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken token;
    private TextView resendOtp,subtitle;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        mAuth = FirebaseAuth.getInstance();
        backArrow = (ImageButton)findViewById(R.id.backarrowoftop);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resendOtp = (TextView)findViewById(R.id.resendToken);
        subtitle = (TextView)findViewById(R.id.textView2);

        progressBar = (ProgressBar)findViewById(R.id.verifyOtpProgress);
        progressBar.setVisibility(View.INVISIBLE);



        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RPN = getIntent().getStringExtra("phone");
                ResentOtp(RPN,token);
            }
        });

        final PinView pinView = (PinView)findViewById(R.id.firstPinView);
        button = (Button)findViewById(R.id.verifyotp);

        final String phoneNumber = getIntent().getStringExtra("phone");
        SendVerificationCode(phoneNumber);
        subtitle.setText("Please enter your OTP to verify, \n Your phone number: "+phoneNumber);
        pinView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (pinView.getText().toString().length() > 5){
                    button.setClickable(true);
                    button.setBackgroundResource(R.drawable.btnbg);
                }else{
                    button.setClickable(false);
                    button.setBackgroundResource(R.drawable.sleepingbtnbg);
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              otp = pinView.getText().toString();
              VerifyTOP(otp);
            }
        });


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //Automatic code fetch
            otp = phoneAuthCredential.getSmsCode();
            if (otp!=null){
                 VerifyTOP(otp);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(),"Something went wrong :(",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            token = forceResendingToken;
        }
    };

    private void SendVerificationCode(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }
    private void VerifyTOP(String otp){
        progressBar.setVisibility(View.VISIBLE);
        button.setText("");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    checkExistingUser();

                }else{
                    Toast.makeText(getApplicationContext(),"Whoops! Something went wrong :(",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkExistingUser() {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() || dataSnapshot.hasChild("fname")){
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(getApplicationContext(), Name.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ResentOtp(String RPN, PhoneAuthProvider.ForceResendingToken token){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+"+RPN,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                this.token);
    }
}
