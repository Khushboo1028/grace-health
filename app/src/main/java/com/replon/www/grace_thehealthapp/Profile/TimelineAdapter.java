package com.replon.www.grace_thehealthapp.Profile;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.R;

import java.util.ArrayList;

public class TimelineAdapter  extends RecyclerView.Adapter{


    Activity activity;
    ArrayList<ContentsTimeline> timelineList;

    public TimelineAdapter(Activity activity, ArrayList<ContentsTimeline> timelineList) {
        this.activity = activity;
        this.timelineList = timelineList;
    }

//      @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//        LayoutInflater layoutInflater=LayoutInflater.from(activity.getApplicationContext());
//        View view=layoutInflater.inflate(R.layout.row_timeline_date,null);
//        RecyclerView.ViewHolder holder=new RecyclerView.ViewHolder(view);
//        return holder;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.row_timeline_date, viewGroup, false);
                return new CenterViewHolder(view);
            case 1:
                view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.row_timeline_left, viewGroup, false);
                return new OtherViewHolder(view);
            case 2:
                view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.row_timeline_right, viewGroup, false);
                return new OtherViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return timelineList.get(position).uiPos;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        final ContentsTimeline contents = timelineList.get(i);
        switch (contents.uiPos){
            case 0:
                ((CenterViewHolder)holder).tv_date_month.setText(contents.date);
                break;
            case 1:
            case 2:
                ((OtherViewHolder)holder).tv_date.setText(contents.dateText);
                ((OtherViewHolder)holder).tv_timeline_text.setText(contents.timelineText);

                if (contents.timelineText.equalsIgnoreCase("sick")){
                    ((OtherViewHolder)holder).timeline_rel.setBackground(activity.getDrawable(R.drawable.timeline_sick_bg));
                    ((OtherViewHolder)holder).img_display_timeline.setImageDrawable(activity.getDrawable(R.drawable.ic_pills));
                }else if (contents.timelineText.equalsIgnoreCase("check up")){
                    ((OtherViewHolder)holder).timeline_rel.setBackground(activity.getDrawable(R.drawable.timeline_checkup_bg));
                    ((OtherViewHolder)holder).img_display_timeline.setImageDrawable(activity.getDrawable(R.drawable.ic_checkup));
                }
                else if (contents.timelineText.equalsIgnoreCase("hospital")){
                    ((OtherViewHolder)holder).timeline_rel.setBackground(activity.getDrawable(R.drawable.timeline_hospital_bg));
                    ((OtherViewHolder)holder).img_display_timeline.setImageDrawable(activity.getDrawable(R.drawable.ic_hospital));
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return timelineList.size();
    }

    class CenterViewHolder extends RecyclerView.ViewHolder {


        TextView tv_date_month;


        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date_month = (TextView) itemView.findViewById(R.id.tv_date_month);
        }
    }

    class OtherViewHolder extends RecyclerView.ViewHolder{

        TextView tv_date,tv_timeline_text;
        ImageView img_display_timeline;
        RelativeLayout timeline_rel;


        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_timeline_text = (TextView) itemView.findViewById(R.id.tv_timeline_text);
            img_display_timeline = (ImageView) itemView.findViewById(R.id.img_display_timeline);
            timeline_rel = (RelativeLayout) itemView.findViewById(R.id.timeline_rel);


        }

    }
}
