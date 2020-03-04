package com.replon.www.grace_thehealthapp.Water;

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


public class DatabaseHelperWater extends SQLiteOpenHelper {

    //id for this row is today's date

    public static final String DATABASE_NAME="Water.db";
    public static final String TABLE_NAME="Water_table";

    public static final String COL_1="ID"; //unique id for each row which is date
    public static final String COL_2="target_glasses"; // set by user that he has to drink x glasses
    public static final String COL_3="glasses_drank"; // added by user
    public static final String COL_4="glass_size";  // set by user saying one glass is how many ml
    public static final String COL_5="total_quantity_drank"; //units drank till now = total_quantity_drank + addedDataInMlByUser


    public static final String TAG = "DatabaseHelperWater";


    public DatabaseHelperWater(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME+ "("+COL_1+" TEXT,"+COL_2+" NUMBER,"+COL_3+" NUMBER,"+COL_4+" NUMBER,"+COL_5+" NUMBER)");
        Log.i(TAG,"Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.i(TAG,"onUpgrade running");

    }

    public boolean insertData(int target_glasses, int glasses_drank,int glass_size,int total_quantity_drank){

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        Log.i(TAG,"date is " +formattedDate);

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(COL_1,formattedDate);
        contentValues.put(COL_2,target_glasses);
        contentValues.put(COL_3,glasses_drank);
        contentValues.put(COL_4,glass_size);
        contentValues.put(COL_5,total_quantity_drank);


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
        String[] columns ={COL_1,COL_2,COL_3,COL_4,COL_5};

//        Cursor res=db.query(TABLE_NAME,columns,"ID = ?",new String[] { date },null,null,null);
        Cursor res=db.query(TABLE_NAME,columns,"ID = ?",new String[] { date },null,null,null);

        return res;
    }

    public boolean updateData(int target_glasses, int glasses_drank,int glass_size,int total_quantity_drank){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        Log.i(TAG,"date is " +formattedDate);

        contentValues.put(COL_2,target_glasses);
        contentValues.put(COL_3,glasses_drank);
        contentValues.put(COL_4,glass_size);
        contentValues.put(COL_5,total_quantity_drank);


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