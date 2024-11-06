package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_practtask2_3;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    private  EditText editText;
    private int counter = 0;
    private TextView txt;
    private CountDownTimer countDownTimer;
    private long timeInMillis = 120000;
    private boolean timerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editText = findViewById(R.id.editTextText);
        startTimer();
        Log.d(TAG, "onCreate: ");
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("text", editText.getText().toString());
        outState.putInt("counter",counter);
        outState.putLong("timeInMillis", timeInMillis);
        outState.putBoolean("timerRunning", timerRunning);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String text = savedInstanceState.getString("text");
        editText.setText(text);
        counter = savedInstanceState.getInt("counter");
        txt = findViewById(R.id.textView3);
        txt.setText("Clicks: " + counter);
        timeInMillis = savedInstanceState.getLong("timeInMillis");
        timerRunning = savedInstanceState.getBoolean("timerRunning");

    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart: ");
    }
    @Override
    protected void onResume(){
        super.onResume();
        resumeTimer();
        Log.d(TAG, "onResume: ");
    }
    @Override
    protected void onPause(){
        super.onPause();
        pauseTimer();
        Log.d(TAG, "onPause: ");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
    public void onButtonClick(View v){
        startActivity(new Intent(this, MainActivity2.class));
    }
    public void onCounterClick(View v) {
        counter++;
        txt = findViewById(R.id.textView3);
        txt.setText("Clicks: " + counter);
    }
    private void startTimer(){
        countDownTimer = new CountDownTimer(timeInMillis,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timeInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish(){
                timerRunning = false;
            }
        }.start();
        timerRunning = true;
    }
    private void pauseTimer(){
        if (timerRunning) {
            countDownTimer.cancel();
            timerRunning = false;
        }
    }
    private void resumeTimer(){
        if (!timerRunning) {
            startTimer();
        }
    }
    private void updateTimerText(){
        int seconds = (int) (timeInMillis / 1000);
        TextView timerView = findViewById(R.id.timer);
        timerView.setText(String.valueOf(seconds));
    }


}