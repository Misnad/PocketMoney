package com.misnadqasim.pocketmoney.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.misnadqasim.pocketmoney.Tag;
import com.misnadqasim.pocketmoney.loan.LoanDetails;
import com.misnadqasim.pocketmoney.transaction.TransactionDetails;

import java.io.LineNumberReader;
import java.util.ArrayList;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "POCKET_MONEY";
    private static final String TABLE_TRANSACTION = "TRANSACTIONS";
    private static final String TABLE_LOAN = "LOANS";
    private static final int DATABASE_VERSION = 1;


    public static final String TR_LABEL = "LABEL";
    public static final String TR_AMOUNT = "AMOUNT";
    public static final String TR_DATETIME = "DATETIME";
    public static final String TR_TAG = "TAG";
    public static final String TR_NOTE = "NOTES";

    public static final String LN_TO_FROM = "TO_FROM";
    public static final String LN_AMOUNT = "AMOUNT";
    public static final String LN_DATETIME = "DATETIME";
    public static final String LN_NOTE = "NOTEs";

    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TRANSACTION +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TR_LABEL + " VARCHAR, " +
                TR_AMOUNT + " INTEGER, " +
                TR_DATETIME + " DATETIME, " +
                TR_TAG + " VARCHAR, " +
                TR_NOTE + " VARCHAR);"
        );
        db.execSQL("CREATE TABLE " + TABLE_LOAN +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LN_TO_FROM + " VARCHAR, " +
                LN_AMOUNT + " INTEGER, " +
                LN_DATETIME + " DATETIME, " +
                TR_NOTE + " VARCHAR);"
        );
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // create backup .csv
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAN);
    }

    public boolean makeTransaction(String label, int amount, String datetime, Tag tag, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TR_LABEL, label);
        contentValues.put(TR_AMOUNT, amount);
        contentValues.put(TR_DATETIME, datetime);
        contentValues.put(TR_TAG, tag.name);
        contentValues.put(TR_NOTE, notes);

        long result = db.insert(TABLE_TRANSACTION, null, contentValues);
        return result != -1;
    }

    public boolean makeLoan(String toFrom, int amount, String datetime, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LN_TO_FROM, toFrom);
        contentValues.put(LN_AMOUNT, amount);
        contentValues.put(LN_DATETIME, datetime);
        contentValues.put(LN_NOTE, notes);

        long result = db.insert(TABLE_LOAN, null, contentValues);
        return result != -1;
    }

    public boolean editTransaction(int id, String label, int amount, long datetime, Tag tag, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TR_LABEL, label);
        contentValues.put(TR_AMOUNT, amount);
        contentValues.put(TR_DATETIME, datetime);
        contentValues.put(TR_TAG, tag.name);
        contentValues.put(TR_NOTE, notes);

        long result = db.update(TABLE_TRANSACTION, contentValues, "ID=?", new String[] {id+""});
        return result != -1;
    }

    public boolean editLoan(int id, String toFrom, int amount, long datetime, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LN_TO_FROM, toFrom);
        contentValues.put(LN_AMOUNT, amount);
        contentValues.put(LN_DATETIME, datetime);
        contentValues.put(LN_NOTE, notes);

        long result = db.update(TABLE_LOAN, contentValues, "ID=?", new String[] {id+""});
        return result != -1;
    }

    public ArrayList<TransactionDetails> getTransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION, null);

        ArrayList<TransactionDetails> result = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                TransactionDetails tsc = new TransactionDetails(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                result.add(tsc);
            }
        }

        return result;
    }

    public ArrayList<LoanDetails> getLoans() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOAN, null);

        ArrayList<LoanDetails> result = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                LoanDetails tsc = new LoanDetails(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                result.add(tsc);
            }
        }

        return result;
    }

    public boolean deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_TRANSACTION,"ID = ?", new String[] {id+""});
        return result != -1;
    }

    public boolean deleteLoan(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_LOAN,"ID = ?", new String[] {id+""});
        return result != -1;
    }

    public int getTransactionSum() {
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT SUM(" + TR_AMOUNT + ") as Total FROM " + TABLE_TRANSACTION, null);

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("Total"));
    }

    public int getLoanSum() {
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT SUM(" + LN_AMOUNT + ") as Total FROM " + TABLE_LOAN, null);

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("Total"));
    }

    public int getTransactionIns() {
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT SUM(" + TR_AMOUNT + ") as Total FROM " + TABLE_TRANSACTION + " WHERE " + TR_AMOUNT + " > 0", null);

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("Total"));
    }

    public int getLoanIns() {
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT SUM(" + LN_AMOUNT + ") as Total FROM " + TABLE_LOAN + " WHERE " + LN_AMOUNT + " > 0", null);

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("Total"));
    }

    public int getTransactionOuts() {
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT SUM(" + TR_AMOUNT + ") as Total FROM " + TABLE_TRANSACTION + " WHERE " + TR_AMOUNT + " < 0", null);

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("Total"));
    }

    public int getLoanOuts() {
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT SUM(" + LN_AMOUNT + ") as Total FROM " + TABLE_LOAN + " WHERE " + LN_AMOUNT + " < 0", null);

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("Total"));
    }

}
