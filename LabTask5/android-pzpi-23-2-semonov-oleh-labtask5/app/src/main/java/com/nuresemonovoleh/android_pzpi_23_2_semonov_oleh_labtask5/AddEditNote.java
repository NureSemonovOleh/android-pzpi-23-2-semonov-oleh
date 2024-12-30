package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask5;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Theme.applyTheme(this);
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

        DBHelper dbHelper = new DBHelper(this);


        Intent intent = getIntent();
        if (intent != null) {
            noteId = intent.getIntExtra("id", -1);
            if (noteId != -1) {
                Note existingNote = dbHelper.getNoteById(noteId);
                if (existingNote != null) {
                    titleEditText.setText(existingNote.getTitle());
                    descriptionEditText.setText(existingNote.getDescription());
                    importanceSpinner.setSelection(existingNote.getImportance() - 1);
                    selectedDate = existingNote.getDateTime().split(" ")[0];
                    selectedTime = existingNote.getDateTime().split(" ")[1];
                    dateTextView.setText(selectedDate);
                    timeTextView.setText(selectedTime);
                    if (existingNote.getImageUri() != null) {
                        imageUri = Uri.parse(existingNote.getImageUri());
                        imageView.setImageURI(imageUri);
                    }
                }
            }
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
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            int importance = importanceSpinner.getSelectedItemPosition() + 1;
            String dateTime = selectedDate + " " + selectedTime;
            String imageUriString = imageUri != null ? imageUri.toString() : null;

            if (noteId == -1) {
                long newNoteId = dbHelper.addNote(new Note(
                        titleEditText.getText().toString(),
                        descriptionEditText.getText().toString(),
                        importanceSpinner.getSelectedItemPosition() + 1,
                        selectedDate + " " + selectedTime,
                        imageUri != null ? imageUri.toString() : null
                ));

                Intent resultIntent = new Intent();
                resultIntent.putExtra("id", newNoteId);
                setResult(RESULT_OK, resultIntent);
                scheduleNotificationsForNote(title, description, newNoteId);
            } else {
                dbHelper.updateNote(new Note(noteId, title, description, importance, dateTime, imageUriString));
                Intent resultIntent = new Intent();
                resultIntent.putExtra("id", noteId);
                setResult(RESULT_OK, resultIntent);
                scheduleNotificationsForNote(title,description,noteId);
            }

            setResult(RESULT_OK);
            finish();
        });


    }


    private void scheduleNotificationsForNote(String title, String description, long noteId) {
        try {
            String dateTime = selectedDate + " " + selectedTime;
            long eventTime = Note.parseDateTime(dateTime).getTime();

            long oneDayBefore = eventTime - (24 * 60 * 60 * 1000);
            long threeHoursBefore = eventTime - (3 * 60 * 60 * 1000);
            long oneHourBefore = eventTime - (1 * 60 * 60 * 1000);

            if (oneDayBefore > System.currentTimeMillis()) {
                scheduleNotification(this, title, description, oneDayBefore, (int) (noteId * 1000 + 1));
            }
            if (threeHoursBefore > System.currentTimeMillis()) {
                scheduleNotification(this, title, description, threeHoursBefore, (int) (noteId * 1000 + 2));
            }
            if (oneHourBefore > System.currentTimeMillis()) {
                scheduleNotification(this, title, description, oneHourBefore, (int) (noteId * 1000 + 3));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error in Format", Toast.LENGTH_LONG).show();
        }
    }



    private void scheduleNotification(Context context, String title, String dateTime, long timeInMillis, int notificationId) {
        dateTime = selectedDate + " " + selectedTime;
        Intent intent = new Intent(context, Notification.class);
        intent.putExtra("title", title);
        intent.putExtra("datetime", dateTime);
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
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
