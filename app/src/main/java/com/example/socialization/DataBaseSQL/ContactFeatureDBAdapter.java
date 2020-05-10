package com.example.socialization.DataBaseSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactFeatureDBAdapter {

    public static String TAG = ContactFeatureDBAdapter.class.getSimpleName();
    public static final String DATABASE_NAME = "CallEntry";
    public static final String TABLE_NAME = "CallLogEntry";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "DATE";
    public static final String COL_2 = "CALL_TYPE";
    public static final String COL_3 = "CONTACT_NAME";
    public static final String COL_4 = "CONTACT_PHONE";
    public static final String COL_5 = "DURATION";
    public static final String COL_6 = "LATITUDE";
    public static final String COL_7 = "LONGITUDE";
    public static final String COL_8 = "SOCIAL_STATUS";
//    public static final String COL_9 = "APP_PACKAGE";
    public static int version = 1;
//    private static String TABLE_TO_CREATE ="CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, DAY INTEGER, HOUR INTEGER, LATITUDE REAL, LONGITUDE REAL,CALL_TYPE INTEGER,CONTACT_NAME TEXT,CONTACT_PHONE TEXT, DURATION INTEGER, APP_PACKAGE TEXT)";
    private static String TABLE_TO_CREATE = "create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, DAY INTEGER, HOUR INTEGER, LATITUDE REAL, LONGITUDE REAL,CALL_TYPE INTEGER,CONTACT_NAME TEXT,CONTACT_PHONE TEXT, DURATION INTEGER, APP_PACKAGE TEXT)";

    private Context context;
    private static SQLiteDatabase sqLliteDatabase;
    private static ContactFeatureDBAdapter contactFeatureDBAdapter;


    private ContactFeatureDBAdapter(Context context){
        this.context = context;
        sqLliteDatabase = new ContactFeatureDBHelper(context,DATABASE_NAME,null,version).getWritableDatabase();
    }

    public static ContactFeatureDBAdapter getContactFeatureDBAdapterInstance(Context context){
        if(contactFeatureDBAdapter == null){
            contactFeatureDBAdapter = new ContactFeatureDBAdapter(context);
        }
        return contactFeatureDBAdapter;
    }

    public boolean insert(long date , int callType,
                          String contactName, String phoneNumber, long duration,
                          Double lat, Double lon,
                          Boolean socialStatus){

        long result = -1;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,date);
        contentValues.put(COL_2,callType);
        contentValues.put(COL_3,contactName);
        contentValues.put(COL_4,phoneNumber);
        contentValues.put(COL_5,duration);
        contentValues.put(COL_6,lat);
        contentValues.put(COL_7,lon);
        contentValues.put(COL_8,socialStatus);

        result = sqLliteDatabase.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        return true;
    }

    public boolean delete(int taskID){
        long result = -1;
        result = sqLliteDatabase.delete(TABLE_NAME,COL_0+" = "+taskID,null);
        if(result == -1)
            return false;
        return true;
    }

    public boolean modify(int taskId, String newID){
        int result= -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_0,newID);
        result = sqLliteDatabase.update(TABLE_NAME,contentValues,COL_0+" = "+taskId,null);
        if(result ==-1)
            return false;
        return true;
    }

    /*api's overrides for used by provides*/

    //Will be used in the content provider
    public long insert(ContentValues contentValues){
        return sqLliteDatabase.insert(TABLE_NAME,null,contentValues);
    }

    //Will be used by the provider
    public int delete(String whereClause, String [] whereValues){
        return sqLliteDatabase.delete(TABLE_NAME,whereClause,whereValues);
    }
    //Will be used in the content provider
    public int update(ContentValues contentValues, String s, String [] strings){
        return sqLliteDatabase.update(TABLE_NAME,contentValues, s,strings);
    }

    //Will be used in the content provider
    public Cursor getCursorsForContactFeatures(){
        Cursor cursor = null;
        if(sqLliteDatabase != null){
            cursor = sqLliteDatabase.query(TABLE_NAME,
                    new String[]{COL_0,COL_1,COL_2,COL_3,COL_4,COL_5,COL_6,COL_7,COL_8},
                    null,null,null,null,null,null);
        }
        return cursor;
    }

    public Cursor getCount(){
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = sqLliteDatabase.rawQuery(countQuery,null);
        return cursor;
    }

    public List<Features>  getLast50ContactFeatures(){

        int last50CurPos = 50;
        List<Features> features = new ArrayList<Features>();

        Cursor cursor = sqLliteDatabase.query(TABLE_NAME,
                new String[]{COL_0,COL_1,COL_2,COL_3,COL_4,COL_5,COL_6,COL_7,COL_8},
                null,null,null,null,null,null);

        if(cursor != null && cursor.getCount() > 50 ) { /*Database has 50 entries*/
           if(cursor.moveToLast()){ /* Get Recent 50 entries */
               while (last50CurPos > 0 && cursor.moveToPrevious()){
                   last50CurPos--;
                   Features features1 = new Features(
                           cursor.getLong(0),
                           cursor.getInt(1),
                           cursor.getString(2),
                           cursor.getString(3),
                           cursor.getInt(4),
                           cursor.getDouble(5),
                           cursor.getDouble(6),
                           cursor.getInt(7)
                   );
                   features.add(features1);
               }
           }
        }

        return features;
    }

    /*Query All Features*/
    public List<Features> getAllContactFeatures(){

        List<Features> features = new ArrayList<Features>();

        Cursor cursor = sqLliteDatabase.query(TABLE_NAME,new String[]{COL_0,TABLE_NAME},null,null,null,null,null,null);

        if(cursor != null && cursor.getCount() > 0 ){
            while(cursor.moveToNext()){
            Features features1 = new Features(
                    cursor.getLong(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getDouble(5),
                    cursor.getDouble(6),
                    cursor.getInt(7)
            );
            features.add(features1);
            }
        }
        cursor.close();
        return features;
    }



    private class ContactFeatureDBHelper extends SQLiteOpenHelper {

        public ContactFeatureDBHelper(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_TO_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }
    }


}
