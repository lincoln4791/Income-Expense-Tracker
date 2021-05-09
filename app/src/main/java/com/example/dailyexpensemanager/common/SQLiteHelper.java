package com.example.dailyexpensemanager.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.dailyexpensemanager.model.MC_Posts;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyDatabase";
    public static final String MY_TABLE = "MyTable";
    private static final String QUERY_LOAD_ALL_TRANSACTIONS = "SELECT * from "+MY_TABLE+"";


    private Context context;

    public final static String QUERY_CREATE_TABLE = "CREATE TABLE " + MY_TABLE + " (ID integer primary Key autoincrement, postDescription varchar, postCategory varchar, postType varchar, postAmount varchar," +
            "postTime varchar, postDay varchar, postMonth varchar, postYear varchar, dateTime varchar, timeStamp varchar) ";


    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
           db.execSQL(QUERY_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public long saveData(MC_Posts posts){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NodeName.POST_DESCRIPTION,posts.getPostDescription());
        contentValues.put(NodeName.POST_TYPE,posts.getPostType());
        contentValues.put(NodeName.POST_CATEGORY,posts.getPostCategory());
        contentValues.put(NodeName.POST_AMOUNT,posts.getPostAmount());
        contentValues.put(NodeName.POST_TIME,posts.getPostTime());
        contentValues.put(NodeName.POST_YEAR,posts.getPostYear());
        contentValues.put(NodeName.POST_MONTH,posts.getPostMonth());
        contentValues.put(NodeName.POST_DAY,posts.getPostDay());
        contentValues.put(NodeName.POST_DATE_TIME,posts.getPostDateTime());
        contentValues.put(NodeName.TIME_STAMP,posts.getTimeStamp());

        Log.d("tag","amount "+posts.getPostAmount());


        return sqLiteDatabase.insert(MY_TABLE, null, contentValues);
    }


    public Cursor loadAllTransactions(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY_LOAD_ALL_TRANSACTIONS,null);
        return cursor;
    }


    public Cursor loadTypeWise(String type){
        String query = "SELECT * from "+MY_TABLE+" WHERE postType='"+type+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }



    public Cursor loadCategoryWise(String category){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postCategory= '" +category+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }



    public Cursor loadTypeAndCategoryWise(String type, String category){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postType= '" + type + "' AND postCategory= '" +category+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }





    public Cursor loadYearWise(String year){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }


    public Cursor loadYearTypeWise(String year, String type){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postType '"+type+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }



    public Cursor loadYearCategoryWise(String year, String category){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postCategory '"+category+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }


    public Cursor loadYearTypeCategoryWise(String year, String type ,String category){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postCategory '"+category+"' AND postType= '"+type+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }




    public Cursor loadYearMonthWise(String year,String month){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postMonth= '"+month+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }



    public Cursor loadYearMonthTypeWise(String year,String month, String type){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postMonth= '"+month+"' AND postType='"+type+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }




    public Cursor loadYearMonthCategoryWise(String year,String month, String category){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postMonth= '"+month+"' AND postCategory='"+category+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }





    public Cursor loadYearMonthTypeCategoryWise(String year,String month,String type, String category){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postMonth= '"+month+"' AND postCategory='"+category+"' AND postType='"+type+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }





    public Cursor loadYearMonthDayWise(String year,String month,String day){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postMonth= '"+month+"' AND postDay='"+day+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }



    public Cursor loadYearMonthDayTypeWise(String year,String month,String day,String type){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postMonth= '"+month+"' AND postDay='"+day+"' AND postType= '"+type+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }




    public Cursor loadYearMonthDayCategoryWise(String year,String month,String day,String category){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postMonth= '"+month+"' AND postDay='"+day+"' AND postCategory= '"+category+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }




    public Cursor loadYearMonthDayTypeCategoryWise(String year,String month,String day,String type,String category){
        String query = "SELECT * from "+MY_TABLE+ " WHERE postYear= '" +year+"' AND postMonth= '"+month+"' AND postDay='"+day+"' AND postType= '"+type+"' AND postCategory= '"+category+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }











}
