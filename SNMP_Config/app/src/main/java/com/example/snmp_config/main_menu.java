package com.example.snmp_config;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class main_menu extends AppCompatActivity {

BluetoothAdapter myBluetooth = null;
BluetoothSocket btSocket = null;
static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_menu);

            Intent newInt = getIntent();


        // pindah - pindah halaman
        Button pindahkeIPconfig = (Button) findViewById(R.id.but_ipconfig);
        pindahkeIPconfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_menu.this, DeviceList.class);
                startActivity(intent);
            }
        });

        Button pindahkeSensorConfig = (Button) findViewById(R.id.but_sensorconfig);
        pindahkeSensorConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(main_menu.this, DeviceList_sensorconfig.class);
                startActivity(in);
            }
        });

        Button pindahkeOnlineMonitoring = (Button) findViewById(R.id.but_lvdconfig);
        pindahkeOnlineMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tent = new Intent(main_menu.this, DeviceList_lvdConfig.class);
                startActivity(tent);
            }
        });
    }

    // message
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    //dialog exit aplikasi
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //memutus koneksi
                if (btSocket!= null)
                {
                    try
                    {
                        btSocket.close();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }

                }

                myBluetooth = BluetoothAdapter.getDefaultAdapter();
                if (myBluetooth.isEnabled()){
                    myBluetooth.disable();
                }


                //menutup aplikasi
                Intent exit = new Intent(Intent.ACTION_MAIN);
                             exit.addCategory(Intent.CATEGORY_HOME);
                             exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                             startActivity(exit);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


 }

