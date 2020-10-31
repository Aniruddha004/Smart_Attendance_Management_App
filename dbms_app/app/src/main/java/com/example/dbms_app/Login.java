package com.example.dbms_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
TextView button;
EditText username, pass;
Retrofit retrofit;
Requests requests;
    SharedPreferences sp;
    String MYPREFS = "MYPREFS";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usrname);
        pass = findViewById(R.id.pass);
        button = findViewById(R.id.but);
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.165:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        requests = retrofit.create(Requests.class);
        sp = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        context = Login.this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String password = get_SHA1(pass.getText().toString());
                Call<String> call = requests.verify(user, password);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(!response.body().equals("No match")){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isFirst", false);
                            editor.putString("username", user);
                            editor.putString("uid", response.body());
                            editor.commit();
                            Intent i = new Intent(context, Show.class );
                            context.startActivity(i);
                            ((Activity)context).finish();
                        }
                        else{
                            Toast.makeText(Login.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
        });

    }
    private String get_SHA1(String s){
        MessageDigest md = null;
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
}