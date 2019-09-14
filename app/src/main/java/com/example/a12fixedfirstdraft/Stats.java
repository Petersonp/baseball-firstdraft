package com.example.a12fixedfirstdraft;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Stats extends GameActivity {
    public static final String BATTING_STATS = "BattingStats";
    public static final String PITCHING_STATS = "PitchingStats";
    DisplayMetrics displayMetrics;

     Button btnPitching;
     Button btnBatting;

     TableLayout tblStats;


     int row_id = 9076;
    //REMOVE past this
     int name_id = 9176;
     int stat_1 = 9276;
     int stat_2 = 9376;
     int stat_3 = 9476;
     int stat_4 = 9576;
     int stat_5 = 9676;
     int stat_6 = 9776;
     int stat_7 = 9876;
     int stat_8 = 9976;
     int count = 0;

     int[] ids = {name_id,stat_1,stat_2,stat_3,stat_4,stat_5,stat_6,stat_7,stat_8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);

        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.95),(int)(height*0.9));

        //setting views
        btnPitching = (Button) findViewById(R.id.btnPitching);
        btnBatting = (Button) findViewById(R.id.btnBatting);
        tblStats = (TableLayout) findViewById(R.id.tblStats);

        updateTable(BATTING_STATS);



        btnPitching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Pitching");
                clearTable();
                updateTable(PITCHING_STATS);
            }
        });


        btnBatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Batting");
                clearTable();
                updateTable(BATTING_STATS);
            }
        });
    }

    protected void clearTable(){
        for (int i =0; i <count;i++){
            TableRow trTmp = (TableRow) findViewById(row_id+i);
            tblStats.removeView(trTmp);
        }
        count =0;
    }

    protected void updateTable(String statsType){
        pause("UPADTING TABLE");
        PlayerNode tmp = getHead();
        System.out.println(tmp.data.pitchingStats.getPIT()+" PIT");
        switch (statsType){
            case (BATTING_STATS):
                String[] BHeaders = {"Batter Name","BA","BBK","OBP","OPS","SF","BB","AB","Hits","HBP","K","TB"};
                addStatRows(BHeaders);
                BattingStats BS = tmp.data.battingStats;
                String[] BStats = {tmp.data.getFirstName()+" "+tmp.data.getLastName(),BS.getBA(), BS.getBBK(), BS.getOBP(), BS.getOPS()/*deleteafterthis*/,BS.getSF(),BS.getBB(),BS.getAB(),BS.getHits(), BS.getHBP(),BS.getK(),BS.getTB()};
                addStatRows(BStats);
                while(tmp.next!= null){
                    BattingStats BS1 = tmp.next.data.battingStats;
                    String[] BStats1 = {tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName(),BS1.getBA(), BS1.getBBK(), BS1.getOBP(), BS1.getOPS()/*deleteafterthis*/,BS1.getSF(),BS1.getBB(),BS1.getAB(),BS1.getHits(),BS1.getHBP(),BS1.getK(),BS1.getTB()};

                    addStatRows(BStats1);
                    tmp = tmp.next;
                }
                break;
            case (PITCHING_STATS):
                String[] PHeaders = {"Pitcher Name","BABIP","CP","GF","KBB","SP","BH","BHR","BAB","BK"/*,"BSF"*/,"GB","PIT"/*,"FB"*/,"BB","S","C"};
                addStatRows(PHeaders);
                PitchingStats PS = tmp.data.pitchingStats;
                String[] PStats = {tmp.data.getFirstName()+" "+tmp.data.getLastName(),PS.getBABIP(),PS.getCP(),PS.getGF(),PS.getKBB(),PS.getSP()/*deleteafterthis*/,PS.getBH(),PS.getBHR(),PS.getBAB(),PS.getBK()/*,PS.getBSF()*/,PS.getGB(),PS.getPIT()/*,PS.getFB()*/,PS.getBB(),PS.getS(),PS.getC()};
                addStatRows(PStats);
                while(tmp.next!= null){
                    PitchingStats PS1 = tmp.next.data.pitchingStats;
                    String[] PStats1 = {tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName(),PS1.getBABIP(), PS1.getCP(), PS1.getGF(), PS1.getKBB(), PS1.getSP()/*deleteafterthis*/,PS1.getBH(),PS1.getBHR(),PS1.getBAB(),PS1.getBK()/*,PS1.getBSF()*/,PS1.getGB(),PS1.getPIT()/*,PS1.getFB()*/,PS1.getBB(),PS1.getS(),PS1.getC()};
                    addStatRows(PStats1);
                    tmp = tmp.next;
                }
                break;
        }
    }



    protected void addStatRows(String[] stats) {
        TableRow trTmp = new TableRow(getApplicationContext());
        trTmp.setPadding(10, 0, 0, 0);
        trTmp.setId(row_id + count);
        for(int i = 0; i < stats.length;i++){
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setText(stats[i]);
            lblTmp.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            lblTmp.setTextSize(20);
            lblTmp.setPadding(40,0,55,0);

            trTmp.addView(lblTmp);
        }
        tblStats.addView(trTmp);
        count++;
    }



}
