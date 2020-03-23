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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;


public class Sensor_config_anak extends AppCompatActivity {

    private String[] Item = {"Tegangan1","Tegangan2","Tegangan3","Tegangan4",
            "Tegangan5","Arus1","Arus2","Arus3","Arus4"};
    BluetoothSocket btSocket = null;
    private BluetoothAdapter myBluetooth = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String address = null;

    TextView txt_judul;
    EditText input_IOD;
    EditText input_TAG;
    EditText input_Unit;

    //private String nama;
    //private String KEY_NAME = "NAMA";

    Button kirim_sensorconfig;

    String aa;

    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_config_anak);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        new ConnectBT2().execute();

        txt_judul = (TextView) findViewById(R.id.txt_judulsensorconfig);
        input_IOD = (EditText) findViewById(R.id.et_InputIOD);
        input_TAG = (EditText) findViewById(R.id.et_InputTag);
        input_Unit = (EditText) findViewById(R.id.et_InputUnit);
        kirim_sensorconfig = (Button) findViewById(R.id.but_kirimsensorconfig);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_item);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,Item);

        spinner.setAdapter(adapter);
        //judul ganti sesuai nama
        /**Bundle extras = getIntent().getExtras();
        nama = extras.getString(KEY_NAME);
        txt_judul.setText(nama);*/

        //kirim data
        kirim_sensorconfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                kirim_sensor();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                txt_judul.setText(adapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });
    }

    private void kirim_sensor() {
        String mes = input_IOD.getText().toString();
        mes += "\n";
        String mes2 = input_TAG.getText().toString();
        mes2 += "\n";
        String mes3 = input_Unit.getText().toString();
        mes3 += "\n";

        String gabungsensor = "SENSORCONFIG;" + mes + ";" + mes2 + ";" + mes3 + ";";

        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(gabungsensor.getBytes());

            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    //menghubungkan
    private class ConnectBT2 extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Sensor_config_anak.this, "Connecting...", "Please wait!!!");  //show a progress dialog
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

        Intent intent = new Intent(Sensor_config_anak.this, main_menu.class);
        startActivity(intent);
    }


}
