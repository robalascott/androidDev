package com.robalascott.rednodechat.rednodechat.FSM;

import android.content.Context;
import com.robalascott.rednodechat.rednodechat.Db.DBHelper;

import org.java_websocket.client.WebSocketClient;

/**
 * Created by robscott on 2017-06-21.
 */

public class FSM {
    /*List of all states*/
    private FSMState StartFSM;
    private FSMState AquireFSM;
    private FSMState SenderFSM;
    private FSMState fsmState;
    /* Context for data usage*/
    private Context context;
    private String username;
    /*Dbase */
    private DBHelper mydb;

    public FSM(Context context, String username){
        this.context = context;
        StartFSM = new StartFSM(this);
        this.username = username;
    }
    public void setFSMState(FSMState newState){
        this.fsmState = newState;
    }
    public void StartState(){
        this.fsmState = StartFSM;
    }
    public void SenderState(){
        this.fsmState = new SenderFSM(this);
    }
    public void aquiredMessage(String message, WebSocketClient wc){
       this.fsmState.aquiredMessage(message, wc);
    }
    public String whichState(){return fsmState.whichState();}
    public Context getContext(){return this.context;}
    public String getUsername(){return this.username;}
}
