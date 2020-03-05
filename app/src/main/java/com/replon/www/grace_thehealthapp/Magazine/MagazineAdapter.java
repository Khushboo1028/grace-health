package com.replon.www.grace_thehealthapp.Magazine;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.PostWebView;

import java.util.ArrayList;
import java.util.Random;

public class MagazineAdapter extends RecyclerView.Adapter <MagazineAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<ContentsMagazinePost> postList;

    public MagazineAdapter(Activity activity, ArrayList<ContentsMagazinePost> postList) {
        this.activity = activity;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_magazine,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        final ContentsMagazinePost contentsMagazinePost = postList.get(i);

        holder.topic.setText(contentsMagazinePost.getTopic());
        holder.topic_description.setText(contentsMagazinePost.getTopic_description());
        holder.post_date.setText(contentsMagazinePost.getDate());
        if(contentsMagazinePost.getImage_url().equals("")){
            holder.post_image.setVisibility(View.GONE);

        }else{
            Glide.with(activity.getApplicationContext()).load(contentsMagazinePost.getImage_url()).into(holder.post_image);
        }
        String[] colors = activity.getApplicationContext().getResources().getStringArray(R.array.magazine_colors);
        int rnd = new Random().nextInt(colors.length);
        holder.cardView.setCardBackgroundColor(Color.parseColor(colors[rnd]));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), PostWebView.class);
                intent.putExtra("post_url",contentsMagazinePost.getNews_url());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.getApplicationContext().startActivity(intent);
            }
        });


        holder.options_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showReport(contentsMagazinePost.getNews_url(),i);
            }
        });


    }


    @Override
    public int getItemCount() {
        return postList.size();
    }



    public void showReport(final String url,final int pos) {
        final String report = "Don't want to see such posts!";
        final String share="Share";

        final CharSequence[] items = {report,share};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        //builder.setTitle(addPhoto);


        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(report)) {

                    postList.remove(pos);
                    notifyDataSetChanged();
                    Toast.makeText(activity.getApplicationContext(),"Noted! You won't see such posts anymore",Toast.LENGTH_LONG).show();

                }

                if (items[item].equals(share)) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.getApplicationContext().startActivity(shareIntent);

                }


            }
        });
        builder.show();


    }



    class ViewHolder extends RecyclerView.ViewHolder {


        TextView topic, topic_description, post_date;
        ImageView options_image, post_image;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            topic = (TextView) itemView.findViewById(R.id.topic_name);
            topic_description = (TextView) itemView.findViewById(R.id.news_description);
            post_date = (TextView) itemView.findViewById(R.id.news_date);
            post_image = (ImageView) itemView.findViewById(R.id.news_image);
            options_image = (ImageView) itemView.findViewById(R.id.image_news_options);
            cardView = (CardView) itemView.findViewById(R.id.card_view);


        }
    }

}




