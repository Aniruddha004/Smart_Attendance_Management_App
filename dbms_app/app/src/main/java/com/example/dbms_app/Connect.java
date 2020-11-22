package com.example.dbms_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connect {
    Retrofit retrofit;
    Requests requests;

    public Connect() {
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.165:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        requests = retrofit.create(Requests.class);
    }
    public void verify(String username, String password, Context context){

        Call<String> call = requests.verify(username, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()&&response.code()==200){
                    if(!response.body().equals("No match")){
                        ((Launcher)context).verify_callback(username, response.body());
                    }
                    else{
                        Toast.makeText(context, "Wrong username or password :(", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public void upload_image(Bitmap bitmap, Context context, String filename) {

        final Bitmap fbitmap = Bitmap.createScaledBitmap(bitmap, 150, 200, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        File f = new File(context.getCacheDir(), filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            Toast.makeText(context, "An error occure while reading image :(", Toast.LENGTH_SHORT).show();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error while creating file :(", Toast.LENGTH_SHORT).show();
        }
        try {
            fos.write(byteArray);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(context, "Unknown error occured :(", Toast.LENGTH_SHORT).show();
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", f.getName(), reqFile);

        Call<String> call = requests.postimage(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body().equals("OK")) {
                        ((MainActivity)context).imageUploadCallback(fbitmap);
                    } else if (response.body().equals("No face detected")) {
                        Toast.makeText(context, "face not detected try again with focus on face ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server caused error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Error while uploading image\n" + "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void post_info(Student s, Context context) {
            Call<String> call = requests.postinfo(s);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        ((MainActivity)context).postInfoCallback(s);
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    public void get_attendance(String uid, Callbacks callback, Context context) {

        ArrayList<Subjects> subjects = new ArrayList<>();
        Call<List<Subjects>> call = requests.get_attendance(uid);
        call.enqueue(new Callback<List<Subjects>>() {
            @Override
            public void onResponse(Call<List<Subjects>> call, Response<List<Subjects>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    List<Subjects> subjectsList = response.body();
                    subjects.clear();
                    subjects.addAll(subjectsList);
                    callback.OnGetSuccess(subjects);

                }
            }

            @Override
            public void onFailure(Call<List<Subjects>> call, Throwable t) {
                Toast.makeText(context, "please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
