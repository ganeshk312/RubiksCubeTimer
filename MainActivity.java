package com.spider.gk.rubikscubetimer;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.opengl.Visibility;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import java.util.Timer;
import android.os.Handler;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;
import static com.spider.gk.rubikscubetimer.R.id.InspectionTime;

public class MainActivity extends AppCompatActivity {
    public long tapStatus = 0;
    int Beep = 0;
    CountDownTimer countDownTimer = null;
    Handler myHandler = new Handler();
    Long time;
    Long timeInMillies, finalTime, startTime;
    Runnable updateTimerMethod = new Runnable() {
        public void run() {

            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000) / 10;
            TextView solveTime = (TextView) findViewById(R.id.solveTime);
            solveTime.setText("" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + ":" + String.format("%02d", milliseconds));
            myHandler.postDelayed(this, 0);
        }
    };
    private boolean stopWatchStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ScreenTap(View view) {


        if (tapStatus == 0) {
            startTimer();
        } else if (tapStatus == 1) {
            stopTimer();
            startStopwatch();
        } else if (tapStatus == 2) {
            stopStopWatch();
        }
        else if (tapStatus==3){
            tapStatus=0;
            TextView InspectionTime = (TextView) findViewById(R.id.InspectionTime);
            InspectionTime.setText("15:00");
            TextView solveTime=(TextView) findViewById(R.id.solveTime);
            solveTime.setText("00:00:00");
            Toast.makeText(this, "Timer RESET", Toast.LENGTH_SHORT).show();

                            }
    }

    public void startTimer() {
        {
            Toast.makeText(this, "15sec of Inspection Time started", Toast.LENGTH_SHORT).show();
Beep=0;
            tapStatus = 1;
            countDownTimer = new CountDownTimer(15000, 10) {
                public void onTick(long millisUntilFinished) {
                    updateTimer(millisUntilFinished);

                }

                public void onFinish() {
                    TimerReset();
                    startStopwatch();
                }
            };
            countDownTimer.start();
        }

    }

    public void updateTimer(long millisUntilFinished) {
        TextView InspectionTime = (TextView) findViewById(R.id.InspectionTime);
        Long sec = millisUntilFinished / 1000;

        Long milliSec = (millisUntilFinished % 1000) / 10;


        if (sec == 3 && milliSec < 1 && Beep == 0) {
            Toast.makeText(this, "3 sec left for inspection",Toast.LENGTH_SHORT ).show();
            ToneGenerator toneGen=new ToneGenerator(AudioManager.STREAM_MUSIC,100);
            toneGen.startTone(ToneGenerator.TONE_PROP_BEEP);
            toneGen.release();
            Beep=1;
        }
        InspectionTime.setText(String.format("%02d", sec) + ":" + String.format("%02d", milliSec));
    }

    public void TimerReset() {
        TextView InspectionTime = (TextView) findViewById(R.id.InspectionTime);
        InspectionTime.setText("00:00");
    }

    public void stopTimer() {
        countDownTimer.cancel();
    }

    public void startStopwatch() {
        Toast.makeText(this, "Stopwatch for Slove time started", Toast.LENGTH_SHORT).show();
        tapStatus = 2;
        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);
    }

    public void stopStopWatch() {
        myHandler.removeCallbacks(updateTimerMethod);
        tapStatus=3;
        Toast.makeText(this, "Congrats for solving the Cube", Toast.LENGTH_SHORT).show();
    }



}
