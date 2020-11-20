package com.example.dbms_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Show extends AppCompatActivity {
    Retrofit retrofit;
    Requests requests;
    SwipeRefreshLayout srl;
    RecyclerView rv;
    Callbacks callback;
    ArrayList<Subjects> subjects;
    SharedPreferences sp;
    private String prefs = "MYPREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.12:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        requests = retrofit.create(Requests.class);
        srl = findViewById(R.id.refresh);
        rv = findViewById(R.id.rv);
        subjects = new ArrayList<>();
        sp = getSharedPreferences(prefs, MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        get_attendance(uid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        Adapter adapter = new Adapter(this, subjects);
        rv.setAdapter(adapter);

        srl.setOnRefreshListener(() -> {
            get_attendance(uid);
            srl.setRefreshing(false);
        });

        callback = adapter::notifyDataSetChanged;

    }

    private void get_attendance(String uid) {

        Call<List<Subjects>> call = requests.get_attendance(uid);
        call.enqueue(new Callback<List<Subjects>>() {
            @Override
            public void onResponse(Call<List<Subjects>> call, Response<List<Subjects>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    List<Subjects> subjectsList = response.body();
                    subjects.clear();
                    /*for (Subjects s : subjectsList)
                        subjects.add(s);*/
                    subjects.addAll(subjectsList);
                    callback.OnGetSuccess();

                }
            }

            @Override
            public void onFailure(Call<List<Subjects>> call, Throwable t) {
                Toast.makeText(Show.this, "please check your internet connection lol", Toast.LENGTH_SHORT).show();
                Log.e("12345678910111213", t.getMessage());
            }
        });
    }
}