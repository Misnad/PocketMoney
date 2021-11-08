package com.misnadqasim.pocketmoney.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.misnadqasim.pocketmoney.transaction.TransactionDetails;

import java.util.ArrayList;
import java.util.Currency;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "POCKET_MONEY";
    private static final String TABLE_NAME = "TRANSACTIONS";
    private static final int DATABASE_VERSION = 1;


    public static final String NAME = "NAME";
    public static final String TIME = "TIME";
    public static final String AMOUNT = "AMOUNT";
    public static final String TAG = "TAG";

    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Name, time, amount, date, tag
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR, TIME DATETIME, AMOUNT INTEGER, TAG VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // create backup .csv
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean doTransaction(String label, long datetime, int amount, String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, label);
        contentValues.put(TIME, toDatetime(datetime));
        contentValues.put(AMOUNT, amount);
        contentValues.put(TAG, tag);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean editTransaction(int id, String label, long datetime, int amount, String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, label);
        contentValues.put(TIME, datetime);
        contentValues.put(AMOUNT, amount);
        contentValues.put(TAG, tag);

        long result = db.update(TABLE_NAME, contentValues, "ID=?", new String[] {id+""});
        return result != -1;
    }

    public ArrayList<TransactionDetails> getTransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<TransactionDetails> result = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                TransactionDetails tsc = new TransactionDetails(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getLong(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                );
                result.add(tsc);
            }
        }

        return result;
    }


    public boolean deleteTransactions(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_NAME ,"ID = ?", new String[] {id+""});
        return result != -1;
    }

    /**
     * Convert time in milliseconds to datetime
     * @param datetime time from epoch in milliseconds
     */
    private String toDatetime(long datetime) {
        // TODO
        return datetime + "";
    }
}
