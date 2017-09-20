package com.robalascott.rednodechat.rednodechat.FSM;

import android.util.Log;


import java.util.LinkedList;
import java.util.List;

import com.robalascott.rednodechat.rednodechat.Db.DBHelper;
import com.robalascott.rednodechat.rednodechat.Encryption.Encrypt;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by robscott on 2017-06-21.
 */

public class SenderFSM implements FSMState {
    FSM fsm;
    private DBHelper mydb;
    private String Dbase = "DBase";
    private List<String> user = new LinkedList<String>();


    public SenderFSM(FSM fsm1) {
        this.fsm = fsm1;
        mydb = new DBHelper(fsm.getContext(), Dbase);
        user.add("Host");
        Log.i(Constant.FSMLOG, "Sender State");
    }

    @Override
    public void aquiredMessage(String message, WebSocketClient wc,Encrypt encrypted) {
        try {

            JSONObject jsonObject = null;
            jsonObject = new JSONObject(message);
            final String newpayload = jsonObject.getString("source");
            final String value = jsonObject.getString("payload");

        /*Reg process for number of clients*/
            if (message.contains(Constant.STATE)) {
            /*Using JSON get name put name*/
                String temp = null;
                if (!user.contains(newpayload)) {
                    user.add(newpayload);
                    Log.i(Constant.FSMLOG, "useradd" + user.size());
                }

                //  return Constant.REG;
            } else if (message.contains(Constant.EXITHOST)) {
                user.clear();
            /*The host is still running*/
                user.add("Host");
                // return Constant.Exit;
                mydb.close();
            } else if (value.contains(fsm.getUsername())) {

                try {
                    String temp = value.substring(value.indexOf("**")+2, value.length());
                    Log.i(Constant.FSMLOG, "AquireMessageTest" + temp);
                    int tempsent = Integer.parseInt(temp);
                    //Log.i(Constant.FSMLOG, "AquireMessageTest" + temp + " " + newpayload + " " + tempsent);
                    mydb.insertContact(newpayload, Integer.parseInt(temp), getTime(), getTime() - tempsent, user.size());
                } catch (NumberFormatException e) {
                    Log.i(Constant.FSMLOG, "Error + Number format" + message);
                    // return Constant.FAIL;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // return Constant.FAIL;
        }
        //return Constant.OK;
    }

    @Override
    public String whichState() {
        return "Sender";
    }

    public int getTime() {
        return (int) (System.currentTimeMillis() % 10000000);
    }

    public void sender(String payload, String source, WebSocketClient mWebSocketClient,Encrypt encrypted) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("payload", payload);
            jsonObject.put("source", source);
            String message = jsonObject.toString();
            String info =  encrypted.encrypt(message);
            mWebSocketClient.send(info);

        } catch (WebsocketNotConnectedException n) {
            Log.i("FSM", "No connection error");
        } catch (JSONException e) {
            Log.i("FSM", "Json error");
        }
    }

}
