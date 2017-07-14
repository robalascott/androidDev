package org.kaaproject.kaa.demo.events.FSM;

import android.util.Log;

/**
 * Created by robscott on 2017-06-21.
 */

public class RecFSM implements FSMState {
    FSM fsm;

    public RecFSM(FSM fsm1) {
        this.fsm = fsm1;
    }

    @Override
    public String aquiredMessage(String message) {
       if(message.contains(Constant.EXITHOST)){
           fsm.setFSMState(new StartFSM(fsm));
           return Constant.Exit;
       }else if(message.contains("@")){
            return message.substring(message.indexOf("@")+1);
       }
        return Constant.OK;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String whichState() {
        return "aquired";
    }
    public int getTime(){
        return (int) (System.currentTimeMillis()%10000000);
    }
}
