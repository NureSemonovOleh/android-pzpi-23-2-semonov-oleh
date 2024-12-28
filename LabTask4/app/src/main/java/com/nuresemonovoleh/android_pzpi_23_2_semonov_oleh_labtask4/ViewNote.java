package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask4;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewNote extends AppCompatActivity {

    private TextView titleView, descriptionView, dateTimeView, importanceView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleView = findViewById(R.id.textViewTitle);
        descriptionView = findViewById(R.id.textViewDescription);
        dateTimeView = findViewById(R.id.textViewDateTime);
        importanceView = findViewById(R.id.textViewImportance);
        imageView = findViewById(R.id.imageViewNote);


        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String dateTime = getIntent().getStringExtra("dateTime");
        int importance = getIntent().getIntExtra("importance", 1);
        String imageUri = getIntent().getStringExtra("imageUri");


        titleView.setText(title);
        descriptionView.setText(description);
        dateTimeView.setText(dateTime);

        String importanceText;
        switch (importance) {
            case 1: importanceText = "Low Importance"; break;
            case 2: importanceText = "Middle Importance"; break;
            case 3: importanceText = "High Importance"; break;
            default: importanceText = "Unknown";
        }
        importanceView.setText(importanceText);

        if (imageUri != null && !imageUri.isEmpty()) {
            imageView.setImageURI(Uri.parse(imageUri));
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}