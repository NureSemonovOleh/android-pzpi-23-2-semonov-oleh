package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_practtask2_1;

import static android.view.View.INVISIBLE;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
        setContentView(R.layout.grid_layout_practice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main4), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private int count = 0;

    public void onButtonClick(View v){
        count++;
        Button button = (Button) v;
        button.setText("clicked" + count);
    }

    public void onColorClick(View v){
        Button button = findViewById(R.id.button6);
        int color = Color.rgb(255,0,0);
        button.setBackgroundColor(color);
    }
    public void onClearClick(View v){
        ImageView image = findViewById(R.id.imageView3);
        image.setVisibility(INVISIBLE);

    }
}