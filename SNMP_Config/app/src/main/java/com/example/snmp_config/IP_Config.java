package com.example.snmp_config;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static com.example.snmp_config.main_menu.myUUID;

public class IP_Config extends AppCompatActivity {

    BluetoothSocket btSocket = null;

    EditText input_IP;
    EditText input_defaultgateway;
    EditText input_subnetmask;
    EditText input_port;

    Button kirim_ipconfig;

    String address = null;
    String gabung;

    private ProgressDialog progress;
    private BluetoothAdapter myBluetooth = null;
    private boolean isBtConnected =false;
static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip__config);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        input_IP = (EditText) findViewById(R.id.et_InputIP);
        input_subnetmask = (EditText) findViewById(R.id.et_InputSubMask);
        input_defaultgateway = (EditText) findViewById(R.id.et_DefaultGt);
        input_port = (EditText) findViewById(R.id.et_InputPort);
        kirim_ipconfig = (Button) findViewById(R.id.but_SendData);

        new ConnectBT().execute();

      kirim_ipconfig.setOnClickListener(new View.OnClickListener() {

           @Override
            public void onClick(View view) {
               turnOnLed();
            }
        });
    }

    //kalo dipencet back
    public void onBackPressed(){
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

        Intent intent = new Intent(IP_Config.this, main_menu.class);
        startActivity(intent);
    }

    // mengirim data IP config
    private void turnOnLed()
    {
        String mes = input_IP.getText().toString();
        mes+="\n";
        String mes2 = input_subnetmask.getText().toString();
        mes2+="\n";
        String mes3 = input_defaultgateway.getText().toString();
        mes3+="\n";
        String mes4 = input_port.getText().toString();
        mes4+="\n";

        String gabung = "IPCONFIG;" + mes + ";" + mes2 + ";" + mes3 +";" + mes4 + ";";


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

    //pemberitahuan
    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    //menghubungkan
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(IP_Config.this, "Connecting...", "Please wait!!!");  //show a progress dialog
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