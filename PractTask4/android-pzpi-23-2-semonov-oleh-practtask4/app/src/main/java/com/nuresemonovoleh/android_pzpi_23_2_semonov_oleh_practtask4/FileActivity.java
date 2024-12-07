package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_practtask4;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileActivity extends AppCompatActivity {

    private final String fileName = "example.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_file);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText input = findViewById(R.id.editTextInput);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonRead = findViewById(R.id.buttonRead);
        TextView output = findViewById(R.id.textViewOutput);

        buttonSave.setOnClickListener(v -> {
            String textToSave = input.getText().toString();
            if (!textToSave.isEmpty()){
                saveToFile(fileName, textToSave);
                input.setText("");
            }
            else{
                Toast.makeText(this, "Type smth in input",
                        Toast.LENGTH_SHORT).show();
            }
        });
        buttonRead.setOnClickListener(v -> {
            String fileContent = readFile(fileName);
            output.setText(fileContent);
        });
    }



    private void saveToFile(String fileName, String data){
        try (FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(data.getBytes());
            fos.close();
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error " , Toast.LENGTH_LONG).show();
        }
    }
    private String readFile(String fileName){
        StringBuilder fileContent = new StringBuilder();
        try (FileInputStream fis = openFileInput(fileName)) {
            int content;
            while ((content = fis.read()) != -1) {
                fileContent.append((char) content);
            }
            Toast.makeText(this, "Data read successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
        return fileContent.toString();
    }
}