package com.robalascott.rednodechat.rednodechat.FSM;

import android.util.Log;

import com.robalascott.rednodechat.rednodechat.Encryption.Encrypt;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by robscott on 2017-06-21.
 */

public class RecFSM implements FSMState {
    FSM fsm;

    public RecFSM(FSM fsm1) {
        this.fsm = fsm1;
        Log.i(Constant.FSMLOG,"RECFSM");
    }

    @Override
    public void aquiredMessage(String message,WebSocketClient wc,Encrypt encrypted) {
        JSONObject jsonObject = null;
        String temp = null;
        try {
            jsonObject = new JSONObject(message);
            final String newUser = jsonObject.getString("source");
            final String newpayload = jsonObject.getString("payload");
            temp = newUser  + newpayload;
            if(message.contains(Constant.EXITHOST)){
                fsm.setFSMState(new StartFSM(fsm));

          /*Constant.Exit;*/
            }else if(message.contains("@%")){
                temp = newUser  + " **" + newpayload.substring(2,newpayload.length());
                Log.i(Constant.FSMLOG,"Message After->" + temp );
                this.sender(temp,fsm.getUsername(),wc,encrypted);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public String whichState() {
        return "aquired";
    }
    public int getTime(){
        return (int) (System.currentTimeMillis()%10000000);
    }

    public void sender(String payload, String source,WebSocketClient mWebSocketClient, Encrypt encrypted){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("payload",payload);
            jsonObject.put("source",source);
            String message = jsonObject.toString();
            String info =  encrypted.encrypt(message);
            Log.i("RecFSM"," "+ info);
            mWebSocketClient.send(info);
        }catch(WebsocketNotConnectedException n){
            Log.i("FSM","No connection error" );
        }catch (JSONException e){
            Log.i("FSM","Json error" );
        }
        // Log.i(Main,s);
    }
}
