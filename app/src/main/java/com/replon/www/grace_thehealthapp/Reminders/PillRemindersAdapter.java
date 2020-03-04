package com.replon.www.grace_thehealthapp.Reminders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.R;

import java.util.ArrayList;

public class PillRemindersAdapter extends RecyclerView.Adapter<PillRemindersAdapter.RemindersViewHolder> {

    public static final String TAG = "PillRemindersAdapter";
    private Activity activity;
    private ArrayList<ContentsPillReminders> remindersList;
    DatabaseHelperReminders myDb;

    public PillRemindersAdapter(Activity activity, ArrayList<ContentsPillReminders> remindersList) {
        this.activity = activity;
        this.remindersList = remindersList;
    }

    @NonNull
    @Override
    public RemindersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_reminders_meds,null);
        RemindersViewHolder holder=new RemindersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PillRemindersAdapter.RemindersViewHolder holder,final int i) {

        final ContentsPillReminders contents = remindersList.get(i);
        holder.pill_name.setText(String.valueOf(contents.getPill_name()));
        String num_pills;
        if (contents.getPill_number() == 1){
            num_pills = contents.getPill_number() + " Pill";
            holder.pills_number_image.setImageResource(R.drawable.ic_one_pill);
        }
        else{
            num_pills = contents.getPill_number() + " Pills";
            if(contents.getPill_number()==2){
                holder.pills_number_image.setImageResource(R.drawable.ic_two_pills);
            }
            if(contents.getPill_number()==3){
                holder.pills_number_image.setImageResource(R.drawable.ic_three_pills);
            }
            if(contents.getPill_number()==4){
                holder.pills_number_image.setImageResource(R.drawable.ic_four_pills);
            }
            if(contents.getPill_number()>=5){
                holder.pills_number_image.setImageResource(R.drawable.ic_five_pills);
            }
        }
        holder.pill_number.setText(num_pills);
        holder.pill_time.setText(contents.getPill_time());




        if (contents.isPill_taken()){
            holder.pill_taken.setImageResource(R.drawable.ic_pills_taken);
        }
        else{
            holder.pill_taken.setImageResource(R.drawable.ic_pills_not_taken);
        }

        holder.pill_taken_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"pill taken layout clicked");
                if(contents.isPill_taken()){
                    contents.setPill_taken(false);
                    holder.pill_taken.setImageResource(R.drawable.ic_pills_not_taken);
                    updatePillData(contents.getPill_id(),"false");

                }

                else{
                    contents.setPill_taken(true);
                    holder.pill_taken.setImageResource(R.drawable.ic_pills_taken);
                    updatePillData(contents.getPill_id(),"true");
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int i = 0;
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    //Single click
                    handler.postDelayed(r, 250);
                } else if (i == 2) {

                    if(contents.isPill_taken()){
                        contents.setPill_taken(false);
                        holder.pill_taken.setImageResource(R.drawable.ic_pills_not_taken);
                        updatePillData(contents.getPill_id(),"false");

                    }

                    else{
                        contents.setPill_taken(true);
                        holder.pill_taken.setImageResource(R.drawable.ic_pills_taken);
                        updatePillData(contents.getPill_id(),"true");
                    }

                    i=0;
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                deleteDialog("","Are you sure you wish to delete this Pill?",contents.getPill_id(),i);
                return false;
            }
        });


    }

    private void updatePillData(String id,String isTaken) {
        myDb=new DatabaseHelperReminders(activity.getApplicationContext());
        boolean isUpdated= myDb.updatePillTakenData(id,isTaken);
        Log.i(TAG,"Data saved");

        if(isUpdated){
            Log.i(TAG,"Reminders Pill Taken data updated to "+ isTaken);
        }else{
            Log.i(TAG,"Unable to update data");

        }

    }

    @Override
    public int getItemCount() {
        return remindersList.size();

    }

    public void deleteDialog(String title, String message,final String id,final int pos){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

                myDb=new DatabaseHelperReminders(activity.getApplicationContext());
                boolean isUpdated= myDb.deleteData(id);

                if(isUpdated){
                    Log.i(TAG,"Medicine deleted");
                    remindersList.remove(pos);
                    notifyDataSetChanged();
                }else{
                    Log.i(TAG,"Unable to delete data");

                }

            }
        }).setNegativeButton(android.R.string.cancel, null);

        builder.show();

    }


    class RemindersViewHolder extends RecyclerView.ViewHolder {


        TextView pill_name,pill_number,pill_time;
        ImageView pill_taken,pills_number_image;
        LinearLayout pill_taken_layout;


        public RemindersViewHolder(@NonNull View itemView) {
            super(itemView);

            pill_name = (TextView)itemView.findViewById(R.id.pill_name);
            pill_time = (TextView)itemView.findViewById(R.id.time_text);
            pill_number = (TextView)itemView.findViewById(R.id.pill_number);
            pill_taken = (ImageView)itemView.findViewById(R.id.pills_taken_image);
            pills_number_image = (ImageView)itemView.findViewById(R.id.pills_number_image);
            pill_taken_layout=(LinearLayout)itemView.findViewById(R.id.pill_taken_layout);


        }
    }
}
