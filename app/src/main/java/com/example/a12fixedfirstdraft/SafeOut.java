package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SafeOut extends GameActivity {
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.SafeOut - Return Message";
    DisplayMetrics displayMetrics;

    TextView lblSafeOut;

    Button btnBack;
    Button btnOut;
    Button btnSafe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safeout_layout);
        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.50),(int)(height*0.50));

        //views
        btnSafe = (Button) findViewById(R.id.btnSafe);
        btnOut = (Button) findViewById(R.id.btnOut);
        btnBack = (Button) findViewById(R.id.btnBack);

        lblSafeOut = (TextView) findViewById(R.id.lblSafeOut);

        btnSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Safe");
            }
        });

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("SOut");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        lblSafeOut.setText("Is The Runner:");

    }

    protected void returnResult(String result){
        Intent intent = new Intent();
        intent.putExtra(RESULT_KEY_MESSAGE,result);
        setResult(Activity.RESULT_OK,intent);
        finish();

    }

    public static String getResultKeyMessage(Intent intent){
        return intent.getStringExtra(RESULT_KEY_MESSAGE);
    }
}
