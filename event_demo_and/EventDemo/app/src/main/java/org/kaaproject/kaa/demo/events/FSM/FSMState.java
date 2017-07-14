package org.kaaproject.kaa.demo.events.FSM;

/**
 * Created by robscott on 2017-06-21.
 */

public interface FSMState {

    String aquiredMessage(String message);
    void sendMessage(String message);
    String whichState();
}
