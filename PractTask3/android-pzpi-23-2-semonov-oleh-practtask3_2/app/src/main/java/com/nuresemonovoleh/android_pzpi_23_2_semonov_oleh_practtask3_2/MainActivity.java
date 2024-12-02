package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_practtask3_2;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        Handler handler1 = new Handler(Looper.getMainLooper());
        Button startHandlerButton = findViewById(R.id.startHandlerButton1);
        startHandlerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = findViewById(R.id.handlerMessageTextView1);
                        textView.setText("Handler executed after delay");
                    }
                }, 2000);
            }
        });



        Handler handler2 = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                handler2.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = findViewById(R.id.handlerMessageTextView2);
                        textView.setText("Updated from background thread");
                    }
                });
            }
        }).start();




        Handler handler3 = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                TextView textView = findViewById(R.id.handlerMessageTextView3);
                textView.setText("Message received: " + msg.what);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                Message msg = handler3.obtainMessage();
                msg.what = 1;
                handler3.sendMessage(msg);
            }
        }).start();


    }


}