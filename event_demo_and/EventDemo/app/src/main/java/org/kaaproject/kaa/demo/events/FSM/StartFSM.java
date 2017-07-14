package org.kaaproject.kaa.demo.events.FSM;

/**
 * Created by robscott on 2017-06-21.
 */

public class StartFSM implements FSMState {
    FSM fsm;
    public StartFSM(FSM fsm1) {
        this.fsm = fsm1;
    }
    @Override
    public String aquiredMessage(String message) {
        if(message.contains(Constant.START)){
            fsm.setFSMState(new RecFSM(fsm));
            return Constant.STATE;
        }
        return Constant.FAIL;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String whichState() {
        return "Start";
    }
}
