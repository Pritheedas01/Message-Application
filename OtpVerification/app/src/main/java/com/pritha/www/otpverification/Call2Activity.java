package com.pritha.www.otpverification;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Call2Activity extends AppCompatActivity {

    DatabaseHelper myDb;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call2);

        final TextView textView = findViewById(R.id.rec_view);
        myDb = new DatabaseHelper(this);
        String data = myDb.getData();
        textView.setText(data);
       textView.setMovementMethod(new ScrollingMovementMethod());


    }
}