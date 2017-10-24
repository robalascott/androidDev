package com.robalascott.qshake.qshake.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;
import com.robalascott.qshake.qshake.Helpers.TimerFunction;
import com.robalascott.qshake.qshake.R;

/**
 * Created by robscott on 2017-09-21.
 */


public class CurrentGameFragment extends Fragment implements View.OnClickListener{
    public Button btnTimer;
    public TextView textViewtimer;
    private TimerFunction timer;
    private boolean timerHasStarted = false;
    private final long startTime = 15 * 1000;
    private final long interval = 1 * 1000;
    private SegmentedProgressBar segmentedProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_game_menu, container, false);
        btnTimer = (Button)rootView.findViewById(R.id.btn_timer);
        btnTimer.setText(R.string.start_settings);
        btnTimer.setOnClickListener(this);
        textViewtimer = (TextView)rootView.findViewById(R.id.textViewTimer);
        /**
         * Plug-in here get time intervals and so on
         */
        segmentedProgressBar = (SegmentedProgressBar) rootView.findViewById(R.id.segmented_progressbar);
        settingSegmentedProgressBar(segmentedProgressBar,15,1000);
        timer = new TimerFunction(startTime,interval,textViewtimer,segmentedProgressBar,timerHasStarted);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        if (timerHasStarted==true) {;
            timer.cancel();
            b.setText(R.string.start_settings);
            textViewtimer.setText(Long.toString(startTime));
            timerHasStarted = false;
            segmentedProgressBar.reset();
        } else {
            b.setText(R.string.stop_settings);
            timer.start();
            timerHasStarted = true;
        }
    }

    public void settingSegmentedProgressBar( SegmentedProgressBar segmentedProgressBar,int startTime,long time){
        segmentedProgressBar.setSegmentCount(startTime);
        segmentedProgressBar.setContainerColor(Color.BLUE);
        segmentedProgressBar.setFillColor(Color.RED);
    }


  /*  public void timer(final TextView mTextField,final Button btn){
        btn.setText(R.string.stop_settings);
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                mTextField.setText("0");
                btn.setText(R.string.start_settings);
            }
        }.start();
    }

*/

}


