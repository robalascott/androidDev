package com.robalascott.rednodechat.rednodechat.FSM;

import android.util.Log;

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
    public void aquiredMessage(String message,WebSocketClient wc) {
        Log.i(Constant.FSMLOG,"StartFSM" + message);
        if(message.contains(Constant.START)){
            fsm.setFSMState(new RecFSM(fsm));
            this.sender(Constant.STATE,fsm.getUsername(),wc);
        }
    }


    @Override
    public String whichState() {
        return "Start";
    }

    public void sender(String payload, String source,WebSocketClient mWebSocketClient){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("payload",payload);
            jsonObject.put("source",source);
            String message = jsonObject.toString();
            mWebSocketClient.send(message);
        }catch(WebsocketNotConnectedException n){
            Log.i("FSM","No connection error" );
        }catch (JSONException e){
            Log.i("FSM","Json error" );
        }
    }
}
