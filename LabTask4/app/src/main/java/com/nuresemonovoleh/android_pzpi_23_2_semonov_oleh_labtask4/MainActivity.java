package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask4;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private List<Note> originalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        noteList = new ArrayList<>();
        originalList = new ArrayList<>();

        originalList.add(new Note("Note 1", "Description 1", 1, "2024-12-28 12:00", null));
        originalList.add(new Note("Note 2", "Description 2", 2, "2024-12-28 14:30", null));
        originalList.add(new Note("Note 3", "Description 3", 3, "2024-12-28 16:00", null));

        noteList.addAll(originalList);

        noteAdapter = new NoteAdapter(noteList, this);
        recyclerViewNotes.setAdapter(noteAdapter);

        registerForContextMenu(recyclerViewNotes);
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
        return super.onOptionsItemSelected(item);
    }

    private void showImportanceFilterDialog() {
        String[] importanceOptions = {"All", "Low Importance", "Middle Importance", "High Importance"};
        new android.app.AlertDialog.Builder(this)
                .setTitle("Importance Filter")
                .setItems(importanceOptions, (dialog, which) -> {
                    if (which == 0) {
                        restoreOriginalList();
                    } else {
                        filterNotesByImportance(which);
                    }
                })
                .show();
    }

    private void filterNotesByImportance(int importance) {
        List<Note> filteredList = new ArrayList<>();
        for (Note note : originalList) {
            if (note.getImportance() == importance) {
                filteredList.add(note);
            }
        }
        updateDisplayedList(filteredList);
    }

    private void filterNotes(String query) {
        List<Note> filteredList = new ArrayList<>();
        for (Note note : originalList) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    note.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(note);
            }
        }
        updateDisplayedList(filteredList);
    }

    private void restoreOriginalList() {
        updateDisplayedList(new ArrayList<>(originalList));
    }

    private void updateDisplayedList(List<Note> newList) {
        noteList.clear();
        noteList.addAll(newList);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            int importance = data.getIntExtra("importance", 1);
            String date = data.getStringExtra("date");
            String time = data.getStringExtra("time");
            String imageUri = data.getStringExtra("imageUri");
            int position = data.getIntExtra("position", -1);

            if (requestCode == 1) {
                Note newNote = new Note(title, description, importance, date + " " + time, imageUri);

                originalList.add(newNote);
                restoreOriginalList();
            } else if (requestCode == 2 && position != -1) {
                Note updatedNote = noteList.get(position);
                updatedNote.setTitle(title);
                updatedNote.setDescription(description);
                updatedNote.setImportance(importance);
                updatedNote.setDateTime(date + " " + time);
                updatedNote.setImageUri(imageUri);

                int originalIndex = originalList.indexOf(noteList.get(position));
                if (originalIndex != -1) {
                    originalList.set(originalIndex, updatedNote);
                }

                noteAdapter.notifyItemChanged(position);
            }
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

        if (item.getItemId() == R.id.menu_edit_note) {
            Note selectedNote = noteList.get(position);

            Intent intent = new Intent(this, AddEditNote.class);
            intent.putExtra("title", selectedNote.getTitle());
            intent.putExtra("description", selectedNote.getDescription());
            intent.putExtra("importance", selectedNote.getImportance());
            intent.putExtra("imageUri", selectedNote.getImageUri());
            String[] dateTimeParts = selectedNote.getDateTime().split(" ");
            intent.putExtra("date", dateTimeParts.length > 0 ? dateTimeParts[0] : "");
            intent.putExtra("time", dateTimeParts.length > 1 ? dateTimeParts[1] : "");
            intent.putExtra("position", position);
            startActivityForResult(intent, 2);
            return true;
        } else if (item.getItemId() == R.id.menu_delete_note) {

            Note noteToRemove = noteList.get(position);
            originalList.remove(noteToRemove);
            noteList.remove(position);
            noteAdapter.notifyItemRemoved(position);
            return true;
        }

        return super.onContextItemSelected(item);
    }

}
