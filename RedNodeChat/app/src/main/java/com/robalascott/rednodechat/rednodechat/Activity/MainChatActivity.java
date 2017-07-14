package com.robalascott.rednodechat.rednodechat.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.robalascott.rednodechat.rednodechat.Db.DBHelper;
import com.robalascott.rednodechat.rednodechat.FSM.Constant;
import com.robalascott.rednodechat.rednodechat.FSM.FSM;
import com.robalascott.rednodechat.rednodechat.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

/**
 *  bundle message problem -> done
 *  import state
 *  error handling for not connect
 *  Toast not working -> send to text field
 *  import sqlite and extra function program
 *  disconnect // reconnect on create on stop etc
 *  unique id for each user -> done
 *  buttons
 *  message format
 */


public class MainChatActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{
    private TextView mTextChatLogs;
    private WebSocketClient mWebSocketClient;
    private EditText mEdit;
    private final String Main ="MAIN";
    private View mButtonHost, mButtonClear,mButtonData,mButtonReset,mButtonText;
    private Spinner spinner;
    private EditText mEditTextInputMessage;
    /*Strings*/
    final String Str = "payload";
    final String AdressRedNode =  "ws://192.168.0.12:1880/ws/chat";
    /*spinner*/
    final Integer[] array = {1,5,10,100,200,300,500};
    private int loop =1;
    private String mUserName;
    private FSM fsm;
    /*SharedPrefe*/
    final int mode = Activity.MODE_PRIVATE;
    final String MYPREFS = "MyPreferences_001";
    private  Toolbar toolbar;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor myEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
         /*Unique Name*/
        mUserName = this.getprefence();
        toolbar.setTitle(mUserName);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarChartActivity.open(getApplicationContext(), "Test");
            }
        });
        /*Chat log*/
        mTextChatLogs = (TextView) findViewById(R.id.simple_chat_logs);
        mTextChatLogs.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTextChatLogs.setIncludeFontPadding(false);
        /*Spinner*/
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
          /*Clear button*/
        mButtonClear =  findViewById(R.id.Clear);
        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fsm.StartState();
                mTextChatLogs.setText(null);

            }
        });
            /*Host button*/
        mButtonHost = findViewById(R.id.Host);
        mButtonHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fsm.SenderState();
                sender(Constant.START,mUserName);
               try {
                    Thread.sleep(3000);
                    for(int x = 0; x<loop; x++ ){
                        sender("@%" + String.valueOf(getTime()),mUserName);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
              //  fsm.sendMessage("hello");
               //  sender(Constant.Exit,mUserName);



            }
        });
         /*Clear Show Data*/
        mButtonData =  findViewById(R.id.Data);
        mButtonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper mydb;
                String Dbase = "DBase";
                mTextChatLogs.setText(null);
                mydb = new DBHelper(getApplicationContext(),Dbase);
                mydb.getAllDiff(mTextChatLogs);
                mydb.close();

            }
        });
            /*Clear Reset*/
        mButtonReset =  findViewById(R.id.Reset);
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper mydb;
                String Dbase = "DBase";
                mydb = new DBHelper(getApplicationContext(),Dbase);
                Log.i(Constant.FSMLOG,"Clear2");
                mydb.delete();
                mydb.close();
            }
        });

        /*Text TEST*/
        mButtonText =  findViewById(R.id.Text);
        mButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fsm.aquiredMessage(Constant.EXITHOST,mWebSocketClient);
                sender(Constant.EXITHOST,mUserName);
            }
        });
        fsm = new FSM(this,mUserName);
        fsm.StartState();
        this.connectWebsocket();
    }
    @Override
    protected void onStart() {
        super.onStart();
         /*FSM */
        fsm.StartState();



    }
    @Override
    protected void onRestart() {
        super.onRestart();
      //  this.connectWebsocket();
         /*FSM */
        fsm.StartState();
        toolbar.setTitle(this.getprefence());
    }
    @Override
    protected void onStop() {
        super.onStop();
        this.saveprefence();
        fsm.StartState();
       // mWebSocketClient.close();
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.saveprefence();
        fsm.StartState();
       // mWebSocketClient.close();
    }

    @Override
    protected void onDestroy() {
        super.onPause();
        this.saveprefence();
        fsm.StartState();
        mWebSocketClient.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            BarChartActivity.open(getApplicationContext(), "Test");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void connectWebsocket(){
        URI uri = null;
        try{
            uri = new URI(AdressRedNode);
        }catch(URISyntaxException e){
            this.setmTextChatlogs("error " +e.toString());
            return;
        }
        mWebSocketClient = new WebSocketClient(uri){

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                //Log.i(Main,"open");
               // String in = "Hello from User" + Build.MANUFACTURER + " " + Build.DEVICE;
              //  sender(in);
            }

            @Override
            public void onMessage(final String payload) {
               // Log.i(Main,"msg " + payload);
               // new Thread(new Task(payload)).start();

               // mTextChatLogs.setText(mTextChatLogs.getText() + "\n" + payload);
                             fsm.aquiredMessage(payload,mWebSocketClient);


                        /*    if(temp.contains(Constant.FAIL) ||temp.contains(Constant.OK) ||temp.contains(Constant.Exit)||temp.contains(Constant.REG)){
                                   Log.i(Constant.CHATLOG, fsm.whichState() + "Event message ->" + temp + " " +mUserName);
                                }else{
                                    mTextChatLogs.setText(mTextChatLogs.getText() + "\n" + temp);
                                    sender(temp,mUserName);
                                    Log.i(Main,temp);
                                }*/


            }



            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i(Main,"Close");
            }

            @Override
            public void onError(Exception ex) {
                Log.i(Main,"Error " + ex.getMessage());
            }
        };
        mWebSocketClient.connect();
        //sender(" + AdressRedNode);
    }


    public void setmTextChatlogs(String s){
        mTextChatLogs.setText(mTextChatLogs.getText() + "\n" + s );
    }

    public void sender(String payload, String source){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("payload",payload);
            jsonObject.put("source",source);
            String message = jsonObject.toString();
            mWebSocketClient.send(message);
        }catch(WebsocketNotConnectedException n){
            mTextChatLogs.setText("No connection error" );
        }catch (JSONException e){
            mTextChatLogs.setText("Json error" );
        }
       // Log.i(Main,s);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.setSelection(position);
        loop = (int)parent.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }

    private int getTime(){
        return (int) (System.currentTimeMillis()%10000000);
    }


    private void saveprefence(){
        myEditor = mySharedPreferences.edit();
        myEditor.putString("Username", mUserName);
        myEditor.commit();
    }

    private String getprefence(){
        mySharedPreferences = getSharedPreferences(MYPREFS, 0);
       return mySharedPreferences.getString("Username", this.SetName());
    }

    private String SetName() {
        Random rand = new Random();
        return Build.MANUFACTURER.toString()+ rand.nextInt()%1200;
    }
}
