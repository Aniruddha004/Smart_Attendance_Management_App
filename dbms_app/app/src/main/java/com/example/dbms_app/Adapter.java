package com.example.dbms_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Subjects> subjects;

    public Adapter(Context context, ArrayList<Subjects> subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new subViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        subViewHolder svh = (subViewHolder)holder;

        svh.subject.setText(subjects.get(position).getSub_name());
        int total = subjects.get(position).getTotal_lectures();
        int att = subjects.get(position).getAttended_lectures();
        float percent;
        if(total!=0)
            percent = (float)att/(float)total*100;
        else
            percent= 0;
        svh.total.setText(String.format(Locale.ENGLISH,"Total: %d", total));
        svh.attended.setText(String.format(Locale.ENGLISH,"Attended: %d", att));
        svh.percent.setText(String.format(Locale.ENGLISH,"%.2f %s",percent,"%"));
        }

    @Override
    public int getItemCount() {
        return subjects.size();
    }
    public class subViewHolder extends RecyclerView.ViewHolder{
        TextView subject, total, attended, percent;
        public subViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subname);
            total = itemView.findViewById(R.id.total);
            attended = itemView.findViewById(R.id.attended);
            percent = itemView.findViewById(R.id.percent);
        }
    }
}
