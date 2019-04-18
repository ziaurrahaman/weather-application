package com.example.hp.shojolweatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChangeCityControlller extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city_controlller);
        final EditText editTextField = (EditText) findViewById(R.id.queryET);
        final ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String newCity = editTextField.getText().toString();
                Intent newCityIntent = new Intent(ChangeCityControlller.this,WeatherController.class);
                newCityIntent.putExtra("city",newCity);
                startActivity(newCityIntent);
                return false;
            }
        });

    }
}
