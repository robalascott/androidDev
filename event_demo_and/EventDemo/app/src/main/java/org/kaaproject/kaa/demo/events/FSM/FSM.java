package org.kaaproject.kaa.demo.events.FSM;

import android.content.Context;
import org.kaaproject.kaa.demo.events.Db.DBHelper;

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
    /*Dbase */
    private DBHelper mydb;

    public FSM(Context context){
        this.context = context;
        StartFSM = new StartFSM(this);
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
    public String aquiredMessage(String message){
        return this.fsmState.aquiredMessage(message);
    }
    public String whichState(){return fsmState.whichState();}
    public void sendMessage(String message){ fsmState.sendMessage("hello"); };
    public Context getContext(){return this.context;}

}
