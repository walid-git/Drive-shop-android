package com.example.walid.driveshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "waliiiid";
    public static final String ID_KEY = "ID";
    public static final String PREFFERENCES = "DriveShop";
    AppCompatEditText phone,password;
    TextView confirm;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(PREFFERENCES,MODE_PRIVATE);
        if (preferences.contains(ID_KEY)) {
            startActivity(new Intent(this, ProductsActivity.class));
            finish();
        }
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(v -> {
            new LoginTask().execute(phone.getText().toString(), password.getText().toString());
        });
    }

    public class LoginTask extends AsyncTask<String, Integer, Long> {
        @Override
        protected Long doInBackground(String... strings) {
            try {
                Socket socket = new Socket(ProductsActivity.SERVER_ADDRESS, 5565);//
                Log.d(TAG, "doInBackground: Connected");
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Log.d(TAG, "doInBackground: sending login data");
                outputStream.writeUTF(strings[0]+"::"+strings[1]);
                outputStream.flush();
                Log.d(TAG, "doInBackground: waiting for input ");
                while ((inputStream.available()<1)) ;
                Log.d(TAG, "doInBackground: input available " + inputStream.available());
                long res = inputStream.readLong();
                return res;

            } catch (IOException e) {
                Log.d(TAG, "Network problem: "+e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Network problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long res) {
            super.onPostExecute(res);
            Toast.makeText(LoginActivity.this, "Received result :: "+res, Toast.LENGTH_SHORT).show();
            if (res != -1) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(ID_KEY, res);
                editor.commit();
                startActivity(new Intent(LoginActivity.this, ProductsActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Wrong login data, or network problem", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
