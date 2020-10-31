package com.example.dbms_app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Requests requests;
    Retrofit retrofit;
    TextView submit;
    ImageView iv;
    EditText nameet, addet, classet, uidet, usernameet, passwordet;
    boolean imageok = false, infook = false;
    Intent i;
    Context context;
    SharedPreferences sp;
    private String prefs = "MYPREFS";
    private String filename;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences(prefs, MODE_PRIVATE);
        boolean first = sp.getBoolean("ifFirst", true);
        editor = sp.edit();
        /*if(first){
            String id = getalpnum(16);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("UID", id);
            editor.commit();
        }*/
        filename = getalpnum(10) + ".jpg";
        nameet = findViewById(R.id.nameet);
        addet = findViewById(R.id.addet);
        classet = findViewById(R.id.classet);
        uidet = findViewById(R.id.uidet);
        iv = findViewById(R.id.iv);
        submit = findViewById(R.id.submit);
        usernameet = findViewById(R.id.usernameet);
        passwordet = findViewById(R.id.passwordet);

        context = MainActivity.this;
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.165:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        requests = retrofit.create(Requests.class);
        //get_attendance();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageok) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra("crop", "true");
                    cameraIntent.putExtra("aspectX", 0);
                    cameraIntent.putExtra("aspectY", 0);
                    cameraIntent.putExtra("outputX", 150);
                    cameraIntent.putExtra("outputY", 200);
                    startActivityForResult(cameraIntent, 1);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameet.getText().toString();
                String _class = classet.getText().toString();
                String add = addet.getText().toString();
                String uid = uidet.getText().toString();
                String password = get_SHA1(passwordet.getText().toString());
                String username = usernameet.getText().toString();

                Stundent stundent = new Stundent(name, _class, add, uid, username, password, filename);
                post_info(stundent);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            upload_image(photo);
        }
    }


    private void upload_image(Bitmap bitmap) {

        //Bitmap bitmap;    // get bitmap from camera intent
        final Bitmap fbitmap = Bitmap.createScaledBitmap(bitmap, 150, 200, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.dog);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        File f = new File(this.getCacheDir(), filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "An error occure while reading image :(", Toast.LENGTH_SHORT).show();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Error while creating file :(", Toast.LENGTH_SHORT).show();
        }
        try {
            fos.write(byteArray);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Unknown error occured :(", Toast.LENGTH_SHORT).show();
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", f.getName(), reqFile);

        Call<String> call = requests.postimage(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body().equals("OK")) {
                        iv.setImageBitmap(fbitmap);
                        imageok  = true;
                    } else if (response.body().equals("No face detected")) {
                        Toast.makeText(getApplicationContext(), "face not detected try again with focus on face ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server caused error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error while uploading image\n" + "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void post_info(Stundent s) {
        if (!imageok) {
            Toast.makeText(getApplicationContext(), "Upload image first", Toast.LENGTH_SHORT).show();
        } else {

            Call<String> call = requests.postinfo(s);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        editor.putString("username", s.getUsername());
                        editor.putBoolean("isFirst", false);
                        editor.putString("uid", s.getUid());
                        editor.commit();


                            i = new Intent(context, Show.class);
                            startActivity(i);
                            ((Activity)context).finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });

        }
    }
    private String getalpnum(int n) {
        String AlphaNumericString = "0123456789" +
                "ABCDEFGHIJKLMNOPQRSUVWXYS" +
                "abcdefghijklmnopqrstuvwxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();

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
