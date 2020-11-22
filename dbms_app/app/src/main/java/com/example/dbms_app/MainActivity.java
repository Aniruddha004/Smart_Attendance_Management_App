package com.example.dbms_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    EditText nameet, addet, classet, uidet, usernameet, passwordet;
    boolean imageok = false;
    Intent i;
    Context context;
    SharedPreferences sp;
    String prefs = "MYPREFS";
    String filename;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    MaterialButton submit;
    Connect connect = Launcher.connect;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences(prefs, MODE_PRIVATE);
        editor = sp.edit();

        filename = getalpnum(10) + ".jpg";
        nameet = findViewById(R.id.nameEditText);
        addet = findViewById(R.id.addrEditText);
        classet = findViewById(R.id.classEditText);
        uidet = findViewById(R.id.uidEditText);
        iv = findViewById(R.id.iv);
        linearLayout = findViewById(R.id.backLayout);
        submit = findViewById(R.id.signUp);
        usernameet = findViewById(R.id.uNameEditText);
        passwordet = findViewById(R.id.passwordEditText);

        context = MainActivity.this;

        iv.setOnClickListener(v -> {
            if (!imageok) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("crop", "true");
                cameraIntent.putExtra("aspectX", 0);
                cameraIntent.putExtra("aspectY", 0);
                cameraIntent.putExtra("outputX", 150);
                cameraIntent.putExtra("outputY", 200);
                startActivityForResult(cameraIntent, 1);
            }
        });
        linearLayout.setOnClickListener(view -> {
            onBackPressed();
        });
        submit.setOnClickListener(v -> {
            String name = nameet.getText().toString();
            String _class = classet.getText().toString();
            String add = addet.getText().toString();
            String uid = uidet.getText().toString();
            String password = get_SHA1(passwordet.getText().toString());
            String username = usernameet.getText().toString();

            Student student = new Student(name, _class, add, uid, username, password, filename);
            post_info(student);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            connect.upload_image(photo, context, filename);
        }
    }

    public void imageUploadCallback(Bitmap fbitmap) {
        iv.setImageBitmap(fbitmap);
        imageok = true;
    }

    public void postInfoCallback(Student s) {
        editor.putString("username", s.getUsername());
        editor.putBoolean("isFirst", false);
        editor.putString("uid", s.getUid());
        editor.commit();
        i = new Intent(context, Show.class);
        startActivity(i);
        finish();
    }

    public void post_info(Student s) {
        if (!imageok) {
            Toast.makeText(getApplicationContext(), "Upload image first", Toast.LENGTH_SHORT).show();
        } else {
            connect.post_info(s, context);
        }
    }

    private String getalpnum(int n) {
        String AlphaNumericString = "0123456789" + "ABCDEFGHIJKLMNOPQRSUVWXYS" + "abcdefghijklmnopqrstuvwxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    private String get_SHA1(String s) {
        MessageDigest md;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this, Launcher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
