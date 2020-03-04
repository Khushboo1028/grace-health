package com.replon.www.grace_thehealthapp.Magazine;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MagazineFragment extends Fragment {


    public static final String TAG = "MagazineFrag";

    //Recycler View
    private RecyclerView recyclerView;
    private MagazineAdapter mAdapter;
    private ArrayList<ContentsMagazinePost> postList;

    private RotateLoading rotateLoading;
    private RequestQueue rq;

    View view;


    private SwipeRefreshLayout swipeRefreshLayout;


    public MagazineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), getActivity());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_magazine, container, false);

        init();





//        postList.add(new ContentsMagazinePost("10 of the most popular misconceptions about drinks debunked",
//                "Certain beverages have been advertised as quick solutions to various health ailments like poor sleep, heart problems, and high blood sugar. Despite what many people may think, these drinks don't always provide health benefits. We asked dietitian and nutrition",
//                "Thu, Oct 31, 2019 at 08:49 PM",
//                "https://image.businessinsider.com/5af0bd5c42e1cc442748683f?width=1200&format=jpeg",
//                "https://www.insider.com/drinks-healthy-myths-debunked-wine-alcohol-coffee-juice-2019-10"));
//
//        postList.add(new ContentsMagazinePost("Best health and fitness apps Sports Interactive Network Philippines",
//                "Since smartphones have become the handy extensions of ourselves, why not maximize it by getting a head start on fitness? As you feast your eyes on the latest social media trends, fuel your body with the perfect workout through the right training phone applic…",
//                "Thu, Oct 31, 2019 at 08:49 PM",
//                "http://contents.spin.ph/image/resize/image/2019/09/30/main-image-640-1-1569849566.webp",
//                "https://www.spin.ph/life/active-lifestyle/best-fitness-health-apps-a2442-20190930"));
//
//        postList.add(new ContentsMagazinePost("Jason Aldean's Wife Brittany Says She Got Back into Shape After Baby by Being 'Naturally Active'",
//                "Brittany Kerr Aldean struggled with getting back into shape following the birth of her daughter in February. Brittany and her husband Jason Aldean welcomed Navy Rome just 14 months after the birth of their son Memphis, and Brittany said she prefers more “nat……",
//                "Thu, Oct 31, 2019 at 08:49 PM",
//                "https://peopledotcom.files.wordpress.com/2019/09/brittany-aldean-1.jpg?crop=21px%2C39px%2C1461px%2C767px&resize=1200%2C630",
//                "https://people.com/health/brittany-aldean-naturally-active-fitness-routine-post-baby/"));



        mAdapter = new MagazineAdapter(getActivity(),postList);
        recyclerView.setAdapter(mAdapter);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                getNews();
            }
        });





        return view;
    }

    private void init(){

        postList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.magazine_recycler_view);

        recyclerView.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        rotateLoading = (RotateLoading) view.findViewById(R.id.rotateLoading);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        rq = Volley.newRequestQueue(getActivity());
    }


    private void getNews(){
        rotateLoading.start();
        postList.clear();

        String url = "https://newsapi.org/v2/everything?q=health+gym+diet&sortBy=publishedAt&apiKey=30ab4c35c7bd4856aeba0be360051020";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArr = response.getJSONArray("articles");

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject newsDetail = jsonArr.getJSONObject(i);

                        String imgUrl = newsDetail.getString("urlToImage");
                        String title = newsDetail.getString("title");
                        String detail = newsDetail.getString("description");
                        String newsUrl = newsDetail.getString("url");
                        String content = newsDetail.getString("content");
                        String date = newsDetail.getString("publishedAt");

                        postList.add(new ContentsMagazinePost(title,detail,date,imgUrl,newsUrl));

                    }


                    mAdapter.notifyDataSetChanged();
                    rotateLoading.stop();
                    recyclerView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
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
        });

        rq.add(request);
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

    @Override
    public void onStart() {
        super.onStart();
        getNews();

        Log.i(TAG,"In On START");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"In On START");
    }
}
