package com.example.fix2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class liat extends AppCompatActivity {

    SQLiteHelper SQLITEHELPER;
    SQLiteDatabase SQLITEDATABASE;
    Cursor cursor;
    SQLiteListAdapter ListAdapter;

    ArrayList<String> ID_ArrayList = new ArrayList<String>();
    ArrayList<String> TANGGAL_ArrayList = new ArrayList<String>();
    ArrayList<String> JAM_ArrayList = new ArrayList<String>();
    ArrayList<String> SUHU_ArrayList = new ArrayList<String>();
    ListView LISTVIEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liat);

        LISTVIEW = (ListView) findViewById(R.id.lv_data);
        SQLITEHELPER = new SQLiteHelper(this);
    }

    @Override
    protected void onResume() {
        ShowSQLiteDBdata();
        super.onResume();
    }

    private void ShowSQLiteDBdata(){
        SQLITEDATABASE = SQLITEHELPER.getWritableDatabase();
        cursor = SQLITEDATABASE.rawQuery ("SELECT * FROM tabelsuhu2", null);

        ID_ArrayList.clear();
        TANGGAL_ArrayList.clear();
        JAM_ArrayList.clear();
        SUHU_ArrayList.clear();

        if (cursor.moveToFirst()) {
            do {
                ID_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ID)));
                TANGGAL_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_Tanggal)));
                JAM_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_Jam)));
                SUHU_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_Suhu)));
            } while (cursor.moveToNext());
        }

        ListAdapter = new SQLiteListAdapter(liat.this, ID_ArrayList, TANGGAL_ArrayList, JAM_ArrayList, SUHU_ArrayList);

        LISTVIEW.setAdapter(ListAdapter);
        cursor.close();
    }
}
