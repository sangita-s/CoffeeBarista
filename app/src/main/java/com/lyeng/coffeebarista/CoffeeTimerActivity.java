package com.lyeng.coffeebarista;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CoffeeTimerActivity extends AppCompatActivity {
    //TODO Get this from the object
    //private static final long START_TIME_IN_MILLIS = 25000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    long timeInMillis;
    double angle = 0;
    private double sweepangle;
    private CountDownTimer mCountDownTimer;
    private Boolean mTimerRunning = false;
    private long mTimeLeftInMilis;

    CircularActivityIndicator c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_timer);

        Intent data = getIntent();
        String name = data.getStringExtra("EXTRA_NAME");
        int minute = Integer.parseInt(data.getStringExtra("TIMER_TIME"));
        timeInMillis = minute * 60 * 1000;
        sweepangle = 360.0 / (timeInMillis / 1000.0);
        mTimeLeftInMilis = timeInMillis;

        mTextViewCountDown = findViewById(R.id.tv_countdown);
        mButtonStartPause = findViewById(R.id.btn_start_pause);
        mButtonReset = findViewById(R.id.btn_reset);

        c = findViewById(R.id.cv_cirInd);
        //c.setAngle(135);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer(timeInMillis);
            }
        });
        updateCountdownText();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMilis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMilis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
//                mButtonStartPause.setText("Start");
//                mButtonStartPause.setVisibility(View.INVISIBLE);
//                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();
        mTimerRunning = true;
        mButtonStartPause.setText("Pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
    }

    private void updateCountdownText() {
        int minutes = (int) (mTimeLeftInMilis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMilis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);

        c.setAngle(angle);
        angle = angle + sweepangle;
        Log.d("Angle after update", String.valueOf(angle));
        Log.d("Sweepangle", String.valueOf(sweepangle));
        c.invalidate();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer(long timeInMillis) {
        mTimeLeftInMilis = timeInMillis;
        updateCountdownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        //TODO Call invalidate properly after reset
        angle = 0;
        c.invalidate();
    }
}
