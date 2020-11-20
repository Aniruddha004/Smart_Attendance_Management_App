package com.example.dbms_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Launcher extends AppCompatActivity {
    TextView createAccount;
    Button loginButton;
    EditText usrName,pass;
    ScrollView scrollView;
    Retrofit retrofit;
    Requests requests;
    SharedPreferences sp;
    String MYPREFS = "MYPREFS";
    Context context;
    ImageView passImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        createAccount = findViewById(R.id.create_Acc);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        usrName = findViewById(R.id.userName);
        pass = findViewById(R.id.passwordEditText);
        scrollView = findViewById(R.id.mainScrollView);
        scrollView.setVerticalScrollBarEnabled(false);
        passImage = findViewById(R.id.passImage);

        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.12:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        requests = retrofit.create(Requests.class);
        sp = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        context = Launcher.this;

        boolean isfirst = sp.getBoolean("isFirst", true);
        if(!isfirst){
            Intent i = new Intent(Launcher.this, Show.class);
            startActivity(i);
            finish();
        }

        usrName.addTextChangedListener(textWatcher);
        pass.addTextChangedListener(textWatcher);

        loginButton.setOnClickListener(v -> {
            String user = usrName.getText().toString().trim();
            String password = pass.getText().toString().trim();
            if(user.isEmpty())
                usrName.setError("Invalid User Name");
                //Toast.makeText(Launcher.this,"Invalid User Name",Toast.LENGTH_LONG).show();
            else if(password.isEmpty())
                pass.setError("Invalid Password");
                //Toast.makeText(Launcher.this,"Invalid Password",Toast.LENGTH_LONG).show();
            else {
                loginButton.setText(R.string.signIn);
                password = get_SHA1(password);
                Call<String> call = requests.verify(user, password);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.body().equals("No match")) {
                            passImage.setImageResource(R.drawable.ic_password_unlock);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isFirst", false);
                            editor.putString("username", user);
                            editor.putString("uid", response.body());
                            editor.apply();
                            Intent i = new Intent(context, Show.class);
                            context.startActivity(i);
                            ((Activity) context).finish();
                        } else {
                            usrName.setError("User Name should not be empty");
                            pass.setError("Password should not be empty");
                            //Toast.makeText(Launcher.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        createAccount.setOnClickListener(v -> {
            Intent i = new Intent(Launcher.this, MainActivity.class);
            startActivity(i);
            finish();
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
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(pass.getText().length() > 0 && usrName.getText().length() > 0){
                loginButton.setEnabled(true);
            }
            else if(pass.getText().length() > 0 || usrName.getText().length() > 0){
                loginButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}