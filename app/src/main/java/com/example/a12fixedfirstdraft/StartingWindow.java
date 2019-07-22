package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class StartingWindow extends GameActivity {
    public static final int REQUEST_CODE_GETMESSAGE_LINEUP = 9;
    public static final int REQUEST_CODE_GETMESSAGE_ROSTER = 8;
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.StartingWindow - Return Message";
    Button btnNewGame;
    Button btnRoster;

    private PlayerNode head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startingwindow_layout);
        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LineUp.class);
                startActivityForResult(i, REQUEST_CODE_GETMESSAGE_LINEUP);
                printPlayerNodes();
            }
        });

        btnRoster = (Button) findViewById(R.id.btnRoster);
        btnRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Roster.class);
                startActivityForResult(i, REQUEST_CODE_GETMESSAGE_ROSTER);
            }
        });
    }

    protected void printPlayerNodes(){
        if(getHead() != null) {
            System.out.println("PRINGINT INDEXES");
            PlayerNode tmp = getHead();
            System.out.println(tmp.data.getFirstName() +" "+tmp.data.getLastName()+": " + tmp.index);
            while (tmp.next != null) {
                System.out.println(tmp.next.data.getFirstName() +" "+tmp.next.data.getLastName()+": " + tmp.next.index);
                tmp = tmp.next;

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_GETMESSAGE_LINEUP:
                if (resultCode == Activity.RESULT_OK){
                    Intent i = new Intent();
                    String result = "NOTHING";
                    i.putExtra(RESULT_KEY_MESSAGE,result);
                    setResult(Activity.RESULT_OK,i);
                    finish();
                }
        }
    }
}
