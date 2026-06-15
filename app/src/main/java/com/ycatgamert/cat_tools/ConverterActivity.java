package com.ycatgamert.cat_tools;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import java.util.Locale;

public class ConverterActivity extends AppCompatActivity {

    private EditText inputValue;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TextView resultTextView;
    private TabLayout tabLayout;

    private final String[] lengthUnits = {"Метры", "Километры", "Сантиметры", "Миллиметры", "Дюймы", "Футы"};
    private final String[] massUnits = {"Граммы", "Килограммы", "Тонны", "Фунты", "Унции"};
    private final String[] volumeUnits = {"Литры", "Миллилитры", "Куб. метры", "Галлоны"};
    private final String[] timeUnits = {"Секунды", "Минуты", "Часы", "Сутки"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        inputValue = findViewById(R.id.inputValue);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        resultTextView = findViewById(R.id.resultTextView);
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.setTabTextColors(Color.WHITE, Color.parseColor("#FFF600"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFF600"));

        tabLayout.addTab(tabLayout.newTab().setText("Длина"));
        tabLayout.addTab(tabLayout.newTab().setText("Масса"));
        tabLayout.addTab(tabLayout.newTab().setText("Объём"));
        tabLayout.addTab(tabLayout.newTab().setText("Время"));

        updateSpinners(lengthUnits);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                resultTextView.setText("Результат: 0");
                inputValue.setText("");
                switch (tab.getPosition()) {
                    case 0: updateSpinners(lengthUnits); break;
                    case 1: updateSpinners(massUnits); break;
                    case 2: updateSpinners(volumeUnits); break;
                    case 3: updateSpinners(timeUnits); break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        findViewById(R.id.btnConvert).setOnClickListener(v -> {
            String inputText = inputValue.getText().toString().trim();
            if (!inputText.isEmpty()) {
                try {
                    double value = Double.parseDouble(inputText);
                    String fromUnit = spinnerFrom.getSelectedItem().toString();
                    String toUnit = spinnerTo.getSelectedItem().toString();
                    int currentTab = tabLayout.getSelectedTabPosition();

                    double result = 0;
                    if (currentTab == 0) {
                        result = convertLength(value, fromUnit, toUnit);
                    } else if (currentTab == 1) {
                        result = convertMass(value, fromUnit, toUnit);
                    } else if (currentTab == 2) {
                        result = convertVolume(value, fromUnit, toUnit);
                    } else if (currentTab == 3) {
                        result = convertTime(value, fromUnit, toUnit);
                    }

                    resultTextView.setText(String.format(Locale.US, "Результат: %,.4f", result));
                } catch (NumberFormatException e) {
                    Toast.makeText(ConverterActivity.this, "Ошибка ввода", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ConverterActivity.this, "Введите число", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSpinners(String[] units) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, units);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
    }

    private double convertLength(double value, String from, String to) {
        double meters = value;
        switch (from) {
            case "Километры": meters = value * 1000.0; break;
            case "Сантиметры": meters = value / 100.0; break;
            case "Миллиметры": meters = value / 1000.0; break;
            case "Дюймы": meters = value / 39.3701; break;
            case "Футы": meters = value / 3.28084; break;
        }
        switch (to) {
            case "Километры": return meters / 1000.0;
            case "Сантиметры": return meters * 100.0;
            case "Миллиметры": return meters * 1000.0;
            case "Дюймы": return meters * 39.3701;
            case "Футы": return meters * 3.28084;
            default: return meters;
        }
    }

    private double convertMass(double value, String from, String to) {
        double grams = value;
        switch (from) {
            case "Килограммы": grams = value * 1000.0; break;
            case "Тонны": grams = value * 1000000.0; break;
            case "Фунты": grams = value * 453.592; break;
            case "Унции": grams = value * 28.3495; break;
        }
        switch (to) {
            case "Килограммы": return grams / 1000.0;
            case "Тонны": return grams / 1000000.0;
            case "Фунты": return grams / 453.592;
            case "Унции": return grams / 28.3495;
            default: return grams;
        }
    }

    private double convertVolume(double value, String from, String to) {
        double liters = value;
        switch (from) {
            case "Миллилитры": liters = value / 1000.0; break;
            case "Куб. метры": liters = value * 1000.0; break;
            case "Галлоны": liters = value * 3.78541; break;
        }
        switch (to) {
            case "Миллилитры": return liters * 1000.0;
            case "Куб. метры": return liters / 1000.0;
            case "Галлоны": return liters / 3.78541;
            default: return liters;
        }
    }

    private double convertTime(double value, String from, String to) {
        double seconds = value;
        switch (from) {
            case "Минуты": seconds = value * 60.0; break;
            case "Часы": seconds = value * 3600.0; break;
            case "Сутки": seconds = value * 86400.0; break;
        }
        switch (to) {
            case "Минуты": return seconds / 60.0;
            case "Часы": return seconds / 3600.0;
            case "Сутки": return seconds / 86400.0;
            default: return seconds;
        }
    }
}



