package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask4;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditNote extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private Spinner importanceSpinner;
    private ImageView imageView;
    private Button saveButton, dateButton, timeButton;
    private TextView dateTextView, timeTextView;

    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageUri;

    private String selectedDate = "";
    private String selectedTime = "";
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        importanceSpinner = findViewById(R.id.spinnerImportance);
        imageView = findViewById(R.id.imageViewNote);
        saveButton = findViewById(R.id.buttonSave);
        dateButton = findViewById(R.id.buttonSelectDate);
        timeButton = findViewById(R.id.buttonSelectTime);
        dateTextView = findViewById(R.id.textViewDate);
        timeTextView = findViewById(R.id.textViewTime);


        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            int importance = intent.getIntExtra("importance", 1);
            String imageUriString = intent.getStringExtra("imageUri");
            selectedDate = intent.getStringExtra("date");
            selectedTime = intent.getStringExtra("time");
            position = intent.getIntExtra("position", -1);

            if (title != null) titleEditText.setText(title);
            if (description != null) descriptionEditText.setText(description);
            importanceSpinner.setSelection(importance - 1);
            if (imageUriString != null) {
                imageUri = Uri.parse(imageUriString);
                imageView.setImageURI(imageUri);
            }
            if (selectedDate != null) dateTextView.setText(selectedDate);
            if (selectedTime != null) timeTextView.setText(selectedTime);
        }


        imageView.setOnClickListener(v -> {
            Intent intentImage = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intentImage.addCategory(Intent.CATEGORY_OPENABLE);
            intentImage.setType("image/*");
            startActivityForResult(intentImage, PICK_IMAGE_REQUEST);
        });


        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                dateTextView.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        timeButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedTime = hourOfDay + ":" + String.format("%02d", minute);
                timeTextView.setText(selectedTime);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });


        saveButton.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putExtra("title", titleEditText.getText().toString());
            data.putExtra("description", descriptionEditText.getText().toString());
            data.putExtra("importance", importanceSpinner.getSelectedItemPosition() + 1);
            data.putExtra("date", selectedDate);
            data.putExtra("time", selectedTime);
            if (imageUri != null) {
                data.putExtra("imageUri", imageUri.toString());
            }
            data.putExtra("position", position);
            setResult(RESULT_OK, data);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);


            getContentResolver().takePersistableUriPermission(imageUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }
}
