package com.example.a12fixedfirstdraft;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class SubPlayer extends GameActivity {
    DisplayMetrics displayMetrics;

    Button btnRoster;
    Button btnPositions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subplayer_layout);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.95),(int)(height*0.9));

        //views
        btnRoster = (Button) findViewById(R.id.btnRoster);
        btnPositions = (Button) findViewById(R.id.btnPositions);


        btnRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTable("ROSTER");
            }
        });

        btnPositions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTable("POSITIONS");
            }
        });



    }

    protected void updateTable(String playerType){
        switch (playerType){
            case ("ROSTER"):
                String[] RHeaders = {"First Name"};
                break;
            case ("POSITIONS"):
                break;
        }

    }
}
