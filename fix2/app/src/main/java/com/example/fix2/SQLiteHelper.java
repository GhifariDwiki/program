package com.example.fix2;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class SQLiteHelper extends SQLiteOpenHelper {

        static String DATABASE_NAME="Databasesuhu2";
        public static final String KEY_ID="id";
        public static final String TABLE_NAME="tabelsuhu2";
        public static final String KEY_Tanggal="tgl";
        public static final String KEY_Jam="jm";
        public static final String KEY_Suhu="shu";
        public SQLiteHelper(Context context) {

            super(context, DATABASE_NAME, null, 1);

        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            try {
                String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_Tanggal + " VARCHAR, " + KEY_Jam + " VARCHAR, " + KEY_Suhu + " VARCHAR)";
                database.execSQL(CREATE_TABLE);
            } catch (SQLException e){
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);

        }

    }

