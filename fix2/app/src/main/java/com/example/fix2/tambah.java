package com.example.fix2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class tambah extends AppCompatActivity {

    EditText tanggal;
    EditText jam;
    EditText suhu;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Boolean CheckEditTextEmpty ;
    String SQLiteQuery;
    SQLiteDatabase SQLITEDATABASE;
    Button save;
    String Tanggal,Jam,Suhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        tanggal = (EditText) findViewById(R.id.et_waktu);
        jam = (EditText) findViewById(R.id.et_jam);
        suhu = (EditText) findViewById(R.id.et_suhu);
        save = (Button) findViewById(R.id.but_simpan);

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(tambah.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrenttime = Calendar.getInstance();
                int hour = mcurrenttime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrenttime.get(Calendar.MINUTE);
                TimePickerDialog mTimepicker;
                mTimepicker = new TimePickerDialog(tambah.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        jam.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                mTimepicker.setTitle("Masukkan Waktu Pengukuran");
                mTimepicker.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBCreate();
                SubmitData2SQLiteDB();
            }
        });

    }

    private void updateLabel(){
        String myformat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        tanggal.setText(sdf.format(myCalendar.getTime()));
    }

    public void DBCreate(){

        SQLITEDATABASE = openOrCreateDatabase("Databasesuhu2", Context.MODE_PRIVATE, null);
        SQLITEDATABASE.execSQL("CREATE TABLE IF NOT EXISTS tabelsuhu2(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tgl VARCHAR, jm VARCHAR, shu VARCHAR);");
    }

    public void SubmitData2SQLiteDB(){

        Tanggal = tanggal.getText().toString();

        Suhu = suhu.getText().toString();

        Jam = jam.getText().toString();

        CheckEditTextIsEmptyOrNot( Tanggal,Jam, Suhu);

        if(CheckEditTextEmpty == true)
        {

            SQLiteQuery = "INSERT INTO tabelsuhu2 (tgl,jm,shu) VALUES('"+Tanggal+"', '"+Jam+"', '"+Suhu+"');";
            SQLITEDATABASE.execSQL(SQLiteQuery);
            Toast.makeText(tambah.this,"Data sukses ditambahkan", Toast.LENGTH_LONG).show();
            ClearEditTextAfterDoneTask();

        }
        else {

            Toast.makeText(tambah.this,"Semua bagian harus diisi", Toast.LENGTH_LONG).show();
        }
    }

    public void CheckEditTextIsEmptyOrNot(String Tanggal,String Jam, String Suhu ){

        if(TextUtils.isEmpty(Tanggal) || TextUtils.isEmpty(Jam) || TextUtils.isEmpty(Suhu)){

            CheckEditTextEmpty = false ;

        }
        else {
            CheckEditTextEmpty = true ;
        }
    }

    public void ClearEditTextAfterDoneTask(){

        tanggal.getText().clear();
        jam.getText().clear();
        suhu.getText().clear();

    }
}
