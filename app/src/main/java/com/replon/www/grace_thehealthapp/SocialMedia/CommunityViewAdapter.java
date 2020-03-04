package com.replon.www.grace_thehealthapp.SocialMedia;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.replon.www.grace_thehealthapp.R;

import java.util.ArrayList;
import java.util.List;

public class CommunityViewAdapter extends RecyclerView.Adapter <CommunityViewAdapter.ViewHolder>{

    private Activity activity;
    private ArrayList<String> arrayList;
    private ArrayList<String> colors;

    public CommunityViewAdapter(Activity activity, ArrayList<String> arrayList, ArrayList<String> colors) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.colors = colors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_community_view,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        final String contents = arrayList.get(i);

        holder.tv_community_name.setText(contents);

        holder.tv_community_name.getBackground().setTint(Color.parseColor(colors.get(i)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CommunityFeedActivity.class);
                intent.putExtra("community_name",contents);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder {


        TextView tv_community_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_community_name = (TextView) itemView.findViewById(R.id.tv_community_name);

        }
    }



}
