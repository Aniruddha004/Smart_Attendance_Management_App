package com.example.flask_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.165:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi  = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<post>> call = jsonPlaceHolderApi.getposts();

        call.enqueue(new Callback<List<post>>() {
            @Override
            public void onResponse(Call<List<post>> call, Response<List<post>> response) {

                List<post> posts = response.body();


                for(post p: posts){
                    String s="";
                    s += "Model: " + p.getModel() + "\n";
                    s+= "Age: " + p.getAge() + "\n\n";
                    tv.append(s);
                }

            }

            @Override
            public void onFailure(Call<List<post>> call, Throwable t) {
                tv.setText(t.getMessage());
            }
        });

    }
}
