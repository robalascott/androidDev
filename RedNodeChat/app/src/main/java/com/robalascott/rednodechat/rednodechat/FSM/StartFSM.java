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

public class StartFSM implements FSMState {
    FSM fsm;
    public StartFSM(FSM fsm1) {
        this.fsm = fsm1;
        Log.i(Constant.FSMLOG,"StartFSM");
    }
    @Override
    public void aquiredMessage(String message,WebSocketClient wc,Encrypt encrypted) {
        Log.i(Constant.FSMLOG,"StartFSM" + message + message.contains(Constant.START));
        if(message.contains(Constant.START)){
            fsm.setFSMState(new RecFSM(fsm));
            Log.i(Constant.FSMLOG,"StartFSMinside" + message);
            this.sender(Constant.STATE,fsm.getUsername(),wc,encrypted);

        }
    }


    @Override
    public String whichState() {
        return "Start";
    }

    public void sender(String payload, String source,WebSocketClient mWebSocketClient,Encrypt encrypted){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("payload",payload);
            jsonObject.put("source",source);
            String message = jsonObject.toString();
            Log.i(Constant.FSMLOG,"StartFSMsender" + message);
            String info =  encrypted.encrypt(message);
            Log.i(Constant.FSMLOG,"StartFSMsender" + message + " " + info);
            mWebSocketClient.send(info);
        }catch(WebsocketNotConnectedException n){
            Log.i("FSM","No connection error" );
        }catch (JSONException e){
            Log.i("FSM","Json error" );
        }
    }
}
