package com.example.firebase_phoneauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterPhone_no extends AppCompatActivity {
    EditText usr_no;
    Button send_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_no);
        usr_no=findViewById(R.id.usr_no);
        send_otp=findViewById(R.id.send_otp);

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = usr_no.getText().toString().trim();

                if(TextUtils.isEmpty(number)||number.length()<10||number.length()>10){
                    usr_no.setError("Valid Number Required");
                    usr_no.requestFocus();
                }
                else {
                    String usr_no = "+" + "91" + number;

                    Intent intent = new Intent(EnterPhone_no.this,Enter_Otp.class);
                    intent.putExtra("Number", usr_no);
                    startActivity(intent);
                }
            }
        });
    }
}
