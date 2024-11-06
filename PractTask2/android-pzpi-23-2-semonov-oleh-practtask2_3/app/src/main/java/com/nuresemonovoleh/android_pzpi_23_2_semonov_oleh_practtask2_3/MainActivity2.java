package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_practtask2_3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(TAG, "onCreate 2: ");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart 2: ");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume 2: ");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause 2: ");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop 2: ");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy 2: ");
    }
    public void onButtonFinish(View v){
        finish();
    }
}