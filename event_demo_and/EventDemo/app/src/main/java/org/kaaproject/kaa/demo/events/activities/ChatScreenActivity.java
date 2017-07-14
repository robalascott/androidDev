/**
 * Copyright 2014-2016 CyberVision, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.kaa.demo.events.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.kaaproject.kaa.demo.events.Db.DBHelper;
import org.kaaproject.kaa.demo.events.EventsDemoApp;
import org.kaaproject.kaa.demo.events.FSM.Constant;
import org.kaaproject.kaa.demo.events.FSM.FSM;
import org.kaaproject.kaa.demo.events.R;
import org.kaaproject.kaa.demo.events.utils.KaaChatManager;
import org.kaaproject.kaa.examples.event.Chat;
import org.kaaproject.kaa.examples.event.ChatEvent;
import org.kaaproject.kaa.examples.event.ChatEventType;
import org.kaaproject.kaa.examples.event.Message;

import java.io.IOException;

public class ChatScreenActivity extends AppCompatActivity implements Chat.Listener, AdapterView.OnItemSelectedListener {

    private Toolbar mToolbar;
    private View mButtonHost, mButtonClear,mButtonData,mButtonReset,mButtonText;
    private TextView mTextChatLogs;
    private EditText mEditTextInputMessage;
    private FSM fsm;
    private Spinner spinner;
    private int loop=1;
    Integer[] array = {1,5,10,100,200,300,500};
    private static final String ARGS_CHAT_NAME = "args_chat_name";
    @ColorInt
    private static final int COLOR_OTHER = 0xff004600;
    @ColorInt
    private static final int COLOR_YOU = 0xff322a61;
    private KaaChatManager mKaaChatManager;
    private String mChatName;

    /**
     * Open chat with name
     *
     * @param ctx
     * @param chatName
     */
    public static void open(Context ctx, String chatName) {
        ctx.startActivity(new Intent(ctx, ChatScreenActivity.class)
                .putExtra(ARGS_CHAT_NAME, chatName));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        /*FSM */
        fsm = new FSM(this);
        fsm.StartState();
        /*Kaa */
        mKaaChatManager = EventsDemoApp.app(this).getKaaChatManager();
        mChatName = getIntent().getExtras().getString(ARGS_CHAT_NAME);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        /*Spinner*/
        spinner = (Spinner) findViewById(R.id.spinner);
        Integer[] array = {1,5,10,100,200,300,500};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mChatName);
        }
        /*Textchatlogs*/
        mTextChatLogs = (TextView) findViewById(R.id.simple_chat_logs);
        mTextChatLogs.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTextChatLogs.setIncludeFontPadding(false);
        /*Host button*/
        mButtonHost = findViewById(R.id.Host);
        mButtonHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fsm.SenderState();
                mButtonHost.setVisibility(View.INVISIBLE);

                // send event to the subscribers
                sender(Constant.START);
                try {
                    Thread.sleep(3000);
                    for(int x = 0; x<loop; x++ ){
                        sender(" @ " + String.valueOf(getTime()));

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                sender(Constant.Exit);
                fsm.aquiredMessage(Constant.Exit);
                mButtonHost.setVisibility(View.VISIBLE);

                         }
        });
        /*Clear button*/
        mButtonClear =  findViewById(R.id.Clear);
        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fsm.StartState();
                mTextChatLogs.setText(null);

            }
        });
              /*Clear Show Data*/
        mButtonData =  findViewById(R.id.Data);
        mButtonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper mydb;
                String Dbase = "DBase";
                mTextChatLogs.setText(null);
                mydb = new DBHelper(getApplicationContext(),Dbase);
                mydb.getAllDiff(mTextChatLogs);
                mydb.close();

            }
        });
                 /*Clear Reset*/
        mButtonReset =  findViewById(R.id.Reset);
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper mydb;
                String Dbase = "DBase";
                mydb = new DBHelper(getApplicationContext(),Dbase);
                Log.i(Constant.FSMLOG,"Clear2");
                mydb.delete();
                mydb.close();
            }
        });
        /*Text TEST*/
        mButtonText =  findViewById(R.id.Text);
        mButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper mydb;
                String Dbase = "DBase";
                Log.i(Constant.FSMLOG,"Text");
                mydb = new DBHelper(getApplicationContext(),Dbase);

                mydb.close();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mKaaChatManager.addChatListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mKaaChatManager.removeChatListener(this);
    }

    @Override
    protected void onDestroy() {
        mKaaChatManager.sendEventToAll(new Message(
                mChatName, getString(R.string.activity_chat_chat_info_left_chat,
                EventsDemoApp.app(ChatScreenActivity.this)
                        .username())));
        super.onDestroy();
    }

    @Override
    public void onEvent(ChatEvent chatEvent, String s) {
        final String chatName = chatEvent.getChatName().trim();
        if (chatEvent.getEventType() == ChatEventType.DELETE) {
            if (chatName.equals(mChatName)) {
                finish();
            }
        }
    }

    @Override
    public void onEvent(Message message, String s) {
        if (mChatName.equals(message.getChatName())) {
            String temp= fsm.aquiredMessage(message.getMessage());
            if(temp.contains(Constant.FAIL) ||temp.contains(Constant.OK) ||temp.contains(Constant.Exit)||temp.contains(Constant.REG)){
                Log.i(Constant.CHATLOG, fsm.whichState() + "Event message ->" + temp + " " +s);
            }else{
               // appendChatEntry(message.getMessage(), COLOR_OTHER);
                sender(temp);
            }
        }
    }

    void appendChatEntry(final String entry, @ColorInt int color) {
        final SpannableString coloredEntry = new SpannableString("\n" + entry);
        coloredEntry.setSpan(new ForegroundColorSpan(color),
                1, entry.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTextChatLogs.append(coloredEntry);
    }


    /*General sender functions*/
    private void sender(String messageObject) {
        mKaaChatManager.sendEventToAll(
                new Message(mChatName,
                        getString(R.string.activity_chat_template_other,
                                EventsDemoApp.app(ChatScreenActivity.this)
                                        .username(), messageObject)));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.setSelection(position);
        loop = (int)parent.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }

    private int getTime(){
        return (int) (System.currentTimeMillis()%10000000);
    }
}
