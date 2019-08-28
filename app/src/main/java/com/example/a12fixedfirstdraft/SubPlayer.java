package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SubPlayer extends GameActivity {
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.SubPlayer - Return Message";
    DisplayMetrics displayMetrics;

    TextView lblSub;

    Button btnRoster;
    Button btnPositions;
    Button btnBack;

    int count = 0;
    int row_id = 5062;
    int sub_id = 5162;
    int first_id = 5262;
    int last_id = 5362;
    int no_id = 5462;
    int pos_id = 5562;

    int[] ids = {first_id,last_id,no_id,pos_id};

    TableLayout tblStats;

    String[] player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subplayer_layout);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.95),(int)(height*0.9));

        Bundle bundle = getIntent().getExtras();
        player = bundle.getStringArray("Position");
        System.out.println("Getting passed values");


        //views
        btnRoster = (Button) findViewById(R.id.btnRoster);
        btnPositions = (Button) findViewById(R.id.btnPositions);
        tblStats = (TableLayout) findViewById(R.id.tblStats);
        btnBack = (Button) findViewById(R.id.btnBack);
        lblSub = (TextView) findViewById(R.id.lblSub);

        lblSub.setText("Sub "+player[0]+" "+player[1]+" #"+player[2]+" with:");

        updateTable("ROSTER");


        btnRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTable();
                updateTable("ROSTER");
            }
        });

        btnPositions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTable();
                updateTable("POSITIONS");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
            }
        });



    }
    protected void clearTable(){
        pause("Clearing Table");
        for (int i =0; i <count;i++){
            System.out.println(row_id+i+": ID of row");
            TableRow trTmp = (TableRow) findViewById(row_id+i);
            tblStats.removeView(trTmp);
        }
        count =0;
    }

    protected void updateTable(String playerType){
        switch (playerType){
            case ("ROSTER"):
                String[] RHeaders = {"First Name","Last Name","#","Substitute"};
                addTableRow(RHeaders,true);
                PlayerNode tmp = getHead();
                String[] RPlayers = {tmp.data.getFirstName(),tmp.data.getLastName(),tmp.data.getPlayerNumber()};
                if(!(tmp.data.getFirstName().equals(player[0])) && (tmp.isChecked == false)) {
                    addTableRow(RPlayers, false);
                }
                while (tmp.next != null){
                    String[] RPlayers1 = {tmp.next.data.getFirstName(),tmp.next.data.getLastName(),tmp.next.data.getPlayerNumber()};
                    if(!(tmp.next.data.getFirstName().equals(player[0])) && (tmp.next.isChecked == false)) {
                        addTableRow(RPlayers1, false);
                    }
                    tmp=tmp.next;
                }
                break;
            case ("POSITIONS"):
                String[] PHeaders = {"First Name","Last Name","#","Pos","Substitute"};
                addTableRow(PHeaders,true);
                PlayerNode tmp1 = getStarter();
                String[] PPlayers = {tmp1.data.getFirstName(),tmp1.data.getLastName(),tmp1.data.getPlayerNumber(),tmp1.positon};
                if(!(tmp1.data.getFirstName().equals(player[0])) && (tmp1.isChecked == true)) {
                    addTableRow(PPlayers, false);
                }
                while(tmp1.next != null){
                    String[] PPlayers1 = {tmp1.next.data.getFirstName(),tmp1.next.data.getLastName(),tmp1.next.data.getPlayerNumber(),tmp1.next.positon};
                    if(!(tmp1.next.data.getFirstName().equals(player[0])) && (tmp1.next.isChecked == true)) {
                        addTableRow(PPlayers1, false);
                    }
                    tmp1=tmp1.next;
                }
                break;
        }

    }

    protected void printArray(String[] array){
        pause("Printing Array");
        String s = "";
        for(int i = 0; i <array.length;i++){
            s+= array[i];
            s+=" ";
        }
        System.out.println(s);
    }

    protected void addTableRow(String[] stats,boolean isHeader){
        pause("Adding Table Row");
        printArray(stats);
        TableRow trTmp = new TableRow(getApplicationContext());
        trTmp.setPadding(30, 0, 10, 0);
        trTmp.setId(row_id + count);
        for(int i = 0; i < stats.length;i++){
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setText(stats[i]);
            lblTmp.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            lblTmp.setTextSize(20);
            lblTmp.setPadding(50,0,55,0);
            pause("COMPARING STATS.LENGTH TO "+i);

            if (stats.length==i){
                System.out.println("SETTING ID OF: "+lblTmp.getText().toString()+": TO: "+(sub_id+count)+" exception");
                lblTmp.setId(sub_id+count);
            }else {
                System.out.println("SETTING ID OF: "+lblTmp.getText().toString()+": TO: "+(ids[i]+count));
                lblTmp.setId(ids[i] + count);
            }


            trTmp.addView(lblTmp);
        }
        if(!isHeader){
            Button btnTmp = new Button(getApplicationContext());
            btnTmp.setText("Sub");
            btnTmp.setId(sub_id+count);
            System.out.println("SETTING ID OF: "+btnTmp.getText().toString()+": TO: "+(sub_id+count));
            btnTmp.setPadding(20,0,20,0);
            trTmp.addView(btnTmp);
            btnTmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnResult(v);
                }
            });
        }
        tblStats.addView(trTmp);
        count++;
    }

    protected void returnResult(View v){
        int ref = v.getId() - sub_id;
        TextView lbltmp1 = (TextView) findViewById(first_id+ref);
        System.out.println("SETTING LBLTMP1 TO: "+(first_id+ref));
        System.out.println(lbltmp1.getText().toString());
        TextView lbltmp2 = (TextView) findViewById(last_id+ref);
        System.out.println("SETTING LBLTMP2 TO: "+(last_id+ref));
        System.out.println(lbltmp2.getText().toString());
        TextView lbltmp3 = (TextView) findViewById(no_id+ref);
        System.out.println("SETTING LBLTMP3 TO: "+(no_id+ref));
        System.out.println(lbltmp3.getText().toString());
        String[] result = {player[0],player[1],player[2],lbltmp1.getText().toString(),lbltmp2.getText().toString(),lbltmp3.getText().toString()};
        Intent i = new Intent();
        i.putExtra(RESULT_KEY_MESSAGE,result);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    public static String[] getResultKeyMessage(Intent intent){
        return intent.getStringArrayExtra(RESULT_KEY_MESSAGE);
    }



}
