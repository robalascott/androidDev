package com.robalascott.noderedpi.node_red;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import Db.DBHelper;

import static java.lang.Boolean.TRUE;

public class MainPiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private WebSocketClient mWebSocketClient;
    private final String Main = "MAIN";
    private Spinner  spinner2;
    private final String Str = "payload";



     final String AdressRedNode = "ws://192.168.0.12:1880/ws/encrypt";
   // final String AdressRedNode = "ws://192.168.10.177:1880/ws/encrypt";
   // final String AdressRedNode = "ws://192.168.10.177:1880/ws/led";

    /*spinner*/;
    final String[] state = {"On", "Off", "Blink"};
    DBHelper mydb;
    String Dbase = "DBase";
    int messagesize = 1000;
    int dev = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mydb.delete();


            }
        });

         /*Spinner*/
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, state);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
        spinner2.setSelection(0);
        connectWebsocket();

        mydb = new DBHelper(getApplicationContext(), Dbase);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_pi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()) {

            case R.id.action_test_3:
                test(3);
                dev = 3;
                break;
            case R.id.action_test_7:
                test(7);
                dev = 7;
                break;
            case R.id.action_test_5:
                test(5);
                dev = 5;
                break;
            case R.id.action_test_6:
                test(6);
                dev = 6;
                break;
            case R.id.action_test_10:
                test(10);
                dev = 10;
                break;
            case R.id.action_test_50:
                test(50);
                dev = 50;
                break;
            case R.id.action_test_100:
                test(100);
                dev = 100;
                break;
            default:
                return false;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.setSelection(position);
        final String value = (String) parent.getSelectedItem().toString();
        Log.i("Main", value);
         mWebSocketClient.send(value);
    }




    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }

    private void connectWebsocket() {
        URI uri = null;
        try {
            uri = new URI(AdressRedNode);
            Log.i("onMessage", "msg " + uri.toString());
        } catch (URISyntaxException e) {
            // this.setmTextChatlogs("error " +e.toString());
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                //Log.i(Main,"open");
                //  String in = "Hello from User" + Build.MANUFACTURER + " " + Build.DEVICE;
                //    mWebSocketClient.send(in);
            }

            @Override
            public void onMessage(final String payload) {

                if (payload.contains("#")) {
                    try {
                        Log.i("onMessage", "msg " + payload);
                        Integer v = Integer.parseInt(payload.substring(1));
                        Log.i("onMessage", "msg " + v);
                        mydb.insertContact("TestNodeRed", v, getTime(), getTime() - v, dev);
                    } catch (Exception e) {
                        Log.i("Int ", e.toString());
                    }
                } else {

                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("onMessage", "Close ");
            }

            @Override
            public void onError(Exception ex) {
                Log.i("onMessage", "Err" + ex.toString());
            }

        };
        mWebSocketClient.connect();
    }

    private int getTime() {
        return (int) (System.currentTimeMillis() % 10000000);
    }

    public void test(final int time) {
        new Thread(new Runnable() {
            public void run() {
                // a potentially  time consuming task
                for (int x = 0; x < messagesize; x++) {
                    String value = "*" + String.valueOf(getTime());
                    mWebSocketClient.send(value);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        Log.i("test", e.toString());
                    }

                }

            }
        }).start();

    }
}