package com.replon.www.grace_thehealthapp.SocialMedia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.UserDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostsAdapter extends RecyclerView.Adapter <PostsAdapter.ViewHolder>{

    public static final String TAG = "PostsAdapter";
    private Activity activity;
    private ArrayList<ContentsPost> postList;
    String auth_token;
    private RequestQueue rq;

    private UserDataStore userDataStore;
    private CustomDialog customDialog;

    public PostsAdapter(Activity activity, ArrayList<ContentsPost> postList) {
        this.activity = activity;
        this.postList = postList;
        userDataStore = new UserDataStore(this.activity);
        customDialog = new CustomDialog();
        rq = Volley.newRequestQueue(this.activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity);
        View view=layoutInflater.inflate(R.layout.row_social_media,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        final ContentsPost contentsPost = postList.get(i);
        holder.post_user_name.setText(contentsPost.getName());
        holder.post_text.setText(contentsPost.getContent());
        holder.post_community.setText(" ➤ "+ contentsPost.getCommunity_name());

        holder.post_date.setText(setDateFormat(contentsPost.getDate_created()));
        if(contentsPost.getProfile_image_url().equals("")){
            holder.post_profile_image.setImageResource(R.drawable.ic_default_profile_image);
        }else{
            Glide.with(activity.getApplicationContext()).load(contentsPost.getProfile_image_url()).placeholder(R.drawable.ic_default_profile_image).into(holder.post_profile_image);
        }
        holder.post_likes_count.setText(contentsPost.likes_count + " likes");
        if(contentsPost.getImage_url().equals(null) || contentsPost.getImage_url().equals("")){
            holder.post_image.setVisibility(View.GONE);
        }else{
            Glide.with(activity.getApplicationContext()).load(contentsPost.getImage_url()).into(holder.post_image);
        }

        JSONObject userJSON = userDataStore.readUserData();
        String uid = "";

        if (!userJSON.has("uid")){
            customDialog.showMessageOneOption(
                    "Oh Snap!",
                    "You need to login to see likes",
                    R.color.pink,
                    R.drawable.ic_error,
                    "Dismiss",
                    activity);
        }else{

            int j=0;



            for (j=0;j<contentsPost.getLikes_uid().length();j++){
                try {
                    if (contentsPost.getLikes_uid().toString().contains(userJSON.getString("uid")))
                    {
                        holder.liked_image.setImageResource(R.drawable.ic_liked);
                        contentsPost.setLiked_boolean(true);
                    }
                    else {
                        holder.liked_image.setImageResource(R.drawable.ic_not_liked);
                        contentsPost.setLiked_boolean(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (j==0){
                holder.liked_image.setImageResource(R.drawable.ic_not_liked);
            }
        }




        holder.liked_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contentsPost.getLiked_boolean()){
                    holder.post_likes_count.setText(contentsPost.getLikes_uid().length()+1 + " likes");
                    holder.liked_image.setImageResource(R.drawable.ic_liked);
                    contentsPost.setLiked_boolean(true);
                    likePost(contentsPost.getPid());
                }

            }
        });

        holder.post_likes_count.setText(contentsPost.getLikes_uid().length() + " likes");

        holder.options_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showReport();
            }
        });


    }

    private void likePost(String pid){
        String url = activity.getString(R.string.BASE_URL) + "/post/like";

        JSONObject paramJson = new JSONObject();

        try {
            paramJson.put("pid", pid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject userJSON = userDataStore.readUserData();


        if (userJSON!=null) {
            if (!userJSON.has("auth_token")) {
                customDialog.showMessageOneOption(
                        "Oh Snap!",
                        "You need to login to see your feed",
                        R.color.pink,
                        R.drawable.ic_error,
                        "Dismiss",
                        activity);
            } else {

                try {
                    auth_token = userJSON.getString("auth_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, paramJson, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "LIKE HO GAYA");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i(TAG,"SOME ERROR: " + error.getMessage());
                    }
                })
                {
                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Content-Type", "application/json");
                        headers.put("x-auth", auth_token);
                        return headers;
                    }
                };

                rq.add(jsonObjectRequest);



            }
        }


    }


    private String setDateFormat(String date){
        long dateMills = Long.parseLong(date);

        // Creating date format
        DateFormat simple = new SimpleDateFormat("EEE, MMM dd, yyyy hh:mm aa");

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(dateMills);

        // Formatting Date according to the
        // given format
        return simple.format(result);
    }


    public void showReport() {
        final String report = "Report this user!";

        final CharSequence[] items = {report};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        //builder.setTitle(addPhoto);


        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(report)) {

                    Toast.makeText(activity.getApplicationContext(),"Thank you for your feedback",Toast.LENGTH_LONG).show();

                }

            }
        });
        builder.show();


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


    TextView post_user_name, post_community, post_text, post_date, post_likes_count;
    ImageView  post_image, bookmark_image, liked_image, options_image;
    CircularImageView post_profile_image;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        post_user_name = (TextView) itemView.findViewById(R.id.post_name_text);
        post_community = (TextView) itemView.findViewById(R.id.community_name_text);
        post_text = (TextView) itemView.findViewById(R.id.post_text);
        post_date = (TextView) itemView.findViewById(R.id.post_date);
        post_likes_count = (TextView) itemView.findViewById(R.id.post_likes_count);
        post_profile_image = (CircularImageView) itemView.findViewById(R.id.users_profile_image);
        post_image = (ImageView) itemView.findViewById(R.id.post_image);
        bookmark_image = (ImageView) itemView.findViewById(R.id.post_bookmark);
        liked_image = (ImageView) itemView.findViewById(R.id.post_liked);
        options_image = (ImageView) itemView.findViewById(R.id.image_post_options);


    }
}
}

