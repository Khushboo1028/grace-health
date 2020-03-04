package com.replon.www.grace_thehealthapp.Reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import android.util.Log;


public class DatabaseHelperReminders extends SQLiteOpenHelper {



    public static final String DATABASE_NAME="Reminders.db";
    public static final String TABLE_NAME="Reminder_table";

    public static final String COL_1="ID";
    public static final String COL_2="name_of_dose";
    public static final String COL_3="time_of_dose";
    public static final String COL_4="hour_of_dose";
    public static final String COL_5="how_often";
    public static final String COL_6="pills_in_dose";
    public static final String COL_7="pills_taken";

    public static final String TAG = "DatabaseHelperReminders";


    public DatabaseHelperReminders(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

//        SQLiteDatabase db=this.getWritableDatabase(); //just for checking
    }


    @Override
    public void onCreate(SQLiteDatabase db) {




        db.execSQL("CREATE TABLE "+TABLE_NAME+ "("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+" TEXT,"+COL_3+" TEXT,"+COL_4+" TEXT,"+COL_5+" TEXT,"+COL_6+" TEXT ,"+COL_7+" TEXT)");
        Log.i(TAG,"Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.i(TAG,"onUpgrade running");

    }

    public boolean insertData(String name_of_dose, String time_of_dose, String hour_of_day, String how_often,String pills_in_dose,String pill_taken){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(COL_2,name_of_dose);
        contentValues.put(COL_3,time_of_dose);
        contentValues.put(COL_4,hour_of_day);
        contentValues.put(COL_5,how_often);
        contentValues.put(COL_6,pills_in_dose);
        contentValues.put(COL_7,pill_taken);


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


    public Cursor getDataWithHour(String hour_of_dose){
        SQLiteDatabase db=this.getWritableDatabase();
        String[] columns ={COL_1, COL_2,COL_3,COL_4,COL_5,COL_6,COL_7};

        Cursor res=db.query(TABLE_NAME,columns,"hour_of_dose = ?",new String[] { hour_of_dose },null,null,null);
        return res;
    }

    public Cursor getDataWithDay(String day){
        SQLiteDatabase db=this.getWritableDatabase();
        String[] columns ={COL_1, COL_2,COL_3,COL_4,COL_5,COL_6,COL_7};

        Cursor res = db.query(TABLE_NAME,columns, COL_5 + " LIKE ? " , new String[] {"%" + day + "%"}, null, null, null);
        return res;
    }

    public Cursor getDataWithHourAndDay(String hour_of_dose,String day){
        SQLiteDatabase db=this.getWritableDatabase();
        String[] columns ={COL_1, COL_2,COL_3,COL_4,COL_5,COL_6,COL_7};
        Cursor res = db.query(TABLE_NAME,columns, COL_5 + " LIKE ? AND " +COL_4+ "= ?" , new String[] {"%" + day + "%",hour_of_dose}, null, null, null);

        return res;
    }

    public boolean updatePillTakenData(String id,String isPillTaken){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(COL_7,isPillTaken);

//        db.insert(TABLE_NAME,null,contentValues);

        //if data not inserted it will return -1 else it will show the values

        long result=db.update(TABLE_NAME,contentValues,"ID = ?",new String[]{ id });

        if(result==-1){
            return false;
        }else{
            return true;
        }

    }

    public boolean deleteData(String id){

        SQLiteDatabase db=this.getWritableDatabase();

        long result = db.delete(TABLE_NAME,"ID = ?",new String[] {id} );

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
}