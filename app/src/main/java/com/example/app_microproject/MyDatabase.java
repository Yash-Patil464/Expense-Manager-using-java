package com.example.app_microproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ExpenseCal.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_calculation";
    private static final String COLUMN_ID = "Tracker_expenseId";
    private static final String COLUMN_NOTE = "Tracker_note";
    private static final String COLUMN_CATEGORY = "Tracker_category";
    private static final String COLUMN_AMOUNT = "Tracker_amount";
    private static final String COLUMN_DATE = "Tracker_date";


    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AMOUNT + " INTEGER, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_DATE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addAmount(Long amount, String note, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AMOUNT, amount);
        cv.put(COLUMN_NOTE, note);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_DATE, date);

        long result = db.insert(TABLE_NAME,null,cv);
        if (result==-1){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Added Successfully!",Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    void updateData(String row_id,Long amount, String note, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AMOUNT,amount);
        cv.put(COLUMN_NOTE,note);
        cv.put(COLUMN_CATEGORY,category);

        long result = db.update(TABLE_NAME, cv,"Tracker_expenseId=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context,"Failed to update.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Successfully Updated!",Toast.LENGTH_SHORT).show();
        }
    }

    void deleteData(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "Tracker_expenseId=?", new String[]{row_id});

        if(result == -1) {
            Toast.makeText(context,"Failed to Deleted.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Successfully Deleted!",Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    @SuppressLint("Range")
    int total_amountData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") as Total FROM " + TABLE_NAME + " WHERE " + COLUMN_CATEGORY + "='Income'";
        Cursor cursor = db.rawQuery(query, null);
        int total=0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        return total;
    }

    @SuppressLint("Range")
    int total_expenseData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") as Total FROM " + TABLE_NAME + " WHERE " + COLUMN_CATEGORY + "='Expense'";
        Cursor cursor = db.rawQuery(query, null);
        int total=0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        return total;
    }
}
