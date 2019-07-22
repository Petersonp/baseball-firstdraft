package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BallInPlay2 extends GameActivity {
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.BallInPlay2 - Return Message";
    DisplayMetrics displayMetrics;
    Button btnBack;
    TextView lblBallInPlay2;
    Button btnOut;
    Button btnSingle;
    Button btnDouble;
    Button btnTriple;
    Button btnInParkHomeRun;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ON THE GROUND
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballinplay2_layout);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("Title");

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));

        lblBallInPlay2 = (TextView) findViewById(R.id.lblBallInPlay2);
        lblBallInPlay2.setText(title);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BallInPlay.class));
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnOut = (Button) findViewById(R.id.btnOut);
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Out");

            }
        });

        btnSingle = (Button) findViewById(R.id.btnSingle);
        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Single");

            }
        });

        btnDouble = (Button) findViewById(R.id.btnDouble);
        btnDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Double");

            }
        });

        btnTriple = (Button) findViewById(R.id.btnTriple);
        btnTriple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Triple");

            }
        });

        btnInParkHomeRun = (Button) findViewById(R.id.btnInParkHomeRun);
        btnInParkHomeRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("In-The-Park Home Run");

            }
        });
    }

    public void returnResult(String result){
        String[] message = new String[3];
        message[1] = title;
        message[0] = result;
        Intent i = new Intent();
        i.putExtra(RESULT_KEY_MESSAGE,message);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    public static String[] getResultKeyMessage(Intent intent){
        return intent.getStringArrayExtra(RESULT_KEY_MESSAGE);
    }
}