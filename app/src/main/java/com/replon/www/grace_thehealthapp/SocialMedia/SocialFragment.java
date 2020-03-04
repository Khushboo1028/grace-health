package com.replon.www.grace_thehealthapp.SocialMedia;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.UserDataStore;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SocialFragment extends Fragment {


    public static final String TAG = "SocialFragment";

    public static final int CREATEPOST = 69;

    //Recycler View
    private RecyclerView recyclerView;
    private PostsAdapter mAdapter;
    private ArrayList<ContentsPost> postList;
    private View view;

    LinearLayout share_something_rel;

    CustomDialog customDialog;

    SwipeRefreshLayout swipeRefreshLayout;
    private RotateLoading rotateLoading;


    UserDataStore userDataStore;
    JSONObject userJSON;
    String auth_token;

    ImageView image_community;

    private RequestQueue rq;


    public SocialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), getActivity());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_social, container, false);

        init();



//        postList.add(new ContentsPost("Swapnil Chauhan",
//                "",
//                "69",
//                "All healing is essentially a release from fear.!!!! \nPlease get well soon My sons…❤️",
//                "Thu, Oct 31, 2019 at 08:49 PM",
//                true,
//                true,
//                "",
//                "https://firebasestorage.googleapis.com/v0/b/replontest.appspot.com/o/profile_images%2FAA18F79B-EA2A-4C0E-B813-7944B1BF4EB3.jpg?alt=media&token=4d5483b4-74d7-45c4-9191-427b046c2130"));
//
//        postList.add(new ContentsPost("Rahul Gala",
//                "#Alzheimers",
//                "0",
//                "All healing is essentially a release from fear.!!!! \nPlease get well soon My sons…❤️",
//                "Thu, Oct 31, 2019 at 08:49 PM",
//                false,
//                false,
//                "https://firebasestorage.googleapis.com/v0/b/replontest.appspot.com/o/notices_files%2F0f8f0d05-29b3-4dd8-a573-944499cce182?alt=media&token=946ce120-6580-49de-b750-766a44f42e88",
//                ""));
//        postList.add(new ContentsPost("Rahul Gala",
//                "#Alzheimers",
//                "0",
//                "All healing is essentially a release from fear.!!!! \nPlease get well soon My sons…❤️",
//                "Thu, Oct 31, 2019 at 08:49 PM",
//                false,
//                false,
//                "",
//                ""));
//        postList.add(new ContentsPost("Rahul Gala",
//                "#Alzheimers",
//                "0",
//                "All healing is essentially a release from fear.!!!! \nPlease get well soon My sons…❤️",
//                "Thu, Oct 31, 2019 at 08:49 PM",
//                false,
//                false,
//                "",
//                ""));
//        postList.add(new ContentsPost("Rahul Gala",
//                "#Alzheimers",
//                "0",
//                "All healing is essentially a release from fear.!!!! \nPlease get well soon My sons…❤😇️",
//                "Thu, Oct 31, 2019 at 08:49 PM",
//                false,
//                false,
//                "",
//                ""));
//        postList.add(new ContentsPost("Rahul Gala",
//                "#Alzheimers",
//                "0",
//                "All healing is essentially a release from fear.!!!! \nPlease get well soon My sons…❤️",
//                "Thu, Oct 31, 2019 at 08:49 PM",
//                false,
//                false,
//                "",
//                ""));

        mAdapter = new PostsAdapter(getActivity(),postList);
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
                Intent intent = new Intent(getActivity(),CreatePostActivity.class);
//                startActivity(intent);
                startActivityForResult(intent,CREATEPOST);
            }
        });

        image_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CommunityViewActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });



        return view;
    }

    private void init(){
        postList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.social_recycler_view);
        recyclerView.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        rotateLoading = (RotateLoading) view.findViewById(R.id.rotateLoading);
        userDataStore = new UserDataStore(getActivity());

        image_community = (ImageView) view.findViewById(R.id.image_community);

        share_something_rel = (LinearLayout) view.findViewById(R.id.share_something_rel);

        customDialog = new CustomDialog();

        rq = Volley.newRequestQueue(getActivity());
    }

    private void getPosts(){

        String url = getString(R.string.BASE_URL) + "/post";


        userJSON = userDataStore.readUserData();
        if (userJSON!=null){
                if (!userJSON.has("auth_token")){
                    customDialog.showMessageOneOption(
                            "Oh Snap!",
                            "You need to login to see your feed",
                            R.color.pink,
                            R.drawable.ic_error,
                            "Dismiss",
                            getActivity());
                }else {

                    try {
                        auth_token =userJSON.getString("auth_token");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }


                    rotateLoading.start();
                    postList.clear();

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
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
    public void onPause() {
        super.onPause();
        recyclerView.setLayoutFrozen(true);
        Log.i(TAG,"In On PAUSE");
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutFrozen(false);
        Log.i(TAG,"In On RESUME");
    }
}
