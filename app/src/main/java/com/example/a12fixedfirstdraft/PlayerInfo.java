package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayerInfo extends GameActivity {
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.PlayerInfo - Return Message";
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtNumber;

    Button btnCreate;
    String[] info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerinfo_layout);

        // Setting views
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtNumber = (EditText) findViewById(R.id.txtNumber);
        btnCreate = (Button) findViewById(R.id.btnCreate);

        //Button create
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });

        Bundle bundle = getIntent().getExtras();
        info = bundle.getStringArray("info");
        btnCreate.setText("Create");
        setFields();
    }

    protected void setFields(){
        if (Boolean.valueOf(info[0])){
            txtFirstName.setText(info[1]);
            txtLastName.setText(info[2]);
            txtNumber.setText(info[3]);
            btnCreate.setText("Update");
        }
    }

    protected void returnResult(){
        String[] result = new String[4];
        result[0] = txtFirstName.getText().toString();
        result[1] = txtLastName.getText().toString();
        result[2] = txtNumber.getText().toString();
        result[3] = info[4];
        Intent i = new Intent();
        i.putExtra(RESULT_KEY_MESSAGE,result);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    public static String[] getResultKeyMessage(Intent intent){
        return intent.getStringArrayExtra(RESULT_KEY_MESSAGE);
    }
}
