package com.replon.www.grace_thehealthapp.Nearby;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.replon.www.grace_thehealthapp.R;

import java.util.ArrayList;

public class NearbyItemAdapter extends RecyclerView.Adapter <NearbyItemAdapter.ViewHolder>{


    private Activity activity;
    private ArrayList<ContentsNearby> nearbyList;

    public NearbyItemAdapter(Activity activity, ArrayList<ContentsNearby> nearbyList) {
        this.activity = activity;
        this.nearbyList = nearbyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_nearby_item,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final ContentsNearby contentsNearby = nearbyList.get(i);
        holder.place_name.setText(contentsNearby.getName());
        holder.place_address.setText(contentsNearby.getAddress());
        holder.image_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = "tel:"+contentsNearby.getPhone();
                Intent acCall = new Intent(Intent.ACTION_DIAL);
                acCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                acCall.setData(Uri.parse(tel));
                activity.startActivity(acCall);
            }
        });
        holder.mainrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String map = "https://www.google.com/maps/search/?api=1&query="+contentsNearby.getAddress()+"&query_place_id="+contentsNearby.getPlace_id();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView place_name, place_address;
        ImageView image_phone;
        RelativeLayout mainrel;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            place_name = (TextView) itemView.findViewById(R.id.place_name);
            place_address = (TextView) itemView.findViewById(R.id.place_address);
            image_phone = (ImageView) itemView.findViewById(R.id.image_phone);
            mainrel = (RelativeLayout) itemView.findViewById(R.id.mainrel);
        }
    }
}
