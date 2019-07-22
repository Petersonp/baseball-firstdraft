package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Pitch extends GameActivity {
    public static final int REQUEST_CODE_GETMESSAGE = 1;
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.Pitch - Return Message";
    DisplayMetrics displayMetrics;
    Button btnBallInPlay;
    Button btnStrike;
    Button btnBall;
    TextView lblPitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pitch_layout);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));

        btnBallInPlay = (Button) findViewById(R.id.btnBallInPlay);
        btnBallInPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),BallInPlay.class);
                startActivityForResult(i,REQUEST_CODE_GETMESSAGE);

            }
        });

        btnStrike = (Button) findViewById(R.id.btnStrike);
        btnStrike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Strike");
            }
        });

        btnBall = (Button) findViewById(R.id.btnBall);
        btnBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Ball");
            }
        });
    }

    protected void returnResult(String result){
        String[] message = new String[2];
        message[0] = result;
        message[1] = result;
        Intent intent = new Intent();
        intent.putExtra(RESULT_KEY_MESSAGE,message);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_GETMESSAGE:
                if(resultCode == Activity.RESULT_OK){
                    String[] message = BallInPlay.getResultKeyMessage(data);

                    //System.out.println(message+" Messege at MainActivity --------------------");
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_KEY_MESSAGE,message);
                    setResult(Activity.RESULT_OK,intent);
                    finish();

                }
        }
    }
    public static String[] getResultKeyMessage(Intent intent){
        return intent.getStringArrayExtra(RESULT_KEY_MESSAGE);
    }
}
