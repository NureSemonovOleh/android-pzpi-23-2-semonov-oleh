package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_practtask4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DBactivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private RecyclerView rvUsers;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.database);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.DBMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DBHelper(this);
        rvUsers = findViewById(R.id.userList);
        Button buttonAddUser = findViewById(R.id.buttonAddUser);

        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        loadUsers();

        buttonAddUser.setOnClickListener(v -> showAddUserDialog());


        Button buttonOpenFileActivity = findViewById(R.id.buttonFileActivity);
        buttonOpenFileActivity.setOnClickListener(v -> {
            Intent intent = new Intent(DBactivity.this, FileActivity.class);
            startActivity(intent);
        });

    }
    private void loadUsers() {
        List<DBHelper.User> users = dbHelper.getAllUsers();
        userAdapter = new UserAdapter(users);
        rvUsers.setAdapter(userAdapter);
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_database, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextDialogName);
        EditText editTextAge = dialogView.findViewById(R.id.editTextDialogAge);
        Button buttonSave = dialogView.findViewById(R.id.buttonDialogSave);
        Button buttonCancel = dialogView.findViewById(R.id.buttonDialogCancel);

        AlertDialog dialog = builder.create();

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String ageText = editTextAge.getText().toString();

            if (!name.isEmpty() && !ageText.isEmpty()) {
                int age = Integer.parseInt(ageText);
                dbHelper.addUser(name, age);
                dialog.dismiss();
                loadUsers();
            }
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }



}