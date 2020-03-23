package com.example.snmp_config;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button hubungkan;
    private BluetoothAdapter myBluetooth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // menyalakan modul bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
            finish();
        } else {
            if (myBluetooth.isEnabled()) {
            } else {
                Intent turnBTOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTOn, 1);
            }


        }

        //pindah ke main_menu
        hubungkan = findViewById(R.id.but_HubungkanBT);

        hubungkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, main_menu.class);
                startActivity(intent);
            }
        });
    }

}


