package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class HomeOrAway extends GameActivity {
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.HomeOrAway - Return Message";
    DisplayMetrics displayMetrics;

    Button btnHome;
    Button btnAway;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeoraway_layout);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHome(true);
                returnResult("Home");
            }
        });

        btnAway = (Button) findViewById(R.id.btnAway);
        btnAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHome(false);
                returnResult("Away");
            }
        });
    }

    protected void returnResult(String result){
        Intent i = new Intent();
        System.out.println(result+ " IS RESULT");
        String tmp = result;
        System.out.println(tmp +" IS TMP");
        i.putExtra(RESULT_KEY_MESSAGE,tmp);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    public static String getResultKeyMessage(Intent intent){
        return intent.getStringExtra(RESULT_KEY_MESSAGE);
    }
}
