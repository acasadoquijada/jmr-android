package com.example.alejandro.jmr_android.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by alejandro on 08/09/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Descriptor.db";
    public static final String SINGLE_COLOR_TABLE_NAME = "SingleColorDescription";
    public static final String STRUCTURE_COLOR_TABLE_NAME = "MPEG7ColorStructure";
    public static final String IMAGE_PATH = "imagePath";
    public static final String VALUE = "value";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                SINGLE_COLOR_TABLE_NAME + " (" +
                IMAGE_PATH + " TEXT PRIMARY KEY ," +
                "RED" + " INTEGER NOT NULL ," +
                "GREEN" + " INTEGER NOT NULL ," +
                "BLUE" + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " +
                STRUCTURE_COLOR_TABLE_NAME + " (" +
                IMAGE_PATH + " TEXT PRIMARY KEY ," +
                VALUE + " DOUBLE NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertValue (String imageName, double value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_PATH, imageName);
        contentValues.put(VALUE, value);
        Log.d("INSERTO", imageName);
        db.insert(SINGLE_COLOR_TABLE_NAME, null, contentValues);
        db.insert(STRUCTURE_COLOR_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertSingleColorValues(String imageName, int r, int g, int b){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_PATH, imageName);
        contentValues.put("RED", r);
        contentValues.put("GREEN", g);
        contentValues.put("BLUE", b);
        Log.d("INSERTO", imageName);
        db.insert(SINGLE_COLOR_TABLE_NAME, null, contentValues);
        return true;
    }

    public int[] getSingleColorData(String imageName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        int rgb[] = {-1,-1,-1};

        cursor =  db.rawQuery
                ("SELECT * FROM " + SINGLE_COLOR_TABLE_NAME + " WHERE " +
                        IMAGE_PATH +" = '"+imageName+"'", null);

        if (cursor.moveToFirst()){
            rgb[0] = cursor.getInt(cursor.getColumnIndex("RED"));
            rgb[1] = cursor.getInt(cursor.getColumnIndex("GREEN"));
            rgb[2] = cursor.getInt(cursor.getColumnIndex("BLUE"));
            return rgb;
        }

        return null;
    }

    public int numberOfRows(String database){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows;
        switch (database){
            case SINGLE_COLOR_TABLE_NAME:
                numRows = (int) DatabaseUtils.queryNumEntries(db, SINGLE_COLOR_TABLE_NAME);
                break;
            case STRUCTURE_COLOR_TABLE_NAME:
                numRows = (int) DatabaseUtils.queryNumEntries(db, STRUCTURE_COLOR_TABLE_NAME);
                break;
            default:
                numRows = -1;
                break;
        }
        return numRows;
    }

    public double getData(String database, String imageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        double value = -1;
        switch (database) {
            case SINGLE_COLOR_TABLE_NAME:
                cursor =  db.rawQuery
                        ("SELECT * FROM " + SINGLE_COLOR_TABLE_NAME + " WHERE " +
                                IMAGE_PATH +" = '"+imageName+"'", null);
                if (cursor.moveToFirst()){
                    int index = cursor.getColumnIndex(VALUE);
                    Log.d("INDEX",Integer.toString(index));
                    value = cursor.getDouble(index);
                    return value;
                }

                break;

            case STRUCTURE_COLOR_TABLE_NAME:
                cursor =  db.rawQuery
                        ("SELECT * FROM " + STRUCTURE_COLOR_TABLE_NAME + " WHERE " +
                                IMAGE_PATH +" = '"+imageName+"'", null);
                if (cursor.moveToFirst()){
                    int index = cursor.getColumnIndex(VALUE);
                    Log.d("INDEX",Integer.toString(index));
                    value = cursor.getDouble(index);
                    return value;
                }

        }

        return -1;
    }
}