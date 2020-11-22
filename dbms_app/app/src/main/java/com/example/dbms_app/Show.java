package com.example.dbms_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
    ImageView logout;
    TextView uidtv;
    private String prefs = "MYPREFS";
    Connect connect = Launcher.connect;
    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        uidtv = findViewById(R.id.uidtv);
        srl = findViewById(R.id.refresh);
        logout = findViewById(R.id.logout);
        rv = findViewById(R.id.rv);

        context = this;
        subjects = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        Adapter adapter = new Adapter(this, subjects);
        rv.setAdapter(adapter);

        sp = getSharedPreferences(prefs, MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        uidtv.setText(uid.toUpperCase());


        callback = subs -> {
            subjects.clear();
            subjects.addAll(subs);
            adapter.notifyDataSetChanged();
        };

        srl.setOnRefreshListener(() -> {
            connect.get_attendance(uid,callback, context);
            srl.setRefreshing(false);
        });

        connect.get_attendance(uid,callback,context);
        logout.setOnClickListener(v -> log_out());

    }

    public void log_out(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isFirst", true);
        editor.putString("username", "");
        editor.putString("uid", "");
        editor.apply();
        Intent i = new Intent(Show.this, Launcher.class);
        startActivity(i);
        finish();
    }
}