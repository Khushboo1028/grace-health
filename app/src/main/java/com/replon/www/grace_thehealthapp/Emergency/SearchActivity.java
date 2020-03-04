package com.replon.www.grace_thehealthapp.Emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    public static final String TAG = "SearchActivity";
    ImageView cancel,search_img;
    EditText et_search;

    RelativeLayout mainrel, super_layout,search_rel;
    LinearLayout layout_search_initial;

    TextView tv_search_for_heading, tv_search_1,tv_search_2,tv_search_3,tv_search_4,tv_search_5,tv_search_6,tv_search_7,tv_search_8,tv_search_9;

    RecyclerView recyclerView;
    ArrayList<ContentsEmergencyData> dataList, searchList;
    EmergencyDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), SearchActivity.this);

        setContentView(R.layout.activity_search);

        init();


        mainrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_search.getText().toString() == null || et_search.getText().toString().equals("")){
                     onBackPressed();
                }else{
                    et_search.getText().clear();
                    super_layout.setBackgroundColor(getColor(R.color.white));
                }
            }
        });

        dataList = this.getIntent().getExtras().getParcelableArrayList("dataList");

        emptySearchList();

        adapter = new EmergencyDataAdapter(this, searchList);

        recyclerView.setAdapter(adapter);


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString().toLowerCase().trim());
                if (s.toString().equals("")){
                    changeBackground("69");
                    tv_search_for_heading.setText("Search for");
                    emptySearchList();
                }
            }
        });

        tvSearchClick();


    }

    private void filter(String text){
        //new array list that will hold the filtered data
        ArrayList<ContentsEmergencyData> temp = new ArrayList<>();

        //looping through existing elements
        for (ContentsEmergencyData s : dataList) {
            //if the existing elements contains the search input
            if (s.getKeywords().contains(text)) {
                //adding the element to filtered list
                temp.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.updateList(temp);
        if(!temp.isEmpty()) {
            changeBackground(temp.get(0).getPlace());
            recyclerView.setVisibility(View.VISIBLE);
            layout_search_initial.setVisibility(View.GONE);
        }
        else {
            changeBackground("69");
            emptySearchList();
            tv_search_for_heading.setText("Oops:( Couldn't find what you are looking for. Try Again!");
        }

    }

    private void tvSearchClick(){
        tv_search_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(tv_search_1.getText().toString());
            }
        });

        tv_search_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(tv_search_2.getText().toString());
            }
        });

        tv_search_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(tv_search_3.getText().toString());
            }
        });

        tv_search_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(tv_search_4.getText().toString());
            }
        });

        tv_search_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(tv_search_5.getText().toString());
            }
        });

        tv_search_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(tv_search_6.getText().toString());
            }
        });

        tv_search_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(tv_search_7.getText().toString());
            }
        });

        tv_search_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(tv_search_8.getText().toString());
            }
        });

        tv_search_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(tv_search_9.getText().toString());
            }
        });


    }

    private void emptySearchList(){
        searchList=new ArrayList<>();

//        searchList.add(new ContentsEmergencyData("National Emergency Helpline","0","112","national,emergency,helpline,112"));
//        searchList.add(new ContentsEmergencyData("Artificial Respiration", "1", "LONG TEXT HERE", "artificial,respiration,throat,breathe,chest,nose,airway,breathing,blowing,mouth,resuscitation,air,mouth to mouth,inhale,exhale,revive,passage,oxygen"));
//        if (adapter==null){
//            Log.i(TAG,"MAKING NEW ADAPTER");
//            adapter = new EmergencyDataAdapter(this, searchList);
//        }else{
//            Log.i(TAG,"Changing ADAPTER" + searchList.size());
//            adapter.updateList(searchList);
//        }

        recyclerView.setVisibility(View.GONE);
        layout_search_initial.setVisibility(View.VISIBLE);


    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainrel.getWindowToken(), 0);
    }


    private void changeBackground(String xxx) {


        if(!xxx.equals("69")){

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (xxx.equals("0")) {
                super_layout.setBackground(getDrawable(R.drawable.emergency_numbers_gradient));
                window.setStatusBarColor(this.getResources().getColor(R.color.emergencyNumbersStartColor));
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else if (xxx.equals("1")) {
                super_layout.setBackground(getDrawable(R.drawable.first_aid_gradient));
                window.setStatusBarColor(this.getResources().getColor(R.color.firstAidStartColor));
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else if (xxx.equals("2")) {
                super_layout.setBackground(getDrawable(R.drawable.safety_tips_gradient));
                window.setStatusBarColor(this.getResources().getColor(R.color.safetyTipsStartColor));
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else if (xxx.equals("3")) {
                super_layout.setBackground(getDrawable(R.drawable.natural_disasters_gradient));
                window.setStatusBarColor(this.getResources().getColor(R.color.safetyTipsStartColor));
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            et_search.setTextColor(getColor(R.color.white));
            search_img.setImageDrawable(getDrawable(R.drawable.ic_search_white));
            cancel.setImageDrawable(getDrawable(R.drawable.ic_cancel_white));
            search_rel.setBackground(getDrawable(R.drawable.white_border_rectangle));

        }
        else{
            super_layout.setBackgroundColor(getColor(R.color.white));
            et_search.setTextColor(getColor(R.color.navyBlueText));
            search_img.setImageDrawable(getDrawable(R.drawable.ic_search_blue));
            cancel.setImageDrawable(getDrawable(R.drawable.ic_cancel_navy_blue));
            search_rel.setBackground(getDrawable(R.drawable.navy_border_rectangle));
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }

    private void init(){
        et_search = (EditText) findViewById(R.id.et_search);

        cancel = (ImageView) findViewById(R.id.cancel);
        search_img = (ImageView)findViewById(R.id.search_img);

        mainrel = (RelativeLayout) findViewById(R.id.mainrel);
        super_layout = (RelativeLayout) findViewById(R.id.super_layout);
        search_rel = (RelativeLayout) findViewById(R.id.search_rel);

        layout_search_initial = (LinearLayout) findViewById(R.id.layout_search_initial);

        tv_search_for_heading = (TextView) findViewById(R.id.tv_search_for_heading);
        tv_search_1 = (TextView) findViewById(R.id.tv_search_1);
        tv_search_2 = (TextView) findViewById(R.id.tv_search_2);
        tv_search_3 = (TextView) findViewById(R.id.tv_search_3);
        tv_search_4 = (TextView) findViewById(R.id.tv_search_4);
        tv_search_5 = (TextView) findViewById(R.id.tv_search_5);
        tv_search_6 = (TextView) findViewById(R.id.tv_search_6);
        tv_search_7 = (TextView) findViewById(R.id.tv_search_7);
        tv_search_8 = (TextView) findViewById(R.id.tv_search_8);
        tv_search_9 = (TextView) findViewById(R.id.tv_search_9);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        dataList=new ArrayList<>();
        searchList = new ArrayList<>();




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
