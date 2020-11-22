package com.example.dbms_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Launcher extends AppCompatActivity {
    TextView createAccount;
    Button loginButton;
    EditText usrName,pass;
    SharedPreferences sp;
    String MYPREFS = "MYPREFS";
    Context context;
    public static Connect connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        connect = new Connect();

        createAccount = findViewById(R.id.create_Acc);
        loginButton = findViewById(R.id.loginButton);
        usrName = findViewById(R.id.userName);
        pass = findViewById(R.id.passwordEditText);

        sp = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        context = Launcher.this;

        boolean isfirst = sp.getBoolean("isFirst", true);
        if(!isfirst){
            Intent i = new Intent(Launcher.this, Show.class);
            startActivity(i);
            finish();
        }

        loginButton.setOnClickListener(v -> {
            String user = usrName.getText().toString().trim();
            String password = pass.getText().toString().trim();
            if(user.isEmpty())
                usrName.setError("Username can't be empty");
            else if(password.isEmpty())
                pass.setError("Username can't be empty");
            else {
                password = get_SHA1(password);
                connect.verify(user, password, context);
            }
        });
        createAccount.setOnClickListener(v -> {
            Intent i = new Intent(Launcher.this, MainActivity.class);
            startActivity(i);
            this.finish();
        });
    }
    private String get_SHA1(String s){
        MessageDigest md ;
        String hashtext = "";
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(s.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashtext;
    }
    public void verify_callback(String username, String uid){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirst", false);
            editor.putString("username", username);
            editor.putString("uid", uid);
            editor.apply();

            Intent i = new Intent(context, Show.class);
            startActivity(i);
            finish();
    }
}