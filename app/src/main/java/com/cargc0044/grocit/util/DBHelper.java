package com.cargc0044.grocit.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Nguza Yikona on 4/1/2016.
 * LOCALDB FOR CART
 */
public class DBHelper extends SQLiteOpenHelper {
    public final static int DB_VERSION = 1;
    private final static String DB_NAME = "the_cart.db";
    public static SQLiteDatabase db;
    private static String TAG = "DatabaseHelper"; // just for log cat
    private final Context context;
    private final String TABLE_NAME = "tbl_cart";
    private final String ID = "prod_id";
    private final String MENU_NAME = "prod_name";
    private final String QUANTITY = "qty";
    private final String TOTAL_PRICE = "price";
    private final String IMAGE = "image";
    private final String USER_ID = "usr_id";
    String DB_PATH;


    public DBHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        if(Build.VERSION.SDK_INT >= 17){
                DB_PATH = context.getApplicationInfo().dataDir + "/databases";
        }
        else {
            DB_PATH = constants.DBPath;


        }
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read = null;


        if(dbExist){
            //do nothing - database already exist

        }else{
            db_Read = this.getReadableDatabase();
            db_Read.close();

            try {
                //copy the database from assets folder
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }



    private boolean checkDataBase(){

        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.exists();

    }

    //get from assets and copy
    private void copyDataBase() throws IOException{

        InputStream myInput = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void close() {
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /** this code is used to get all data from database */
    public ArrayList<ArrayList<Object>> getAllData(){
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

        Cursor cursor = null;

        try{
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{USER_ID, ID, MENU_NAME, TOTAL_PRICE, IMAGE, QUANTITY},
                    null,null, null, null, null);
            cursor.moveToFirst();

            if (!cursor.isAfterLast()){
                do{
                    ArrayList<Object> dataList = new ArrayList<Object>();

                    dataList.add(cursor.getLong(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));
                    dataList.add(cursor.getString(3));
                    dataList.add(cursor.getString(4));

                    dataArrays.add(dataList);
                }

                while (cursor.moveToNext());
            }
            cursor.close();
        }catch (SQLException e){
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return dataArrays;
    }

    /** this code is used to get all data from database */
    public boolean isDataExist(String id){
        boolean exist = false;

        Cursor cursor = null;

        try{
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{ID},
                    ID +"="+id,
                    null, null, null, null);
            if(cursor.getCount() > 0){
                exist = true;
            }

            cursor.close();
        }catch (SQLException e){
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return exist;
    }

    /** this code is used to get all data from database */
    public boolean isPreviousDataExist(){
        boolean exist = false;

        Cursor cursor = null;

        try{
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{ID},
                    null,null, null, null, null);
            if(cursor.getCount() > 0){
                exist = true;
            }

            cursor.close();
        }catch (SQLException e){
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return exist;
    }

    public void addData(String usrId, String id, String menu_name, double total_price){
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(USER_ID, usrId);
        values.put(ID, id);
        values.put(MENU_NAME, menu_name);
        values.put(TOTAL_PRICE, total_price);

        // ask the database object to insert the new data
        try{db.insert(TABLE_NAME, null, values);}
        catch(Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }
    public void addData(String usrId, String id, String menu_name, String image, int quantity, double total_price){
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(USER_ID, usrId);
        values.put(ID, id);
        values.put(MENU_NAME, menu_name);
        values.put(IMAGE, image);
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, total_price);

        // ask the database object to insert the new data
        try{db.insert(TABLE_NAME, null, values);}
        catch(Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteData(String id){
        // ask the database manager to delete the row of given id
        try {db.delete(TABLE_NAME, ID + "=" + id, null);}
        catch (Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteAllData(){
        // ask the database manager to delete the row of given id
        try {db.delete(TABLE_NAME, null, null);}
        catch (Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void updateData(String usrId, String id, double total_price){
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(TOTAL_PRICE, total_price);
        values.put(USER_ID, usrId);

        // ask the database object to update the database row of given rowID
        try {db.update(TABLE_NAME, values, ID + "=" + id, null);}
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }
    public void updateData(String usrId, String id, int quantity, double total_price){
        // this is a key value pair holder used by android's SQLite functions
        ContentValues values = new ContentValues();
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, total_price);
        values.put(USER_ID, usrId);

        // ask the database object to update the database row of given rowID
        try {db.update(TABLE_NAME, values, ID + "=" + id, null);}
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }


}