package com.example.firebase_phoneauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Enter_Otp extends AppCompatActivity {
    EditText usr_otp;
    Button verify_usr_otp;
    String verificationId;
    String mOtp;
    String ph_no;

    private FirebaseAuth myAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter__otp);

      //  id of edit text where otp is insert
        usr_otp = findViewById(R.id.phone_otp);

      //   id of button to verify otp
      verify_usr_otp = findViewById(R.id.verify_otp);
         myAuth = FirebaseAuth.getInstance();

        ph_no = getIntent().getStringExtra("Number");
        sendVerificationCode(ph_no);

        verify_usr_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOtp = usr_otp.getText().toString().trim();
               sendVerificationCode(ph_no);
            }
        });
    }

    //Method for send Verification code
    public void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60, //validation of ssec
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
               mCallBack
        );
    }
    //Verification process
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if(code != null){
                usr_otp.setText(code);
                verifyCode(code);
            }
            else if(mOtp != null){
                verifyCode(mOtp);
            }
        }

        @Override
        public void onVerificationFailed(@org.jetbrains.annotations.NotNull FirebaseException e) {
            Toast.makeText(Enter_Otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }};
   // Verify code
    public void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);

        signInWithCredential(credential);

    }


    public void signInWithCredential(PhoneAuthCredential credential){
        myAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(Enter_Otp.this,Destination.class));
                            Toast.makeText(Enter_Otp.this, "succesfull", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Enter_Otp.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    }
