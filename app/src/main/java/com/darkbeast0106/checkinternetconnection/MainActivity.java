package com.darkbeast0106.checkinternetconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView textInternet;
    ConnectivityManager connectivityManager;
    Timer timer;
    boolean connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onResume() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                connection = checkConnected();
                timerMethod();
            }
        };
        timer.schedule(task, 0,5000);
        super.onResume();
    }

    private void timerMethod() {
        runOnUiThread(timerTick);
    }

    @Override
    protected void onPause() {
        timer.cancel();
        super.onPause();
    }

    // A hostAvailable függvény elég lenne,
    // de mobil adatforgalmat spórolunk azzal, hogy feltételezzük,
    // hogy ha van mobil kapcsolat akkor internet is van.
    private boolean checkConnected() {
        return hostAvailable("www.google.com", 80);
        /*
        NetworkInfo.State mobilState
                = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifiState
                = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobilState.equals(NetworkInfo.State.CONNECTED)){
            return true;
        }
        if (wifiState.equals(NetworkInfo.State.CONNECTED)){

        }
        return false;
        */

    }

    public boolean hostAvailable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);
            return true;
        } catch (IOException e) {
            // Either we have a timeout or unreachable host or failed DNS lookup
            System.out.println(e);
            return false;
        }
    }

    private final Runnable timerTick = new Runnable() {
        @Override
        public void run() {
            textInternet.setVisibility(connection ? View.VISIBLE : View.GONE);
        }
    };

    private void init() {
        textInternet = findViewById(R.id.text_internet);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}