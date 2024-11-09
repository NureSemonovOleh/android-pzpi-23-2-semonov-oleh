package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private View colorPanel;
    private SeekBar seekBarR, seekBarG, seekBarB;
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
        getElements();
        setColor();
    }
    private void getElements(){
        colorPanel = findViewById(R.id.view);
        seekBarR = findViewById(R.id.seekR);
        seekBarG = findViewById(R.id.seekG);
        seekBarB = findViewById(R.id.seekB);
    }
    private void colorChanger(){
        int red = seekBarR.getProgress();
        int green = seekBarG.getProgress();
        int blue = seekBarB.getProgress();
        int finalColor = Color.rgb(red,green,blue);
        colorPanel.setBackgroundColor(finalColor);
    }
    private void setColor() {
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colorChanger();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekBarR.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarG.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarB.setOnSeekBarChangeListener(seekBarChangeListener);
    }

}