package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_GETMESSAGE_STARTINGWINDOW = 5;
    public static final int REQUEST_CODE_GETMESSAGE_PITCH = 6;
    Button btnStart;
    static private PlayerNode head;
    static private PlayerNode starter;
    static private PlayerNode otherHead;

    int strikes;
    int balls;
    int inning;
    int outs;

    private PlayerNode pitcher;
    private PlayerNode catcher;
    private PlayerNode firstBase;
    private PlayerNode secondBase;
    private PlayerNode thirdBase;
    private PlayerNode shortStop;
    private PlayerNode leftField;
    private PlayerNode centerField;
    private PlayerNode rightField;

    private PlayerNode hole;

    private BatterNode onDeck = new BatterNode();
    private BatterNode batter = new BatterNode();
    private BatterNode fbRunner = new BatterNode();
    private BatterNode sbRunner = new BatterNode();
    private BatterNode tbRunner = new BatterNode();
    private BatterNode hbRunner = new BatterNode();

    Button btnPitcher;
    Button btnCatcher;
    Button btnFirstBase;
    Button btnSecondBase;
    Button btnThirdBase;
    Button btnShortStop;
    Button btnLeftField;
    Button btnCenterField;
    Button btnRightField;
    Button btnPitch;

    TextView lblGameInfo;
    TextView lblAtBat;
    TextView lblOnDeck;

    PlayerNode[] positionNodes = {pitcher,catcher,firstBase,secondBase,thirdBase,shortStop,leftField,centerField,rightField};
    String[] positions = {"P","C","FB","SB","TB","SS","LF","CF","RF"};

    //Button[] positionButtons = {btnPitcher,btnCatcher,btnFirstBase,btnSecondBase,btnThirdBase,btnShortStop,btnLeftField,btnCenterField,btnRightField};
    Button[] positionButtons = new Button[9];

    public boolean isHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameactivity_layout);

        //Other team tmp
        for (int i = 0; i <9;i++){
            PlayerNode newPlayer = new PlayerNode();
            Player player = new Player();
            player.setLastName("ofn"+String.valueOf(i+1));
            player.setLastName("oln"+String.valueOf(i+1));
            player.setPlayerNumber(String.valueOf(i+1));
            newPlayer.data = player;
            newPlayer.order = i+1;
            newPlayer.positon = positions[i];
            if (otherHead == null){
                otherHead = newPlayer;
            }else{
                PlayerNode tmp = otherHead;
                while (tmp.next != null){
                    tmp = tmp.next;
                }
                tmp.next = newPlayer;
            }
        }

        //BatterNodes
        onDeck.next = batter;
        batter.next = fbRunner;
        batter.order = 0;
        fbRunner.next = sbRunner;
        fbRunner.order = 1;
        sbRunner.next = tbRunner;
        sbRunner.order = 2;
        tbRunner.next = hbRunner;
        tbRunner.order = 3;


        //views
        Button btnPitcher = (Button) findViewById(R.id.btnPitcher);
        positionButtons[0] = btnPitcher;
        Button btnCatcher = (Button) findViewById(R.id.btnCatcher);
        positionButtons[1] = btnCatcher;
        Button btnFirstBase = (Button) findViewById(R.id.btnFirstBase);
        positionButtons[2] = btnFirstBase;
        Button btnSecondBase = (Button) findViewById(R.id.btnSecondBase);
        positionButtons[3] = btnSecondBase;
        Button btnThirdBase = (Button) findViewById(R.id.btnThirdBase);
        positionButtons[4] = btnThirdBase;
        Button btnShortStop = (Button) findViewById(R.id.btnShortStop);
        positionButtons[5] = btnShortStop;
        Button btnLeftField = (Button) findViewById(R.id.btnLeftField);
        positionButtons[6] = btnLeftField;
        Button btnCenterField = (Button) findViewById(R.id.btnCenterField);
        positionButtons[7] = btnCenterField;
        Button btnRightField = (Button) findViewById(R.id.btnRightField);
        positionButtons[8] = btnRightField;
        Button btnPitch = (Button) findViewById(R.id.btnPitch);

        lblGameInfo = (TextView) findViewById(R.id.lblGameInfo);
        lblAtBat = (TextView) findViewById(R.id.lblAtBat);
        lblOnDeck = (TextView) findViewById(R.id.lblOnDeck);




        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),StartingWindow.class);
                startActivityForResult(i, REQUEST_CODE_GETMESSAGE_STARTINGWINDOW);
            }
        });

        btnPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printLineUp();
                Intent i = new Intent(getApplicationContext(),Pitch.class);
                startActivityForResult(i,REQUEST_CODE_GETMESSAGE_PITCH);
            }
        });


        //loadPlayers();
    }

    protected void updateGameInfo(){
        lblGameInfo.setText("I:"+getInning(inning)+" B:"+balls+" S:"+strikes+" O:"+outs);
    }

    private String getInning(int inning){
        String s = "";
        if (inning%2 == 1){
            s+= "^";
        }else{
            s+= "v";
        }
        s+= String.valueOf(Math.round(inning/2));
        return s;
    }

    protected void loadPositions(){
        PlayerNode tmp = getStarter();
        System.out.println(tmp.data.getFirstName()+" "+tmp.positon+ " IN LOADPOSITIONS");
        for (int i = 0; i < positions.length;i++){
            System.out.println("CHECKING IF: "+tmp.positon+" IS EQUAL TO: "+positions[i]);
            if (tmp.positon.equals(positions[i])){
                System.out.println(tmp.positon+" IS EQUAL TO: "+positions[i]);
                positionNodes[i] = tmp;
                System.out.println(positionNodes[i].data.getFirstName()+ " IS THE FIRST NODE IN THE POSITIONS");
                break;
            }
        }
        while (tmp.next != null){
            System.out.println("MOVING ON");
            for (int i = 0; i < positions.length;i++){
                System.out.println("CHECKING IF: "+tmp.next.positon+" IS EQUAL TO: "+positions[i]);
                if (tmp.next.positon.equals(positions[i])){
                    System.out.println(tmp.next.positon+" IS EQUAL TO: "+positions[i]);
                    positionNodes[i] = tmp.next;
                    System.out.println(positionNodes[i].data.getFirstName()+ " IS THE FIRST NODE IN THE POSITIONS");
                }
            }
            tmp = tmp.next;
        }
    }

    protected void printPositionNodes(){
        for (int i = 0; i<positionNodes.length;i++){
            if(positionNodes[i]==null){
                System.out.println("POSITION NODE AT: "+i+" IS NULL");
            }else{
                System.out.println(positionNodes[i].data.getFirstName()+" "+positionNodes[i].data.getLastName()+" "+positionNodes[i].data.getPlayerNumber());
            }
        }
    }

    protected void printLineUp(){
        PlayerNode tmp = getStarter();
        System.out.println(tmp.order+": "+tmp.data.getFirstName()+" "+tmp.data.getLastName()+" #"+tmp.data.getPlayerNumber()+" @"+tmp.positon+" ----");
        while (tmp.next != null){
            System.out.println(tmp.next.order+": "+tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName()+" #"+tmp.next.data.getPlayerNumber()+" @"+tmp.next.positon);
            tmp = tmp.next;
        }
    }

    protected void loadPlayers(){
        for (int i = 0; i < positionButtons.length;i++){
            System.out.println(positionButtons[1].getText().toString());
            positionButtons[i].setText(positionNodes[i].data.getFirstName().toString()+" "+positionNodes[i].data.getLastName().toString());
        }
    }

    protected PlayerNode getHead(){
        return head;
    }

    protected void setHead(PlayerNode head){
        this.head = head;
    }

    protected PlayerNode getStarter() { return starter; }

    protected void setStarter(PlayerNode starter) { this.starter = starter; }

    protected void moveBatter(int n){
        BatterNode tmp = batter;
        for (int i = 0; i <n;i++){
            tmp.next.data = tmp.data;
            tmp.data = null;
            tmp = tmp.next;
        }

    }

    protected void checkGameInfo(){
        if (balls == 4){
            balls = 0;
            batter.data.data.battingStats.addBB();
            shiftBatters();
        }
        if (strikes == 3){
            strikes = 0;
            pitcher.data.pitchingStats.addBK();
            batter.data.data.battingStats.addK();
            outs++;
            shiftBatters();
        }
        if (outs == 3){
            inning++;
            outs = 0;
        }

    }

    protected void shiftBatters(){
        batter.data = onDeck.data;
        if (onDeck.data.next.designatedHitter == null) {
            onDeck.data = onDeck.data.next;
        }else{
            onDeck.data = onDeck.data.next.designatedHitter;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode+" IS THE REQUEST CODE");
        if (requestCode == REQUEST_CODE_GETMESSAGE_PITCH){
            System.out.println(REQUEST_CODE_GETMESSAGE_PITCH+" IS EQUAL TO "+requestCode);
            System.out.println("RUNNING REQUESTCODEGETMESSAGEPITCH");
            if (resultCode == Activity.RESULT_OK){
                String[] result = Pitch.getResultKeyMessage(data);
                System.out.println(result[0]+" IS RESULT[0]");
                System.out.println(result[1]+" IS RESULT[1]");
                pitcher.data.pitchingStats.addPIT();
                switch (result[0]){
                    case ("Ground Ball"):
                        System.out.println("GROUND BALL IS THE RESULT");
                        pitcher.data.pitchingStats.addC();
                    case ("Line Drive"):
                        System.out.println("LINE DRIVE IS THE RESULT");
                        pitcher.data.pitchingStats.addC();
                    case ("Pop Fly"):
                        System.out.println("POP FLY IS THE RESULT");
                        pitcher.data.pitchingStats.addFB();
                        pitcher.data.pitchingStats.addC();
                    case ("Bunt"):
                        System.out.println("BUNT IS THE RESULT");
                        pitcher.data.pitchingStats.addC();
                    case ("Foul Ball"):
                        System.out.println("FOUL BALL IS THE RESULT");
                        pitcher.data.pitchingStats.addC();
                }
                switch (result[1]){
                    case ("Out"):
                        System.out.println("OUT IS THE RESULT");
                        outs++;
                    case ("Single"):
                        System.out.println("SINGLE IS THE RESULT");
                        strikes = 0;
                        balls = 0;
                        batter.data.data.battingStats.addHits();
                    case ("Double"):
                        System.out.println("DOUBLE IS THE RESULT");
                        strikes = 0;
                        balls = 0;
                        batter.data.data.battingStats.addHits();
                    case ("Triple"):
                        System.out.println("TRIPLE IS THE RESULT");
                        strikes = 0;
                        balls = 0;
                        batter.data.data.battingStats.addHits();
                    case ("In-The-Park Home Run"):
                        System.out.println("IN THE PARK HOME RUN IS THE RESULT");
                        pitcher.data.pitchingStats.addBHR();
                        strikes = 0;
                        balls = 0;
                        batter.data.data.battingStats.addHits();
                    case ("Strike"):
                        System.out.println("STRIKE IS THE RESULT");
                        pitcher.data.pitchingStats.addS();
                        strikes++;
                    case ("Ball"):
                        System.out.println("BALL IS THE RESULT");
                        balls++;

                }
                checkGameInfo();
                updateGameInfo();


            }
        }else if (requestCode == REQUEST_CODE_GETMESSAGE_STARTINGWINDOW){
            if (resultCode == Activity.RESULT_OK) {
                String result = LineUp.getResultKeyMessage(data);
                System.out.println(result + " IS THE RESULT");
                if (result != null) {
                    switch (result) {
                        case ("Home"):
                            loadPositions();
                            printPositionNodes();
                            loadPlayers();
                        case ("Away"):
                            if (getStarter().designatedHitter == null) {
                                batter.data = getStarter();
                            } else {
                                batter.data = getStarter().designatedHitter;
                            }
                            if (getStarter().next.designatedHitter == null) {
                                onDeck.data = getStarter().next;
                            } else {
                                onDeck.data = getStarter().next.designatedHitter;
                            }
                    }

                }else{
                    System.out.println("DOING ELSE");
                    if (isHome){
                        loadPositions();
                        printPositionNodes();
                        loadPlayers();
                    }else{
                        if (getStarter().designatedHitter == null) {
                            batter.data = getStarter();
                        } else {
                            batter.data = getStarter().designatedHitter;
                        }
                        if (getStarter().next.designatedHitter == null) {
                            onDeck.data = getStarter().next;
                        } else {
                            onDeck.data = getStarter().next.designatedHitter;
                        }
                    }
                }
            }
        }

    }


}
