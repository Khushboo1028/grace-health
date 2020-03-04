package com.replon.www.grace_thehealthapp.Emergency;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.replon.www.grace_thehealthapp.R;

import java.util.ArrayList;

public class EmergencyNumbersAdapter extends RecyclerView.Adapter<EmergencyNumbersAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<ContentsEmergencyData> numbersList;

    public EmergencyNumbersAdapter(Activity activity, ArrayList<ContentsEmergencyData> numbersList) {
        this.activity = activity;
        this.numbersList = numbersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_emergency_numbers,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final ContentsEmergencyData contents = numbersList.get(i);
        holder.name.setText(contents.getName());
        holder.number.setText(contents.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri call = Uri.parse("tel:" + contents.getDescription());
                Intent intent = new Intent(Intent.ACTION_DIAL, call);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return numbersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView name,number;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.emg_num_text);
            number = (TextView)itemView.findViewById(R.id.emg_num);
        }
    }
}
