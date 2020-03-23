package com.example.snmp_config;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Online_Monitoring extends AppCompatActivity {

    BluetoothSocket btSocket = null;

    EditText input_rly1;
    EditText input_rly2;

    Button kirim_lvd;

    String address = null;
    String gabung;

    private ProgressDialog progress;
    private BluetoothAdapter myBluetooth = null;
    private boolean isBtConnected =false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online__monitoring);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        input_rly1 = (EditText) findViewById(R.id.et_rly1);
        input_rly2 = (EditText) findViewById(R.id.et_rly2);
        kirim_lvd = (Button) findViewById(R.id.but_lvdconfig);

        new ConnectBT3().execute();

        kirim_lvd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                kirimlvd();
            }
        });
    }

    public void onBackPressed() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                msg("Error");
            }

        }

        Intent intent = new Intent(Online_Monitoring.this, main_menu.class);
        startActivity(intent);
    }

    private void kirimlvd()
    {
        String mes = input_rly1.getText().toString();
        mes+="\n";
        String mes2 = input_rly2.getText().toString();
        mes2+="\n";

        String gabung = "LVDCONFIG;" + mes + ";" + mes2 + ";";


        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(gabung.getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT3 extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Online_Monitoring.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

}
