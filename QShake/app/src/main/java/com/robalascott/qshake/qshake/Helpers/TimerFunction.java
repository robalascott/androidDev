package com.robalascott.qshake.qshake.Helpers;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;

/**
 * Created by robscott on 2017-10-06.
 */

public class TimerFunction extends CountDownTimer {
    private TextView textViewtimer;
    private SegmentedProgressBar segmentedProgressBar;
    private boolean timerHasStarted;
    private long millisInFuture;
    public TimerFunction(long millisInFuture, long countDownInterval, TextView textViewtimer,SegmentedProgressBar segmentedProgressBar,boolean timerHasStarted) {
        super(millisInFuture, countDownInterval);
        this.millisInFuture = millisInFuture;
        this.textViewtimer = textViewtimer;
        this.segmentedProgressBar = segmentedProgressBar;
        this.timerHasStarted = timerHasStarted;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(segmentedProgressBar!=null ){
            segmentedProgressBar.playSegment(1);
            textViewtimer.setText("" + millisUntilFinished / 1000);
            segmentedProgressBar.incrementCompletedSegments();
        }
    }

    @Override
    public void onFinish() {
        textViewtimer.setText("Time's up!");
        segmentedProgressBar.reset();
        timerHasStarted = false;
    }



}
