package com.robalascott.rednodechat.rednodechat.FSM;

import com.robalascott.rednodechat.rednodechat.Encryption.Encrypt;

import org.java_websocket.client.WebSocketClient;

/**
 * Created by robscott on 2017-06-21.
 */

public interface FSMState {

    void aquiredMessage(String message, WebSocketClient wc, Encrypt encrypted);
    String whichState();
}
