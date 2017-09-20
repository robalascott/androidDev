package com.robalascott.rednodemqttt.noderedmqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by robscott on 2017-08-04.
 */

interface MqttCallbackExtended {
    public void connectComplete(boolean b, String s);
    public void connectionLost(Throwable throwable);
    public void AlarmActivatedMessageReceived();
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken);
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception;
}
