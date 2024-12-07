package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_practtask4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText Name,Age;
    private Button button;
    private SharedPreferences sp;

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

        Name = findViewById(R.id.editTextName);
        Age = findViewById(R.id.editTextAge);
        button = findViewById(R.id.button);
        sp = getSharedPreferences("Preferences",MODE_PRIVATE);

        loadData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String age = Age.getText().toString();

                if (!name.isEmpty() && !age.isEmpty()){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Name", name);
                    editor.putString("Age", age);
                    editor.apply();

                    Toast.makeText(MainActivity.this,"Saved", Toast.LENGTH_SHORT).show();
                    loadData();

                }
                else{
                    Toast.makeText(MainActivity.this,"Enter name and age", Toast.LENGTH_SHORT).show();

                }
            }
        });

        Button buttonOpenDB = findViewById(R.id.buttonDataBase);
        buttonOpenDB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,DBactivity.class);
            startActivity(intent);
        });
    }

    public void loadData() {
        String name = sp.getString("Name","No Name");
        String age = sp.getString("Age", "No Age");

        Name.setText(name);
        Age.setText(age);
    }




}