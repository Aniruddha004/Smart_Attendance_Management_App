package com.example.dbms_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Launcher extends AppCompatActivity {
TextView login, signup;
String MYPREFS = "MYPREFS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        SharedPreferences sp = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        boolean isfirst = sp.getBoolean("isFirst", true);
        if(!isfirst){
            Intent i = new Intent(Launcher.this, Show.class);
            startActivity(i);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Launcher.this, Login.class);
                startActivity(i);
                finish();

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Launcher.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }
}