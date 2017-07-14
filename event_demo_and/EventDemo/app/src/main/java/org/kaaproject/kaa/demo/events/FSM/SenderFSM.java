package org.kaaproject.kaa.demo.events.FSM;

import android.util.Log;

import org.kaaproject.kaa.demo.events.Db.DBHelper;

import java.util.LinkedList;
import java.util.List;

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
        mydb = new DBHelper(fsm.getContext(),Dbase);
        user.add("Host");
    }
    @Override
    public String aquiredMessage(String message) {
        Log.i(Constant.FSMLOG,"Message" + message);

        /*Reg process for number of clients*/
        if(message.contains(Constant.STATE)){
            String temp1 = message.substring(0,message.indexOf(":"));
            Log.i(Constant.FSMLOG,"userFail" + user.size() + temp1);
            if(!user.contains(temp1)){
                user.add(temp1);
                Log.i(Constant.FSMLOG,"useradd" + user.size());
            }
            Log.i(Constant.FSMLOG,"userFail" + user.size());
            return Constant.REG;
        } else if(message.contains(Constant.EXITHOST)){
            user.clear();
            /*The host is still running*/
            user.add("Host");
            return Constant.Exit;
        }else {
        /*Store info here*/
            String temp = message.substring(message.indexOf(":")+1).trim();
            String temp1 = null;
            try{
                int tempsent = Integer.parseInt(temp);
                temp1 = message.substring(0,message.indexOf(":"));

                mydb.insertContact(temp1,Integer.parseInt(temp),getTime(),getTime()-tempsent,user.size());
            }catch(NumberFormatException e){
                Log.i(Constant.FSMLOG,"Error + Number format" + temp + " " + temp1);
            }
            return Constant.OK;
        }
    }

    @Override
    public void sendMessage(String message) {
        mydb.close();
    }

    @Override
    public String whichState() {
        return "Sender";
    }

    public int getTime(){
        return (int) (System.currentTimeMillis()%10000000);
    }


}
