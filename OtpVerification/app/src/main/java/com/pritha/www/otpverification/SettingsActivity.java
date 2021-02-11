package com.pritha.www.otpverification;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    Spinner spinner;
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //assert getSupportActionBar() != null;
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = findViewById(R.id.spinner);

        /*final ArrayList<String> fruits1 = new ArrayList<>();

        fruits1.add("FOLLLOW_SYSTEM");
        fruits1.add("YES");
        fruits1.add("NO");
        fruits1.add("AUTO");*/



        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                //R.layout.support_simple_spinner_dropdown_item, fruits1);
        //spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),"you selected "+ fruits1.get(position),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onItemSelected: " + position );
                handleNightMode(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: ");
            }
        });


    }

    private void handleNightMode(int position) {

        switch (position) {
            case 0:
                Log.e(TAG, "Nothing Selected");
                break;
            case 1:
                Log.e(TAG, "FOLLLOW_SYSTEM");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                getDelegate().applyDayNight();
                break;
            case 2:
                Log.e(TAG, "YES");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                getDelegate().applyDayNight();
                break;
            case 3:
                Log.e(TAG, "NO");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                getDelegate().applyDayNight();
                break;
            case 4:
                Log.e(TAG, "AUTO");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                getDelegate().applyDayNight();
                break;
            default:
                Log.e(TAG, "FOLLLOW_SYSTEM");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
