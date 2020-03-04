package com.replon.www.grace_thehealthapp.Emergency;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.replon.www.grace_thehealthapp.R;

import java.util.ArrayList;

public class EmergencyDataAdapter extends RecyclerView.Adapter<EmergencyDataAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<ContentsEmergencyData> dataList;

    public EmergencyDataAdapter(Activity activity, ArrayList<ContentsEmergencyData> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_emergency_data,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final ContentsEmergencyData contents = dataList.get(i);
        holder.name.setText(contents.getName());


        if (contents.getPlace().equals("0")){
            holder.place.setText("in Emergency Numbers");
            holder.circle_view.setBackground(activity.getDrawable(R.drawable.emergency_numbers_circle));
            holder.next_arrow.setImageDrawable(activity.getDrawable(R.drawable.ic_call_blue));
            holder.main_img.setImageDrawable(activity.getDrawable(R.drawable.ic_search_white));
        }
        else if (contents.getPlace().equals("1")){
            holder.place.setText("in First Aid");
            holder.circle_view.setBackground(activity.getDrawable(R.drawable.first_aid_circle));
            holder.next_arrow.setImageDrawable(activity.getDrawable(R.drawable.ic_arrow_forward_blue));
            holder.main_img.setImageDrawable(activity.getDrawable(contents.getImage()));

        }
        else if (contents.getPlace().equals("2")){
            holder.place.setText("in Safety Tips");
            holder.circle_view.setBackground(activity.getDrawable(R.drawable.natural_disasters_circle));
            holder.next_arrow.setImageDrawable(activity.getDrawable(R.drawable.ic_arrow_forward_blue));
            holder.main_img.setImageDrawable(activity.getDrawable(contents.getImage()));

        }
        else if (contents.getPlace().equals("3")){
            holder.place.setText("in Natural Disasters");
            holder.circle_view.setBackground(activity.getDrawable(R.drawable.safety_tips_circle));
            holder.next_arrow.setImageDrawable(activity.getDrawable(R.drawable.ic_arrow_forward_blue));
            holder.main_img.setImageDrawable(activity.getDrawable(contents.getImage()));

        }

        if (activity instanceof SearchActivity){
            holder.place.setVisibility(View.VISIBLE);

        }else {
            holder.place.setVisibility(View.GONE);
            holder.name.setPadding(0,50,0,0);
            if (activity instanceof FirstAid){
                holder.circle_view.setBackground(activity.getDrawable(R.drawable.first_aid_circle));
            }else if(activity instanceof NaturalDisasters){
                holder.circle_view.setBackground(activity.getDrawable(R.drawable.natural_disasters_circle));
            }else if (activity instanceof SafetyTips){
                holder.circle_view.setBackground(activity.getDrawable(R.drawable.safety_tips_circle));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (contents.getPlace().equals("0")) {
                    Uri call = Uri.parse("tel:" + contents.getDescription());
                    Intent intent = new Intent(Intent.ACTION_DIAL, call);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }else {
                    //GOTO OTHER ACTIVITY
                    Intent intent = new Intent(activity,EmergencyDataDisplay.class);
                    intent.putExtra("heading",contents.getName());
                    intent.putExtra("place",contents.getPlace());
                    intent.putExtra("description",contents.getDescription());
                    intent.putExtra("url",contents.getURL());
                    activity.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateList(ArrayList<ContentsEmergencyData> list){
        dataList = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView name,place;
        FrameLayout circle_view;
        ImageView next_arrow,main_img;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.tv_name);
            place = (TextView)itemView.findViewById(R.id.tv_place);

            circle_view = (FrameLayout) itemView.findViewById(R.id.circle_view);

            next_arrow = (ImageView) itemView.findViewById(R.id.next);
            main_img = (ImageView) itemView.findViewById(R.id.main_img);
        }
    }
}
