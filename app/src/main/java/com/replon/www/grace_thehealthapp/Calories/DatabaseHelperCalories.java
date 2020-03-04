package com.replon.www.grace_thehealthapp.Calories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DatabaseHelperCalories extends SQLiteOpenHelper {

    //id for this row is today's date

    public static final String DATABASE_NAME="Calories.db";
    public static final String TABLE_NAME="calorie_table";

    public static final String COL_1="ID"; //unique id for each row which is date
    public static final String COL_2="target_calories";
    public static final String COL_3="calories_consumed";
    public static final String COL_4="food_name";


    public static final String TAG = "DatabaseHelperCalories";


    public DatabaseHelperCalories(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME+ "("+COL_1+" TEXT,"+COL_2+" NUMBER,"+COL_3+" NUMBER,"+COL_4+" TEXT)");
        Log.i(TAG,"Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.i(TAG,"onUpgrade running");

    }

    public boolean insertData(int target_calories, int calories_consumed,String food_name){

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        Log.i(TAG,"date is " +formattedDate);

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(COL_1,formattedDate);
        contentValues.put(COL_2,target_calories);
        contentValues.put(COL_3,calories_consumed);
        contentValues.put(COL_4,food_name);



        //if data not inserted it will return -1 else it will show the values

        long result=db.insert(TABLE_NAME,null,contentValues);

        if(result==-1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();


        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return res;
    }

    public Cursor getDataWithDate(String date){
        SQLiteDatabase db=this.getWritableDatabase();
        String[] columns ={COL_1,COL_2,COL_3};

//        Cursor res=db.query(TABLE_NAME,columns,"ID = ?",new String[] { date },null,null,null);
        Cursor res=db.query(TABLE_NAME,columns,"ID = ?",new String[] { date },null,null,null);

        return res;
    }

    public boolean updateData(int target_calories,int calories_consumed,String food_name){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        Log.i(TAG,"date is " +formattedDate);

        contentValues.put(COL_2,target_calories);
        contentValues.put(COL_3,calories_consumed);
        contentValues.put(COL_4,food_name);

        //if data not inserted it will return -1 else it will show the values

        long result=db.update(TABLE_NAME,contentValues,"ID = ?",new String[]{ formattedDate });

        if(result==-1){
            return false;
        }else{
            return true;
        }

    }

    public Integer deleteData(String id){
        SQLiteDatabase db=this.getWritableDatabase();

        return db.delete(TABLE_NAME,"ID = ?",new String[] {id} );
    }
}