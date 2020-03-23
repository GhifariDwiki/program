package com.example.snmp_config;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeviceList_sensorconfig extends AppCompatActivity {

    ListView deviceList;
    private BluetoothAdapter myBluetooth = null;
    BluetoothDevice bt;
    Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    Button hubungkan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list_sensorconfig);

        hubungkan = findViewById(R.id.but_hubungkan3);
        deviceList = findViewById(R.id.lv_PairedDevice3);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        hubungkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });
    }

    // scanning bluetooth
    private void pairedDevicesList() {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    // supaya listviewnya bisa diklik dan pindah halaman ke main menu
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent i = new Intent(DeviceList_sensorconfig.this, Sensor_config_anak.class);
            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };

}