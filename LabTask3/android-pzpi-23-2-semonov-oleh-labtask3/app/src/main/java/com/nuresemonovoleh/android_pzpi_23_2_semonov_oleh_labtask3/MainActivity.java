package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MainActivity extends AppCompatActivity {

    private TextView output;

    private double firstNumber = 0;
    private double secondNumber = 0;
    private String operator = "";
    private String currInput = "";

    private final DecimalFormat decimalFormat = new DecimalFormat(
            "#.############",
            new DecimalFormatSymbols(){{
                setDecimalSeparator('.');
            }}
    );


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
        output = findViewById(R.id.textView);
    }

    public void onNumberButtonClick(View v){
        Button button = (Button) v;
        String btnText = button.getText().toString();

        if (btnText.equals(".")) {
            if (!currInput.contains(".")) {
                if (currInput.isEmpty()) {
                    currInput = "0.";
                } else {
                    currInput += ".";
                }
            }
        } else {
            currInput += btnText;
        }
        output.setText(currInput);
    }

    public void onOperatorButtonClick(View v){
        Button button = (Button) v;
        String opText = button.getText().toString();
        try {
            firstNumber = Double.parseDouble(currInput);
        } catch (NumberFormatException e){
            output.setText("Error");
            currInput = "";
            return;
        }
        operator = opText;
        output.setText(currInput);
        currInput = "";
    }

    public void onEqualButtonClick(View v){
        if (operator.isEmpty()) {
            output.setText("Error");
            return;
        }
        try {
            secondNumber = Double.parseDouble(currInput);
        } catch (NumberFormatException e ) {
            output.setText("Error");
            currInput = "";
            operator = "";
            return;
        }
        double result;
        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    output.setText("Error");
                    currInput = "";
                    operator = "";
                    return;
                }
                break;
            default:
                output.setText("Error");
                currInput = "";
                operator = "";
                return;
        }


        String resultText = decimalFormat.format(result);
        output.setText(resultText);
        currInput = resultText;
        operator = "";
    }

    public void onClearButtonClick(View v) {
        currInput = "";
        operator = "";
        firstNumber = 0;
        secondNumber = 0;
        output.setText("0");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("currInput", currInput);
        outState.putDouble("firstNumber", firstNumber);
        outState.putDouble("secondNumber", secondNumber);
        outState.putString("operator", operator);
        outState.putString("outputText", output.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currInput = savedInstanceState.getString("currInput", "");
        firstNumber = savedInstanceState.getDouble("firstNumber", 0);
        secondNumber = savedInstanceState.getDouble("secondNumber", 0);
        operator = savedInstanceState.getString("operator", "");
        String outputText = savedInstanceState.getString("outputText", "0");
        output.setText(outputText);
    }
}
