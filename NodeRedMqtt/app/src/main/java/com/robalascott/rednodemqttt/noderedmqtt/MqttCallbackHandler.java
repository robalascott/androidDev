package com.robalascott.rednodemqttt.noderedmqtt;

import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

/**
 * Created by robscott on 2017-08-04.
 */

class MqttCallbackHandler implements MqttCallbackExtended, MqttCallback {

private final MqttAndroidClient client;

public MqttCallbackHandler (MqttAndroidClient client)
        {
        this.client=client;
        }

@Override
public void connectComplete(boolean b, String s) {
        Log.w("mqtt", s);
        }

@Override
public void connectionLost(Throwable throwable) {

        }

public void AlarmActivatedMessageReceived()
        {
        MqttMessage msg= new MqttMessage("Hello, the Mosquitto Broker got your message saying that the Alarm is Activated.".getBytes());
        try {
        this.client.publish("Fitlet", msg);
        Log.i("mqtt", "Message published");

        } catch (MqttPersistenceException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();

        } catch (MqttException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
        }

@Override
public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Log.w("mqtt", mqttMessage.toString());

        if (mqttMessage.toString().contains("Alarm Activated"))
        {
        AlarmActivatedMessageReceived();
        }

        }

@Override
public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        }
}