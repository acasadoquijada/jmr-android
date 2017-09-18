package com.jmr_android.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jmr_android.fragment.ConsultFragment;
import com.jmr_android.jmr.MPEG7ColorStructure;

/**
 * Created by alejandro on 08/09/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Descriptor.db";
    public static final String SINGLE_COLOR_TABLE_NAME = "SingleColorDescription";
    public static final String STRUCTURE_COLOR_TABLE_NAME = "MPEG7ColorStructure";
    public static final String IMAGE_PATH = "imagePath";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
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
                "HIST" + " TEXT NOT NULL, " +
                "QLEVELS" + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
        onCreate(db);
    }

    private boolean insertColorStructureHist(String imageName, int[] hist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_PATH, imageName);
        // Convertir
        String sHist = convertArrayToString(hist);
        contentValues.put("HIST", sHist);

        //  Log.d("INSERTO", imageName);
        db.insert(STRUCTURE_COLOR_TABLE_NAME, null, contentValues);
        return true;
    }

    private boolean insertSingleColorValues(String imageName, int[] values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_PATH, imageName);
        contentValues.put("RED", values[0]);
        contentValues.put("GREEN", values[1]);
        contentValues.put("BLUE", values[2]);
        //   Log.d("INSERTO", imageName);
        db.insert(SINGLE_COLOR_TABLE_NAME, null, contentValues);
        return true;
    }

    public int[] getData(String imageName, int active_descriptor) {

        switch (active_descriptor) {
            case ConsultFragment.SINGLE_COLOR_DESCRIPTOR:
                return getSingleColorData(imageName);

            case ConsultFragment.MPEG7_COLOR_STRUCTURE:
                return getColorStructureHist(imageName);

            default:
                return null;
        }

    }

    public void setData(String imageName, int[] values, int active_descriptor) {
        switch (active_descriptor) {
            case ConsultFragment.SINGLE_COLOR_DESCRIPTOR:
                insertSingleColorValues(imageName, values);
                break;

            case ConsultFragment.MPEG7_COLOR_STRUCTURE:
                insertColorStructureHist(imageName, values);
                break;

            default:

        }
    }

    private int[] getColorStructureHist(String imageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        int hist[];
        String sHist;

        cursor = db.rawQuery
                ("SELECT * FROM " + STRUCTURE_COLOR_TABLE_NAME + " WHERE " +
                        IMAGE_PATH + " = '" + imageName + "'", null);

        if (cursor.moveToFirst()) {
            sHist = cursor.getString(cursor.getColumnIndex("HIST"));

            hist = convertStringToArray(sHist);

            //  Log.d("hist size",Integer.toString(hist.length));
            return hist;
        }

        return null;
    }

    private int[] getSingleColorData(String imageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        int hist[] = {-1, -1, -1};

        cursor = db.rawQuery
                ("SELECT * FROM " + SINGLE_COLOR_TABLE_NAME + " WHERE " +
                        IMAGE_PATH + " = '" + imageName + "'", null);

        if (cursor.moveToFirst()) {
            hist[0] = cursor.getInt(cursor.getColumnIndex("RED"));
            hist[1] = cursor.getInt(cursor.getColumnIndex("GREEN"));
            hist[2] = cursor.getInt(cursor.getColumnIndex("BLUE"));
            return hist;
        }

        return null;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery ;
        clearDBQuery = "DELETE FROM "+ SINGLE_COLOR_TABLE_NAME;
        db.execSQL(clearDBQuery);
        clearDBQuery = "DELETE FROM "+ STRUCTURE_COLOR_TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    private String strSeparator = "__,__";

    private String convertArrayToString(int[] arrayInt) {

        String string = "";
        for (int i = 0; i < arrayInt.length; i++) {
            int v = arrayInt[i];
            String vString = Integer.toString(v);
            string = string + vString;

            if (i < arrayInt.length - 1) {
                string = string + strSeparator;
            }
        }
        return string;
    }

    private int[] convertStringToArray(String str) {
        String[] arr = str.split(strSeparator);

        int[] integerArray = new int[arr.length];

        for (int i = 0; i < integerArray.length; i++) {
            integerArray[i] = Integer.valueOf(arr[i]);
            // Log.d("INT",Integer.toString(integerArray[i]));
        }

        return integerArray;
    }
}
