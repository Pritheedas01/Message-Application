package com.pritha.www.otpverification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class NightActivity extends AppCompatActivity {
    int currentDayNight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night);

        currentDayNight = AppCompatDelegate.getDefaultNightMode();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (currentDayNight != AppCompatDelegate.getDefaultNightMode()) {
            recreate();
        }
    }

    public void gotoSettingsActivity(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}