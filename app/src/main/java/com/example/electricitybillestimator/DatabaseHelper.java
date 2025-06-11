package com.example.electricitybillestimator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.electricitybillestimator.CalculationEntry;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electricity_bill.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    public static final String TABLE_CALCULATIONS = "calculations";
    public static final String COLUMN_ID = "_id"; // Primary key
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_UNITS_USED = "units_used";
    public static final String COLUMN_TOTAL_CHARGES = "total_charges";
    public static final String COLUMN_REBATE_PERCENTAGE = "rebate_percentage";
    public static final String COLUMN_FINAL_COST = "final_cost";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // SQL query to create the table
    private static final String CREATE_TABLE_CALCULATIONS =
            "CREATE TABLE " + TABLE_CALCULATIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_MONTH + " TEXT NOT NULL," +
                    COLUMN_UNITS_USED + " REAL NOT NULL," +
                    COLUMN_TOTAL_CHARGES + " REAL NOT NULL," +
                    COLUMN_REBATE_PERCENTAGE + " REAL NOT NULL," +
                    COLUMN_FINAL_COST + " REAL NOT NULL," +
                    COLUMN_TIMESTAMP + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CALCULATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists and create a new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALCULATIONS);
        onCreate(db);
    }

    // Method to add a new calculation entry
    public long addCalculation(CalculationEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MONTH, entry.getMonth());
        values.put(COLUMN_UNITS_USED, entry.getUnitsUsed());
        values.put(COLUMN_TOTAL_CHARGES, entry.getTotalCharges());
        values.put(COLUMN_REBATE_PERCENTAGE, entry.getRebatePercentage());
        values.put(COLUMN_FINAL_COST, entry.getFinalCost());
        values.put(COLUMN_TIMESTAMP, entry.getTimestamp());

        long id = db.insert(TABLE_CALCULATIONS, null, values);
        db.close();
        return id; // Returns the ID of the newly inserted row, or -1 if an error occurred
    }

    // Method to get all calculation entries
    public List<CalculationEntry> getAllCalculations() {
        List<CalculationEntry> calculationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CALCULATIONS + " ORDER BY " + COLUMN_TIMESTAMP + " DESC"; // Order by latest first

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String month = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MONTH));
                double unitsUsed = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNITS_USED));
                double totalCharges = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CHARGES));
                double rebatePercentage = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REBATE_PERCENTAGE));
                double finalCost = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FINAL_COST));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));

                CalculationEntry entry = new CalculationEntry(id, month, unitsUsed, totalCharges, rebatePercentage, finalCost, timestamp);
                calculationList.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return calculationList;
    }

    // Method to get a single calculation entry by ID
    public CalculationEntry getCalculationById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CALCULATIONS,
                new String[]{COLUMN_ID, COLUMN_MONTH, COLUMN_UNITS_USED, COLUMN_TOTAL_CHARGES,
                        COLUMN_REBATE_PERCENTAGE, COLUMN_FINAL_COST, COLUMN_TIMESTAMP},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        CalculationEntry entry = null;
        if (cursor != null && cursor.moveToFirst()) {
            entry = new CalculationEntry(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MONTH)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNITS_USED)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CHARGES)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REBATE_PERCENTAGE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FINAL_COST)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
            );
            cursor.close();
        }
        db.close();
        return entry;
    }
}