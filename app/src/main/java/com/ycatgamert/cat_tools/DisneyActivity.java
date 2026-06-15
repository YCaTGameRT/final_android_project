package com.ycatgamert.cat_tools;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class DisneyActivity extends AppCompatActivity {

    private TextView characterName;
    private TextView characterFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disney);

        characterName = findViewById(R.id.characterName);
        characterFilms = findViewById(R.id.characterFilms);

        findViewById(R.id.btnGetCharacter).setOnClickListener(v -> loadDisneyData());
    }

    private void loadDisneyData() {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.disneyapi.dev/character");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    if (dataArray.length() > 0) {
                        int randomIndex = new Random().nextInt(dataArray.length());
                        JSONObject character = dataArray.getJSONObject(randomIndex);

                        String name = character.getString("name");
                        JSONArray filmsArray = character.getJSONArray("films");

                        StringBuilder filmsBuilder = new StringBuilder();
                        for (int i = 0; i < filmsArray.length(); i++) {
                            filmsBuilder.append(filmsArray.getString(i)).append("\n");
                        }
                        String films = filmsBuilder.length() > 0 ? filmsBuilder.toString().trim() : "Нет данных";

                        runOnUiThread(() -> {
                            characterName.setText("Имя: " + name);
                            characterFilms.setText("Фильмы:\n" + films);
                        });
                    }
                } else {
                    showError();
                }
                connection.disconnect();
            } catch (Exception e) {
                showError();
            }
        }).start();
    }

    private void showError() {
        runOnUiThread(() -> Toast.makeText(DisneyActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show());
    }
}

