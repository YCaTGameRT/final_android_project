package com.ycatgamert.cat_tools;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {

    private TextView resultTextView;
    private String currentInput = "";
    private String operator = "";
    private double firstValue = 0;
    private boolean isOperatorPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        resultTextView = findViewById(R.id.resultTextView);

        int[] numberIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        View.OnClickListener numberListener = v -> {
            Button b = (Button) v;
            if (isOperatorPressed) {
                currentInput = "";
                isOperatorPressed = false;
            }
            currentInput += b.getText().toString();
            resultTextView.setText(currentInput);
        };
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberListener);
        }

        int[] operatorIds = {R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv};
        View.OnClickListener operatorListener = v -> {
            Button b = (Button) v;
            if (!currentInput.isEmpty()) {
                firstValue = Double.parseDouble(currentInput);
                operator = b.getText().toString();
                isOperatorPressed = true;
            }
        };
        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(operatorListener);
        }

        findViewById(R.id.btnC).setOnClickListener(v -> {
            currentInput = "";
            operator = "";
            firstValue = 0;
            resultTextView.setText("0");
        });

        findViewById(R.id.btnEqual).setOnClickListener(v -> {
            if (!currentInput.isEmpty() && !operator.isEmpty()) {
                double secondValue = Double.parseDouble(currentInput);
                double result = 0;
                switch (operator) {
                    case "+": result = firstValue + secondValue; break;
                    case "-": result = firstValue - secondValue; break;
                    case "*": result = firstValue * secondValue; break;
                    case "/": if (secondValue != 0) result = firstValue / secondValue; break;
                }
                currentInput = String.valueOf(result);
                if (currentInput.endsWith(".0")) {
                    currentInput = currentInput.substring(0, currentInput.length() - 2);
                }
                resultTextView.setText(currentInput);
                operator = "";
            }
        });
    }
}
