package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask5;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NOTIFICATIONS = 1001;
    private RecyclerView recyclerViewNotes;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Theme.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATIONS);
            }
        }

        Notification.createNotificationChannel(this);


        dbHelper = new DBHelper(this);

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this);

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(noteAdapter);

        loadNotes();

        registerForContextMenu(recyclerViewNotes);

    }

    private void loadNotes() {
        List<Note> notesFromDb = dbHelper.getAllNotes();
        if (notesFromDb != null) {
            noteList.clear();
            noteList.addAll(notesFromDb);

            noteAdapter = new NoteAdapter(noteList, this);
            recyclerViewNotes.setAdapter(noteAdapter);
        } else {
            Toast.makeText(this, "Zero Notes", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return true;
            }
        });

        return true;
    }
    private void filterNotes(String query) {
        List<Note> filteredList = dbHelper.searchNotes(query);
        noteAdapter.updateList(filteredList);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_note) {
            Intent intent = new Intent(this, AddEditNote.class);
            startActivityForResult(intent, 1); // Код 1 для додавання
            return true;
        } else if (item.getItemId() == R.id.menu_filter) {
            showImportanceFilterDialog();
            return true;
        }
        else if (item.getItemId() == R.id.menu_settings){
            showThemeSettingDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showThemeSettingDialog(){
        String[] themes = {"Light Theme", "Dark Theme", "System Default"};
        new AlertDialog.Builder(this)
                .setTitle("Choose Your Destiny")
                .setSingleChoiceItems(themes, Theme.getSavedTheme(this), ((dialog, which) -> {
                    Theme.saveTheme(this, which);
                    dialog.dismiss();
                    showFontSizeSettingDialog();
                })).show();
    }
    private void showFontSizeSettingDialog() {
        String[] fontSizes = {"Small", "Medium", "Big"};

        new AlertDialog.Builder(this)
                .setTitle("Choose Font Size")
                .setSingleChoiceItems(fontSizes, Theme.getSavedFontSize(this), (dialog, which) -> {
                    Theme.saveFontSize(this, which);
                    dialog.dismiss();
                    recreate();
                })
                .show();
    }
    private void showImportanceFilterDialog() {
        String[] importanceOptions = {"All", "Low Importance", "Middle Importance", "High Importance"};
        new android.app.AlertDialog.Builder(this)
                .setTitle("Importance Filter")
                .setItems(importanceOptions, (dialog, which) -> {
                    if (which == 0) {
                        loadNotes();
                    } else {
                        filterNotesByImportance(which);
                    }
                })
                .show();
    }

    private void filterNotesByImportance(int importance) {
        List<Note> filteredList = dbHelper.getAllNotes();
        filteredList.removeIf(note -> note.getImportance() != importance);
        noteAdapter.updateList(filteredList);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            int importance = data.getIntExtra("importance", 1);
            String dateTime = data.getStringExtra("dateTime");
            String imageUri = data.getStringExtra("imageUri");

            if (requestCode == 1) { // Добавление новой заметки
                Note newNote = new Note(0, title, description, importance, dateTime, imageUri);
                dbHelper.addNote(newNote);
                loadNotes();
            } else if (requestCode == 2) { // Редактирование заметки
                int noteId = data.getIntExtra("id", -1);
                Note updatedNote = new Note(noteId, title, description, importance, dateTime, imageUri);
                dbHelper.updateNote(updatedNote);
                loadNotes();
            }

            recreate();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = noteAdapter.getContextMenuPosition();

        if (position < 0 || position >= noteList.size()) {
            return super.onContextItemSelected(item);
        }

        Note selectedNote = noteList.get(position);

        if (item.getItemId() == R.id.menu_edit_note) {
            Intent intent = new Intent(this, AddEditNote.class);
            intent.putExtra("id", selectedNote.getId());
            intent.putExtra("title", selectedNote.getTitle());
            intent.putExtra("description", selectedNote.getDescription());
            intent.putExtra("importance", selectedNote.getImportance());
            intent.putExtra("dateTime", selectedNote.getDateTime());
            intent.putExtra("imageUri", selectedNote.getImageUri());
            startActivityForResult(intent, 2);
            return true;
        } else if (item.getItemId() == R.id.menu_delete_note) {
            dbHelper.deleteNote(selectedNote.getId());
            cancelNotification(this, (int) (selectedNote.getId() * 1000 + 1));
            cancelNotification(this, (int) (selectedNote.getId() * 1000 + 2));
            cancelNotification(this, (int) (selectedNote.getId() * 1000 + 3));

            loadNotes();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void cancelNotification(Context context, int notificationId) {
        Intent intent = new Intent(context, Notification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }
}
