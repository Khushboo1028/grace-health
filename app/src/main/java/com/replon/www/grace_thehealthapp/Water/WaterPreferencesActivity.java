package com.replon.www.grace_thehealthapp.Water;

import android.app.IntentService;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WaterPreferencesActivity extends AppCompatActivity {

    private static final String TAG = "WaterPreferencesActivity";
    ImageView back,save;
    GridView gridView;
    WaterGridAdapter gridAdapter;
    TextView glass_value_text;
    EditText et_water_goal;

    String[] units;

    int[] glass_icon = {R.drawable.ic_water_1_glass,R.drawable.ic_water_2_glass,R.drawable.ic_water_bottle,
                        R.drawable.ic_water_full_bottle,R.drawable.ic_water_bottle_1_glass,R.drawable.ic_water_bottle_2_glass};
    String[] glass_values_string = {"200","200","200","200","200","200"};

    int[] glass_values = {200,400,750,1000,1200,1500};

    int pos;


    DatabaseHelperWater myDb;
   // String glass_goal_in_digit,glass_units="mL",glass_quantity;
   // int glasses_drank;
    boolean data_with_date_found;
  //  int total_water_drank;

   // int water_goal,total_quantity_drank;
    CustomDialog customDialog;

    int target_glasses = 8;
    int glasses_drank = 0;
    int total_quantity_drank = 0;
    int glass_size =200;
    String str_glass_size;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), WaterPreferencesActivity.this);

        setContentView(R.layout.activity_water_prefrences);
        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getColor(R.color.darkBlue));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                str_glass_size = glass_values_string[position];
                glass_value_text.setText(str_glass_size);
                save.setVisibility(View.VISIBLE);

                glass_size=  Integer.parseInt(str_glass_size.substring(0,str_glass_size.length()-3));
                Log.i(TAG,"One glass = "+glass_size);

            }
        });


        viewData();

        glass_value_text.setText(String.valueOf(glass_size));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        et_water_goal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                save.setVisibility(View.VISIBLE);

                return false;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!et_water_goal.getText().toString().trim().isEmpty()){
                    target_glasses = Integer.parseInt(et_water_goal.getText().toString().trim());

                    saveData();
                }else{
                    target_glasses=8;
                    saveData();
                }


            }
        });


        glass_values_string = new String[glass_values.length];


        for (int i = 0;i<glass_values.length;i++){
            glass_values_string[i] = String.valueOf(glass_values[i]) + " ml";

        }

        glass_value_text.setText(String.valueOf(glass_size) +" ml");
        gridAdapter = new WaterGridAdapter(getApplicationContext(), glass_icon, glass_values_string);
        gridView.setAdapter(gridAdapter);

    }


    private void init(){

        back = (ImageView) findViewById(R.id.back);
        save = (ImageView) findViewById(R.id.save);
        save.setVisibility(View.GONE);

        myDb=new DatabaseHelperWater(this);
        et_water_goal=(EditText)findViewById(R.id.water_goal_edittext);


        gridView = (GridView) findViewById(R.id.water_pref_grid);
        glass_value_text = (TextView) findViewById(R.id.glass_value_text);

        units = getResources().getStringArray(R.array.water_units_array);
        customDialog = new CustomDialog();

        glass_value_text.setText(glass_values_string[0] +" ml");
    }



    private void saveData() {

        if(!data_with_date_found) {

            boolean isInserted = myDb.insertData(target_glasses, glasses_drank, glass_size, total_quantity_drank);

            if (isInserted) {
                Log.i(TAG, "Water goal saved");
                data_with_date_found = true;
                finish();
            } else {
                Log.i(TAG, "Unable to save water goal");
                customDialog.showMessageOneOption(
                        "Oh Snap!",
                        "Unable to Update Data",
                        R.drawable.ic_error,
                        R.color.darkBlue,
                        "Dismiss",
                        WaterPreferencesActivity.this);

            }
        }else {
            boolean isInserted = myDb.updateData(target_glasses, glasses_drank, glass_size, total_quantity_drank);
            if (isInserted) {
                Log.i(TAG, "Water goal updated");
                finish();
            } else {
                Log.i(TAG, "Unable to update water goal");
                customDialog.showMessageOneOption(
                        "Oh Snap!",
                        "Unable to Update Data",
                        R.drawable.ic_error,
                        R.color.darkBlue,
                        "Dismiss",
                        WaterPreferencesActivity.this);

            }
        }

    }
    private void viewData() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        Log.i(TAG,"date is " +formattedDate);


        if(myDb.getDataWithDate(formattedDate)!=null){

            StringBuffer buffer=new StringBuffer();
            Cursor res = myDb.getDataWithDate(formattedDate);
            if(res.getCount()==0){
                Log.i(TAG,"There is no stored data");
                data_with_date_found=false;
                glasses_drank=0;
                target_glasses=8;
                glass_size=200;
                total_quantity_drank=0;

            }else{
                data_with_date_found=true;
                while(res.moveToNext()){

                    buffer.append("ID: " +res.getString(0)+"\n" );
                    buffer.append("target_glasses: " +res.getInt(1)+"\n");
                    buffer.append("glasses_drank: "+res.getString(2)+"\n");
                    buffer.append("glass_size " +res.getInt(3)+"\n");
                    buffer.append("total_quantity_drank " +res.getInt(4)+"\n\n");


                    glasses_drank=res.getInt(2);
                    target_glasses=res.getInt(1);
                    glass_size=res.getInt(3);
                    total_quantity_drank=res.getInt(4);




                }
//                Log.i(TAG,"glasses drank is "+glasses_drank);

                Log.i(TAG,buffer.toString());

            }

            Cursor res2 = myDb.getAllData();
            if(res2.getCount()==0){
                Log.i(TAG,"There is no stored data");
                glasses_drank=0;
                target_glasses=8;
                glass_size=200;
                total_quantity_drank=0;


            }else{

                while(res2.moveToNext()){

                    target_glasses=res2.getInt(1);
                    glass_size=res2.getInt(3);

                }

                Log.i(TAG,"target_glass is "+target_glasses);
                et_water_goal.setText(String.valueOf(target_glasses));
                glass_value_text.setText(String.valueOf(glass_size) +" ml");



            }
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }
}
