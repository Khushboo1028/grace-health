package com.replon.www.grace_thehealthapp.SocialMedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.UserDataStore;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public class CommunityFeedActivity extends AppCompatActivity {

    private static final String TAG = "CommunityFeed";
    ImageView back;

    String community_name;
    TextView tv_heading;

    public static final int CREATEPOST = 69;

    //Recycler View
    private RecyclerView recyclerView;
    private PostsAdapter mAdapter;
    private ArrayList<ContentsPost> postList;

    LinearLayout share_something_rel;

    CustomDialog customDialog;

    SwipeRefreshLayout swipeRefreshLayout;
    private RotateLoading rotateLoading;


    UserDataStore userDataStore;
    JSONObject userJSON;
    String auth_token;

    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), CommunityFeedActivity.this);

        setContentView(R.layout.activity_community_feed);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAdapter = new PostsAdapter(this,postList);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getPosts();
            }
        });

        share_something_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreatePostActivity.class);
//                startActivity(intent);
                startActivityForResult(intent,CREATEPOST);
            }
        });

    }

    private void init(){

        back = (ImageView)findViewById(R.id.back);

        community_name = getIntent().getStringExtra("community_name");

        tv_heading = (TextView) findViewById(R.id.tv_heading);
        tv_heading.setText(community_name);

        recyclerView = (RecyclerView) findViewById(R.id.social_recycler_view);
        recyclerView.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        postList = new ArrayList<>();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        rotateLoading = (RotateLoading) findViewById(R.id.rotateLoading);
        userDataStore = new UserDataStore(CommunityFeedActivity.this);

        share_something_rel = (LinearLayout) findViewById(R.id.share_something_rel);

        customDialog = new CustomDialog();

        rq = Volley.newRequestQueue(this);


    }

    private void getPosts(){

        String url = getString(R.string.BASE_URL) + "/posts/"+community_name;
        url = url.replace("#","%23");

        userJSON = userDataStore.readUserData();
        if (userJSON!=null){
            if (!userJSON.has("auth_token")){
                customDialog.showMessageOneOption(
                        "Oh Snap!",
                        "You need to login to see your feed",
                        R.color.pink,
                        R.drawable.ic_error,
                        "Dismiss",
                        CommunityFeedActivity.this);
            }else {

                try {
                    auth_token =userJSON.getString("auth_token");
                }catch (JSONException e){
                    e.printStackTrace();
                }


                rotateLoading.start();
                postList.clear();



                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            JSONArray likes_uid;

                            Log.i(TAG,"RESPONSE MILA HAI "+ response.toString());
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postDetail = response.getJSONObject(i);

                                likes_uid = new JSONArray();

                                String pid = postDetail.getString("pid");
                                String content = postDetail.getString("content");
                                int likes_count = postDetail.getInt("likes_count");
                                String image_url = postDetail.getString("image_url");

                                if(!postDetail.get("likes_uid").equals(null)){
                                    likes_uid = postDetail.getJSONArray("likes_uid");
                                    Log.i(TAG,"likes_uid is "+likes_uid);
                                }

                                String uid = postDetail.getString("uid");
                                String date_created = postDetail.getString("date_created");
                                String community_id = postDetail.getString("community_name");
                                String profile_image_url = postDetail.getString("profile_image_url");
                                String name = postDetail.getString("name");

                                Log.i(TAG,"POST CONTENT "+ content);

                                postList.add(new ContentsPost(pid,content,likes_count,image_url,likes_uid,uid,date_created,community_id,profile_image_url,name,false));

                            }

                            mAdapter.notifyDataSetChanged();
                            rotateLoading.stop();
                            swipeRefreshLayout.setRefreshing(false);


                        }catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                            rotateLoading.stop();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                        rotateLoading.stop();

                    }
                })// OVERRIDE METHODS
                {

                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Content-Type", "application/json");
                        headers.put("x-auth", auth_token);
                        return headers;
                    }

                };

                rq.add(jsonArrayRequest);
            }
        }




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CREATEPOST && resultCode==RESULT_OK){
            getPosts();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }


}


