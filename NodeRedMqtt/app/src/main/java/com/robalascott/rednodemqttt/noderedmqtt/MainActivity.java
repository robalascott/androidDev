package com.robalascott.rednodemqttt.noderedmqtt;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MainActivity extends AppCompatActivity {
    String TAG ="TAG";
    String UserName = "User";
    String Password = "1234456";

    int qos = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String clientId = MqttClient.generateClientId();

        //The URL of the Mosquitto Broker is 192.168.9.100:1883
        final  MqttAndroidClient client = new MqttAndroidClient(this.getApplicationContext(), "mqtt://192.168.10.177:1883", clientId);

        client.setCallback(new MqttCallbackHandler(client));//This is here for when a message is received

        MqttConnectOptions options = new MqttConnectOptions();

        try {
            options.setUserName("fred");
            options.setPassword("1234".toCharArray());
            IMqttToken token = client.connect(options);
            if(token == null) Log.d("fail", "onfail");


            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("mqtt", "onSuccess");
//-----------------------------------------------------------------------------------------------
                    //PUBLISH THE MESSAGE
                    MqttMessage message = new MqttMessage("Hello, I am an Android Mqtt Client.".getBytes());
                    message.setQos(0);
                    message.setRetained(false);

                    String topic = "android";

                    try {
                        client.publish(topic, message);
                        Log.i("mqtt", "Message published");

                        // client.disconnect();
                        //Log.i("mqtt", "client disconnected");

                    } catch (MqttPersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    } catch (MqttException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
//-----------------------------------------------------------------------------------------------

                    String subtopic = "tester";

                    try {
                        IMqttToken subToken = client.subscribe(subtopic, qos);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // The message was published
                                Log.i("mqtt", "subscription success");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                Log.i("mqtt", "subscription failed");

                            }
                        });



                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

//---------------------------------------------------------------------------

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("mqtt", "onFailure");

                }

            });


        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
