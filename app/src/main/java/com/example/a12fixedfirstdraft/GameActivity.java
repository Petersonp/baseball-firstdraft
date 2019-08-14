package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_GETMESSAGE_STARTINGWINDOW = 5;
    public static final int REQUEST_CODE_GETMESSAGE_PITCH = 6;
    public static final int REQUEST_CODE_SUBPLAYER = 19;
    Button btnStart;
    static private PlayerNode head;
    static private PlayerNode starter;
    static private PlayerNode otherHead;

    int row_id = 9076;
    int first_id = 9176;
    int last_id = 9276;
    int order_id = 9376;

    //tmp
    Button btnPrintBatters;

    int strikes;
    int balls;
    int inning;
    int outs;
    private int count = 0;

    GameNode front;
    GameNode base;

    private PlayerNode pitcher;
    private PlayerNode catcher;
    private PlayerNode firstBase;
    private PlayerNode secondBase;
    private PlayerNode thirdBase;
    private PlayerNode shortStop;
    private PlayerNode leftField;
    private PlayerNode centerField;
    private PlayerNode rightField;

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
    Button btnStats;

    Button btnFirstRunner;
    Button btnSecondRunner;
    Button btnThirdRunner;
    Button btnBatter;

    Button btnUndo;

    TextView lblGameInfo;
    TextView lblAtBat;
    TextView lblOnDeck;

    PlayerNode[] positionNodes = {pitcher,catcher,firstBase,secondBase,thirdBase,shortStop,leftField,centerField,rightField};
    String[] positions = {"P","C","FB","SB","TB","SS","LF","CF","RF"};

    //Button[] positionButtons;
    Button[] positionButtons = new Button[9];

    static private boolean isHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameactivity_layout);

        //Other team tmp
        for (int i = 0; i <9;i++){
            PlayerNode newPlayer = new PlayerNode();
            Player player = new Player();
            player.setFirstName("ofn"+String.valueOf(i+1));
            player.setLastName("oln"+String.valueOf(i+1));
            player.setPlayerNumber(String.valueOf(i+1));
            newPlayer.data = player;
            newPlayer.order = i+1;
            newPlayer.positon = positions[i];
            if (getOtherHead() == null){
                setOtherHead(newPlayer);
            }else{
                PlayerNode tmp = otherHead;
                while (tmp.next != null){
                    tmp = tmp.next;
                }
                tmp.next = newPlayer;
            }
        }

        btnPrintBatters = (Button) findViewById(R.id.btnPrintBatters);
        btnPrintBatters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //printBatters();
                totString();
            }
        });
        //views
        btnPitcher = (Button) findViewById(R.id.btnPitcher);positionButtons[0] = btnPitcher;
        btnCatcher = (Button) findViewById(R.id.btnCatcher);positionButtons[1] = btnCatcher;
        btnFirstBase = (Button) findViewById(R.id.btnFirstBase);positionButtons[2] = btnFirstBase;
        btnSecondBase = (Button) findViewById(R.id.btnSecondBase);positionButtons[3] = btnSecondBase;
        btnThirdBase = (Button) findViewById(R.id.btnThirdBase);positionButtons[4] = btnThirdBase;
        btnShortStop = (Button) findViewById(R.id.btnShortStop);positionButtons[5] = btnShortStop;
        btnLeftField = (Button) findViewById(R.id.btnLeftField);positionButtons[6] = btnLeftField;
        btnCenterField = (Button) findViewById(R.id.btnCenterField);positionButtons[7] = btnCenterField;
        btnRightField = (Button) findViewById(R.id.btnRightField);positionButtons[8] = btnRightField;
        btnPitch = (Button) findViewById(R.id.btnPitch);

        lblGameInfo = (TextView) findViewById(R.id.lblGameInfo);
        lblAtBat = (TextView) findViewById(R.id.lblAtBat);
        lblOnDeck = (TextView) findViewById(R.id.lblOnDeck);

        btnFirstRunner = (Button) findViewById(R.id.btnFirstRunner);
        btnSecondRunner = (Button) findViewById(R.id.btnSecondRunner);
        btnThirdRunner = (Button) findViewById(R.id.btnThirdRunner);
        btnBatter = (Button) findViewById(R.id.btnBatter); btnBatter.setBackgroundColor(Color.GREEN);

        btnUndo = (Button) findViewById(R.id.btnUndo);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStats = (Button) findViewById(R.id.btnStats);



        //BatterNodes
        onDeck.next = batter;
        batter.next = fbRunner;
        batter.order = 0;
        batter.btn = btnBatter;
        fbRunner.next = sbRunner;
        fbRunner.order = 1;
        fbRunner.btn = btnFirstRunner;
        sbRunner.next = tbRunner;
        sbRunner.order = 2;
        sbRunner.btn = btnSecondRunner;
        tbRunner.next = hbRunner;
        tbRunner.order = 3;
        tbRunner.btn = btnThirdRunner;

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()) {
                    pause("UNDO");
                    setGame(pop());
                }
            }
        });


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
                //printLineUp();
                Intent i = new Intent(getApplicationContext(),Pitch.class);
                startActivityForResult(i,REQUEST_CODE_GETMESSAGE_PITCH);
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Stats.class);
                startActivity(i);
            }
        });

        btnFirstBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("BTNFIRSTBASE PRESSED");
                if (fbRunner.data != null && fbRunner.next.data == null){
                    fbRunner.next.data = fbRunner.data;
                    fbRunner.next.btn.setText(fbRunner.data.data.getFirstName()+" "+fbRunner.data.data.getLastName());
                    fbRunner.btn.setText("--");
                    fbRunner.data = null;
                    push();
                }
            }
        });
        btnSecondRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sbRunner.data != null && sbRunner.next.data == null){
                    sbRunner.next.data = sbRunner.data;
                    sbRunner.next.btn.setText(sbRunner.data.data.getFirstName()+" "+sbRunner.data.data.getLastName());
                    sbRunner.btn.setText("--");
                    sbRunner.data = null;
                    push();

                }
            }
        });
        btnThirdRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tbRunner.data != null){
                    tbRunner.data = null;
                    tbRunner.btn.setText("--");
                    //add points
                    push();
                }

            }
        });


        //loadPlayers();

        //Substitute players
        btnPitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(pitcher);
            }
        });

        btnCatcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(catcher);
            }
        });

        btnFirstBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(firstBase);
            }
        });

        btnSecondBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(secondBase);
            }
        });

        btnThirdBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(thirdBase);
            }
        });

        btnShortStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(shortStop);
            }
        });

        btnRightField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(rightField);
            }
        });

        btnCenterField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(centerField);
            }
        });

        btnLeftField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(leftField);
            }
        });
    }

    protected void substituteActivity(PlayerNode player){
        pause("SUBSTITUTING ACTIVITY");
        System.out.println(player.data.getFirstName()+" "+player.data.getLastName()+" @"+player.positon);
        Intent i = new Intent(getApplicationContext(),BallInPlay2.class);
        Bundle bundle = new Bundle();
        String[] stuff = {player.positon,player.data.getFirstName(),player.data.getLastName()};
        bundle.putStringArray("Position",stuff);
        i.putExtras(bundle);
        startActivityForResult(i, REQUEST_CODE_SUBPLAYER);

    }



    protected void updateGameInfo(){
        lblGameInfo.setText("I:"+getInning(inning)+" B:"+balls+" S:"+strikes+" O:"+outs);
        lblAtBat.setText("At Bat: "+batter.data.data.getFirstName()+" "+batter.data.data.getLastName());
        lblOnDeck.setText("On Dec: "+onDeck.data.data.getFirstName()+" "+onDeck.data.data.getLastName());
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

    protected void loadFieldingPositionNodes(PlayerNode tmpHead){
        pause("LOADING FIELDING POSITION NODES");
        PlayerNode tmp = tmpHead;
        for (int i = 0; i < positions.length;i++){
            if (tmp.positon.equals(positions[i])){
                positionNodes[i] = tmp;
                break;
            }
        }
        while (tmp.next != null){
            for (int i = 0; i < positions.length;i++){
                if (tmp.next.positon.equals(positions[i])){
                    positionNodes[i] = tmp.next;
                }
            }
            tmp = tmp.next;
        }
        updateNodes(positionNodes);
    }

    //Linked List
    protected Game newGame(){
        pause("NEW GAME");
        Game g = new Game();
        g.setStrikes(strikes);
        System.out.println("STRIKES: "+ g.getStrikes());
        g.setBalls(balls);
        System.out.println("BALLS: "+ g.getBalls());
        g.setOuts(outs);
        System.out.println("OUTS: "+ g.getOuts());
        g.setInning(inning);
        System.out.println("INNINGS: "+ g.getInning());

        g.setRoster(getHead());
        g.setLineup(getStarter());
        g.setOtherLineup(getOtherHead());

        g.setBatter(batter.data);
        g.setOnDeck(onDeck.data);
        g.setFbRunner(fbRunner.data);
        g.setSbRunner(sbRunner.data);
        g.setTbRunner(tbRunner.data);
        /*
        if(g.getFbRunner().data == null){
            System.out.println("FBRUNNER IS NULL");
        }
        if (g.getFbRunner().data != null){
            System.out.println("FBRUNNER: "+ g.getFbRunner().data.getFirstName()+" "+fbRunner.data.data.getLastName());
        }else{
            g.getFbRunner().data = null;
            System.out.println("FBRUNNER IS EMPTY");
        }
        g.setSbRunner(sbRunner.data);
        if (g.getSbRunner().data != null){
            System.out.println("SBRUNNER: "+ g.getSbRunner().data.getFirstName()+" "+sbRunner.data.data.getLastName());
        }else{
            g.getSbRunner().data = null;
            System.out.println("SBRUNNER IS EMPTY");
        }
        g.setTbRunner(tbRunner.data);
        if (g.getTbRunner().data != null){
            System.out.println("TBRUNNER: "+ g.getTbRunner().data.getFirstName()+" "+tbRunner.data.data.getLastName());
        }else{
            g.getTbRunner().data = null;
            System.out.println("TBRUNNER IS EMPTY");
        }
        */
        g.setPitcher(pitcher);
        g.setCatcher(catcher);
        g.setFirstBase(firstBase);
        g.setSecondBase(secondBase);
        g.setThirdBase(thirdBase);
        g.setShortStop(shortStop);
        g.setLeftField(leftField);
        g.setCenterField(centerField);
        g.setRightField(rightField);

        return g;
    }

    protected void setGame(Game g){
        System.out.println("OLD STRIKES: "+strikes);
        this.strikes = g.getStrikes();
        System.out.println("NEW STRIKES: "+strikes);
        System.out.println("OLD BALLS: "+balls);
        balls = g.getBalls();
        System.out.println("NEW BALLS: "+strikes);
        System.out.println("OLD OUTS: "+balls);
        outs = g.getOuts();
        System.out.println("NEW OUTS: "+strikes);
        System.out.println("OLD INNING: "+inning);
        inning = g.getInning();
        System.out.println("NEW INNING: "+strikes);

        setHead(g.getRoster());
        setStarter(g.getLineup());
        setOtherHead(g.getOtherLineup());

        batter.data = g.getBatter();
        onDeck.data = g.getOnDeck();
        System.out.println("BATTER: "+batter.data.data.getFirstName()+" "+batter.data.data.getLastName());
        fbRunner.data = g.getFbRunner();
        if (fbRunner.data != null) {
            System.out.println("FBRUNNER: " + fbRunner.data.data.getFirstName() + " " + fbRunner.data.data.getLastName());
        }else{
            System.out.println("FBRUNNER NULL");
        }
        sbRunner.data = g.getSbRunner();
        if (sbRunner.data != null) {
            System.out.println("SBRUNNER: " + sbRunner.data.data.getFirstName() + " " + sbRunner.data.data.getLastName());
        }else{
            System.out.println("SBRUNNER NULL");
        }
        tbRunner.data = g.getTbRunner();
        if (tbRunner.data != null) {
            System.out.println("TBRUNNER: " + tbRunner.data.data.getFirstName() + " " + tbRunner.data.data.getLastName());
        }else {
            System.out.println("TBRUNNER NULL");
        }

        pitcher = g.getPitcher();
        catcher = g.getCatcher();
        firstBase = g.getFirstBase();
        secondBase = g.getSecondBase();
        thirdBase = g.getThirdBase();
        shortStop = g.getShortStop();
        leftField = g.getLeftField();
        centerField = g.getCenterField();
        rightField = g.getRightField();

        loadFieldingPositionNodes(getStarter());
        loadFieldingButtons();
        updateBatter();
        //loadBattingPositionNodes(getOtherHead());
        updateGameInfo();
        updateRunners();


    }

    protected void push(){
        pause("Pushing");
        GameNode newNode = new GameNode();
        newNode.data = newGame();
        if(front == null){
            front = newNode;
            base = newNode;
        }
        else{
            if(isEmpty()){
                base.next = newNode;
                newNode.back = base;
            }
            newNode.back = front;
            front.next = newNode;
            front = newNode;
        }
    }

    protected Game pop(){
        pause("Popping");
        if (!isEmpty()){
            Game g = front.data;
            front = front.back;
            if (isEmpty()){
                base.next= null;
            }
            return g;
        }else{
            Game g = front.data;
            return g;

        }
    }

    protected boolean isEmpty(){
        return (base.next == null);
    }

    protected void totString(){
        int nodeCount =1;
        GameNode tmp = front;
        System.out.println("FRONT");
        while (tmp != null){
            pause("NODE --------"+nodeCount);
            System.out.println("STRIKES: "+ tmp.data.getStrikes());
            System.out.println("BALLS: "+ tmp.data.getBalls());
            System.out.println("OUTS: "+ tmp.data.getOuts());
            System.out.println("INNING: "+ tmp.data.getInning());
            if (tmp.data.getBatter() != null) {
                System.out.println("BATTER: " + tmp.data.getBatter().data.getFirstName() + " " + tmp.data.getBatter().data.getLastName());
            }else{
                System.out.println("BATTER IS NULL");
            }
            if (tmp.data.getOnDeck() != null){
                System.out.println("ON DECK: "+tmp.data.getOnDeck().data.getFirstName() + " " + tmp.data.getOnDeck().data.getLastName());
            }else{
                System.out.println("ON DECK IS NULL");
            }
            if (tmp.data.getFbRunner() != null) {
                System.out.println("FBRUNNER: " + tmp.data.getFbRunner().data.getFirstName() + " " + tmp.data.getFbRunner().data.getLastName());
            }else{
                System.out.println("FBRUNNER IS NULL");
            }
            if (tmp.data.getSbRunner() != null) {
                System.out.println("SBRUNNER: " + tmp.data.getSbRunner().data.getFirstName() + " " + tmp.data.getSbRunner().data.getLastName());
            }else{
                System.out.println("SBRUNNER IS NULL");
            }
            if (tmp.data.getTbRunner() != null) {
                System.out.println("TBRUNNER: " + tmp.data.getTbRunner().data.getFirstName() + " " + tmp.data.getTbRunner().data.getLastName());
            }else{
                System.out.println("TBRUNNER IS NULL");
            }
            tmp = tmp.back;
            nodeCount++;
        }
    }
    // Linked List

    protected void printPositionNodes(){
        for (int j = 0; j < 10;j++){
            System.out.println(" ");
        }
        for (int i = 0; i<positionNodes.length;i++){
            if(positionNodes[i]==null){
            }else{
                System.out.println(positionNodes[i].data.getFirstName()+" "+positionNodes[i].data.getLastName()+" "+positionNodes[i].data.getPlayerNumber());
            }
        }
        for (int j = 0; j < 10;j++){
            System.out.println(" ");
        }
    }

    protected void printLineUp(){
        PlayerNode tmp = getStarter();
        System.out.println(tmp.order+": "+tmp.data.getFirstName()+" "+tmp.data.getLastName()+" #"+tmp.data.getPlayerNumber()+" @"+tmp.positon+" ----");
        for (int i =0;i<8;i++){
            System.out.println(tmp.next.order+": "+tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName()+" #"+tmp.next.data.getPlayerNumber()+" @"+tmp.next.positon);
            tmp = tmp.next;
        }
    }
    // --------------------------------
    protected void loadFieldingButtons(){
        for (int i = 0; i < positionButtons.length;i++){
            positionButtons[i].setText(positionNodes[i].data.getFirstName().toString()+" "+positionNodes[i].data.getLastName().toString());
        }
    }

    protected PlayerNode getOtherHead(){
        return otherHead;
    }

    protected void setOtherHead(PlayerNode otherHead){
        this.otherHead = otherHead;
    }

    protected PlayerNode getHead(){
        return head;
    }

    protected void setHead(PlayerNode head){
        this.head = head;
    }

    protected PlayerNode getStarter() { return starter; }

    protected void setStarter(PlayerNode starter) { this.starter = starter; }

    protected void printBatters(){
        pause("Printing Batters");
        BatterNode tmp = batter;
        System.out.println(tmp.data.data.getFirstName()+" "+tmp.data.data.getLastName()+" IS THE BATTER");
        tmp = tmp.next;
        if (tmp.data != null) {
            System.out.println(tmp.data.data.getFirstName() + " " + tmp.data.data.getLastName() + " IS THE FIRST BASE RUNNER");
        }else{
            System.out.println("FIRST BASE RUNNER IS EMPTY");
        }
        tmp = tmp.next;
        if (tmp.data!= null) {
            System.out.println(tmp.data.data.getFirstName() + " " + tmp.data.data.getLastName() + " IS THE SECOND BASE RUNNER");
        }else{
            System.out.println("SECOND BASE RUNNER IS EMPTY");
        }
        tmp = tmp.next;
        if (tmp.data != null){
            System.out.println(tmp.data.data.getFirstName()+" "+tmp.data.data.getLastName()+" IS THE THIRD BASE RUNNER");
        }else{
            System.out.println("THIRD BASE RUNNER IS EMPTY");
        }
    }

    protected void moveBatter(int n){
        printBatters();
        for (int i =0; i< n;i++) {
            pause("Moving Batters");
            if (tbRunner.data != null) {
                //add points
                tbRunner.data = null;
                tbRunner.btn.setText("--");
            }
            if (sbRunner.data != null) {
                //System.out.println(tbRunner.data.data.getFirstName() + " Third Base Before");
                tbRunner.data = sbRunner.data;
                System.out.println(tbRunner.data.data.getFirstName() + " Third Base After");
                tbRunner.btn.setText(tbRunner.data.data.getFirstName() + " " + tbRunner.data.data.getLastName());
                tbRunner.btn.setBackgroundColor(Color.GREEN);
                sbRunner.data = null;
                sbRunner.btn.setText("--");
            } else {
                System.out.println("Third Base else");
                tbRunner.data = null;
                tbRunner.btn.setText("--");

            }
            if (fbRunner.data != null) {
                //System.out.println(sbRunner.data.data.getFirstName() + " Second Base Before");
                sbRunner.data = fbRunner.data;
                System.out.println(sbRunner.data.data.getFirstName() + " Second Base After");
                sbRunner.btn.setText(sbRunner.data.data.getFirstName() + " " + sbRunner.data.data.getLastName());
                sbRunner.btn.setBackgroundColor(Color.GREEN);
                fbRunner.data = null;
                fbRunner.btn.setText("--");
            } else {
                System.out.println("Second Base else");
                sbRunner.data = null;
                sbRunner.btn.setText("--");
            }
            if (i<1) {
                fbRunner.data = batter.data;
                fbRunner.btn.setText(fbRunner.data.data.getFirstName() + " " + fbRunner.data.data.getLastName());
                fbRunner.btn.setBackgroundColor(Color.GREEN);
            }

            printBatters();
        }
        shiftBatters();

    }

    protected void updateBatter(){
        pause("Updating Batters");
        if (batter.data != null){
            batter.btn.setText(batter.data.data.getFirstName() + " " + batter.data.data.getLastName());
        }
    }

    protected void updateRunners(){
        pause("Updating Runners");
        if (tbRunner.data!= null){
            tbRunner.btn.setText(tbRunner.data.data.getFirstName() + " " + tbRunner.data.data.getLastName());
            tbRunner.btn.setBackgroundColor(Color.GREEN);
        }else{
            tbRunner.data = null;
            tbRunner.btn.setText("--");
        }
        if (sbRunner.data!= null){
            sbRunner.btn.setText(sbRunner.data.data.getFirstName() + " " + sbRunner.data.data.getLastName());
            sbRunner.btn.setBackgroundColor(Color.GREEN);
        }else{
            sbRunner.data = null;
            sbRunner.btn.setText("--");
        }
        if (fbRunner.data!= null){
            fbRunner.btn.setText(fbRunner.data.data.getFirstName() + " " + fbRunner.data.data.getLastName());
            fbRunner.btn.setBackgroundColor(Color.GREEN);
        }else{
            fbRunner.data = null;
            fbRunner.btn.setText("--");
        }
        btnBatter.setText(batter.data.data.getFirstName() + " " + batter.data.data.getLastName());
    }


    protected void checkGameInfo(){
        if (balls == 4){
            balls = 0;
            strikes = 0;
            batter.data.data.battingStats.addBB();
            pitcher.data.pitchingStats.addBB();
            moveBatter(1);
        }
        if (strikes == 3){
            strikes = 0;
            balls = 0;
            pitcher.data.pitchingStats.addBK();
            batter.data.data.battingStats.addK();
            outs++;
            shiftBatters();
        }
        if (outs == 3){
            inning++;
            outs = 0;
            strikes = 0;
            balls = 0;
            shiftBatters();
        }

    }
    //formating output
    public void pause(String s){
        for (int i =0; i< 5;i++){
            System.out.println(" ");
        }
        System.out.println("@"+s+"<-------------------->");
    }
    protected void shiftBatters(){
        pause("Shift Batters");
        System.out.println("Shifting batter data to onDeck");
        batter.data = onDeck.data;
        batter.data.data.battingStats.addAB();
        pitcher.data.pitchingStats.addBAB();
        System.out.println("ADDING AN AT BAT TO: "+batter.data.data.getFirstName());
        batter.btn.setText(batter.data.data.getFirstName()+" "+batter.data.data.getLastName());
        if (onDeck.data.next == null){
            onDeck.data.next = getStarter();
        }
        if (onDeck.data.next.designatedHitter == null) {
            onDeck.data = onDeck.data.next;
        }else{
            onDeck.data = onDeck.data.next.designatedHitter;
        }

    }

    protected void updateNodes(PlayerNode[] tmppositionNodes){
        pitcher = tmppositionNodes[0];
        //System.out.println(pitcher.data.getFirstName()+" "+pitcher.data.getLastName()+" pitcher");
        catcher = tmppositionNodes[1];
        //System.out.println(catcher.data.getFirstName()+" "+catcher.data.getLastName()+" catcher");
        firstBase = tmppositionNodes[2];
        //System.out.println(firstBase.data.getFirstName()+" "+firstBase.data.getLastName()+" firstBase");
        secondBase = tmppositionNodes[3];
        //System.out.println(secondBase.data.getFirstName()+" "+secondBase.data.getLastName()+" secondBase");
        thirdBase = tmppositionNodes[4];
        //System.out.println(thirdBase.data.getFirstName()+" "+thirdBase.data.getLastName()+" thirdBase");
        shortStop = tmppositionNodes[5];
        //System.out.println(shortStop.data.getFirstName()+" "+shortStop.data.getLastName()+" shortStop");
        leftField = tmppositionNodes[6];
        //System.out.println(leftField.data.getFirstName()+" "+leftField.data.getLastName()+" leftField");
        centerField = tmppositionNodes[7];
        //System.out.println(centerField.data.getFirstName()+" "+centerField.data.getLastName()+" centerField");
        rightField = tmppositionNodes[8];
        //System.out.println(rightField.data.getFirstName()+" "+rightField.data.getLastName()+" rightField");
    }

    protected void createPositionNodes(){
        PlayerNode[] tmpPosNodes = new PlayerNode[9];
        tmpPosNodes[0] = pitcher;
        tmpPosNodes[1] = catcher;
        tmpPosNodes[2] = firstBase;
        tmpPosNodes[3] = secondBase;
        tmpPosNodes[4] = thirdBase;
        tmpPosNodes[5] = shortStop;
        tmpPosNodes[6] = rightField;
        tmpPosNodes[7] = centerField;
        tmpPosNodes[8] = leftField;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GETMESSAGE_PITCH){
            if (resultCode == Activity.RESULT_OK){
                push();
                updateNodes(positionNodes);
                String[] result = Pitch.getResultKeyMessage(data);
                System.out.println(result[0]+" IS RESULT[0]");
                System.out.println(result[1]+" IS RESULT[1]");
                System.out.println(pitcher.data.getFirstName()+" "+pitcher.data.getLastName()+" IS THE PITCHER ----------------------");
                pitcher.data.pitchingStats.addPIT();
                switch (result[1]){
                    case ("Ground Ball"):
                        System.out.println("GROUND BALL IS THE RESULT");
                        pitcher.data.pitchingStats.addC();
                        pitcher.data.pitchingStats.addGB();
                        break;
                    case ("Line Drive"):
                        System.out.println("LINE DRIVE IS THE RESULT");
                        pitcher.data.pitchingStats.addC();
                        break;
                    case ("Pop Fly"):
                        System.out.println("POP FLY IS THE RESULT");
                        pitcher.data.pitchingStats.addFB();
                        pitcher.data.pitchingStats.addC();
                        pitcher.data.pitchingStats.addBSF();
                        batter.data.data.battingStats.addSF();
                        break;
                    case ("Bunt"):
                        System.out.println("BUNT IS THE RESULT");
                        pitcher.data.pitchingStats.addC();
                        break;
                }
                switch (result[0]){
                    case ("Out"):
                        System.out.println("OUT IS THE RESULT");
                        outs++;
                        break;
                    case ("Single"):
                        System.out.println("SINGLE IS THE RESULT");
                        batter.data.data.battingStats.addTB(1);
                        strikes = 0;
                        balls = 0;
                        batter.data.data.battingStats.addHits();
                        batter.data.data.battingStats.addTB(1);
                        pitcher.data.pitchingStats.addBHR();
                        pitcher.data.pitchingStats.addBH();
                        moveBatter(1);
                        break;
                    case ("Double"):
                        System.out.println("DOUBLE IS THE RESULT");
                        strikes = 0;
                        balls = 0;
                        batter.data.data.battingStats.addHits();
                        batter.data.data.battingStats.addTB(2);
                        pitcher.data.pitchingStats.addBHR();
                        pitcher.data.pitchingStats.addBH();
                        moveBatter(2);
                        break;
                    case ("Triple"):
                        System.out.println("TRIPLE IS THE RESULT");
                        strikes = 0;
                        balls = 0;
                        batter.data.data.battingStats.addHits();
                        batter.data.data.battingStats.addTB(3);
                        pitcher.data.pitchingStats.addBHR();
                        pitcher.data.pitchingStats.addBH();
                        moveBatter(3);
                        break;
                    case ("In-The-Park Home Run"):
                        System.out.println("IN THE PARK HOME RUN IS THE RESULT");
                        pitcher.data.pitchingStats.addBHR();
                        strikes = 0;
                        balls = 0;
                        batter.data.data.battingStats.addHits();
                        batter.data.data.battingStats.addTB(4);
                        pitcher.data.pitchingStats.addBHR();
                        pitcher.data.pitchingStats.addBH();
                        moveBatter(4);
                        break;
                    case ("Home Run"):
                        System.out.println("HOME RUN IS THE RESULT");
                        pitcher.data.pitchingStats.addBHR();
                        strikes = 0;
                        balls = 0;
                        batter.data.data.battingStats.addHits();
                        batter.data.data.battingStats.addTB(4);
                        pitcher.data.pitchingStats.addBHR();
                        pitcher.data.pitchingStats.addBH();
                    case ("Strike"):
                        System.out.println("STRIKE IS THE RESULT");
                        pitcher.data.pitchingStats.addS();
                        strikes++;
                        break;
                    case ("Ball"):
                        System.out.println("BALL IS THE RESULT");
                        balls++;
                        break;
                    case ("Hit By Pitch"):
                        System.out.println("HIT BY PITCH IS THE RESULT");
                        batter.data.data.battingStats.addBB();
                        pitcher.data.pitchingStats.addBB();
                        batter.data.data.battingStats.addHBP();
                        balls = 4;
                    case ("Catcher Interference"):
                        System.out.println("CATCHER INTERFERENCE IS THE RESULT");
                        batter.data.data.battingStats.addBB();
                        pitcher.data.pitchingStats.addBB();
                        balls = 4;
                    case ("Balk"):
                        System.out.println("BALK IS THE RESULT");
                        batter.data.data.battingStats.addBB();
                        pitcher.data.pitchingStats.addBB();
                        balls = 4;
                    case ("Intentional Walk"):
                        System.out.println("INTENTIONAL WALK IS THE RESULT");
                        batter.data.data.battingStats.addBB();
                        pitcher.data.pitchingStats.addBB();
                        balls = 4;
                    case ("Foul Ball"):
                        System.out.println("FOUL BALL IS THE RESULT");
                        pitcher.data.pitchingStats.addC();
                        if (strikes < 2){
                            strikes++;
                        }
                        break;



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
                            loadFieldingPositionNodes(getStarter());
                            printPositionNodes();
                            loadFieldingButtons();
                            loadBattingPositionNodes(getOtherHead());
                            break;
                        case ("Away"):
                            loadFieldingPositionNodes(getOtherHead());
                            printPositionNodes();
                            loadFieldingButtons();
                            loadBattingPositionNodes(getStarter());
                            break;
                    }

                }else{
                    System.out.println("DOING ELSE");
                    System.out.println(isHome+" IS ISHOME");
                    if (isHome){
                        System.out.println("STARTING TEAM IS HOME");
                        loadFieldingPositionNodes(getStarter());
                        printPositionNodes();
                        loadFieldingButtons();
                        loadBattingPositionNodes(getOtherHead());
                    }else{
                        System.out.println("STARTING TEAM IS AWAY");
                        loadFieldingPositionNodes(getOtherHead());
                        printPositionNodes();
                        loadFieldingButtons();
                        loadBattingPositionNodes(getStarter());
                    }
                }
                updateGameInfo();
                push();
            }
        }

        }

    protected void loadBattingPositionNodes(PlayerNode head){
        pause("Loading Batting Position Nodes");
        if (head.designatedHitter == null){
            batter.data = head;

        }else{
            batter.data = head.designatedHitter;
        }
        batter.data.data.battingStats.addAB();
        System.out.println("ADDING AN AT BAT TO: "+batter.data.data.getFirstName());
        batter.btn.setText(batter.data.data.getFirstName()+" "+batter.data.data.getLastName());
        if (head.next.designatedHitter == null){
            onDeck.data = head.next;
        }else{
            onDeck.data = head.next.designatedHitter;
        }

    }


    public void setHome(boolean home) {
        isHome = home;
    }
}
