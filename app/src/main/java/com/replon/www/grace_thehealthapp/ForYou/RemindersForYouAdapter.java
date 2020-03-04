package com.replon.www.grace_thehealthapp.ForYou;

import android.app.Activity;
import android.content.Intent;
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
import com.replon.www.grace_thehealthapp.Reminders.DatabaseHelperReminders;
import com.replon.www.grace_thehealthapp.Reminders.RemindersActivity;

import java.util.ArrayList;

public class RemindersForYouAdapter extends RecyclerView.Adapter<RemindersForYouAdapter.RemindersViewHolder>{

    public static final String TAG = "RemindersForYouAdapter";
//    private Context context;
    private ArrayList<ContentsRemindersForYou> remindersList;
    private DatabaseHelperReminders myDb;
    private Activity activity;

    public RemindersForYouAdapter(Activity activity, ArrayList<ContentsRemindersForYou> remindersList) {
//        this.context = context;
        this.activity = activity;
        this.remindersList = remindersList;
    }



    @NonNull
    @Override
    public RemindersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_reminders_for_you,null);
        RemindersViewHolder holder=new RemindersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final @NonNull RemindersViewHolder holder, int i) {

        final ContentsRemindersForYou contents = remindersList.get(i);
        holder.pill_name.setText(String.valueOf(contents.getPill_name()));
        String num_pills;
        if (contents.getPill_number() == 1){
            num_pills = contents.getPill_number() + " Pill";
        }
        else{
            num_pills = contents.getPill_number() + " Pills";
        }
        holder.pill_number.setText(num_pills);
        if (contents.isPill_taken()){
            holder.pill_taken.setImageResource(R.drawable.ic_pills_taken);
        }
        else{
            holder.pill_taken.setImageResource(R.drawable.ic_pills_not_taken);
        }

        holder.pill_taken_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), RemindersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.getApplicationContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });

    }

    private void updatePillData(String id,String isTaken) {
        myDb=new DatabaseHelperReminders(activity.getApplicationContext());
        boolean isUpdated= myDb.updatePillTakenData(id,isTaken);
        Log.i(TAG,"Data saved");

        if(isUpdated){
            Log.i(TAG,"Reminders Pill Taken data updated");
        }else{
            Log.i(TAG,"Unable to update data");

        }

    }
    @Override
    public int getItemCount() {
        return remindersList.size();

    }

    class RemindersViewHolder extends RecyclerView.ViewHolder {


        TextView pill_name,pill_number;
        ImageView pill_taken;
        LinearLayout pill_taken_layout;


        public RemindersViewHolder(@NonNull View itemView) {
            super(itemView);

            pill_name = (TextView)itemView.findViewById(R.id.pill_name_text);
            pill_number = (TextView)itemView.findViewById(R.id.pills_value_text);
            pill_taken = (ImageView)itemView.findViewById(R.id.pills_taken);
            pill_taken_layout=(LinearLayout)itemView.findViewById(R.id.pill_taken_layout);


        }
    }
}
