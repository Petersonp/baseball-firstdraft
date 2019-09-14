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

import org.w3c.dom.Node;

import java.util.List;

public class GameActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_GETMESSAGE_STARTINGWINDOW = 5;
    public static final int REQUEST_CODE_GETMESSAGE_PITCH = 6;
    public static final int REQUEST_CODE_SUBPLAYER = 19;
    public static final int REQUEST_CODE_RUNNER = 99;
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
    int points =0;
    int opoints =0;

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
    Button btnViewTeam;

    Button btnFirstRunner;
    Button btnSecondRunner;
    Button btnThirdRunner;
    Button btnBatter;

    Button btnDelete;

    Button btnUndo;

    TextView lblGameInfo;
    TextView lblAtBat;
    TextView lblOnDeck;
    TextView lblScores;

    PlayerNode[] positionNodes = {pitcher,catcher,firstBase,secondBase,thirdBase,shortStop,leftField,centerField,rightField};
    String[] positions = {"P","C","FB","SB","TB","SS","LF","CF","RF"};

    //Button[] positionButtons;
    Button[] positionButtons = new Button[9];

    public DatabaseHandler db = new DatabaseHandler(this);

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
        lblScores = (TextView) findViewById(R.id.lblScores);

        btnFirstRunner = (Button) findViewById(R.id.btnFirstRunner);
        btnSecondRunner = (Button) findViewById(R.id.btnSecondRunner);
        btnThirdRunner = (Button) findViewById(R.id.btnThirdRunner);
        btnBatter = (Button) findViewById(R.id.btnBatter); btnBatter.setBackgroundColor(Color.GREEN);

        btnUndo = (Button) findViewById(R.id.btnUndo);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStats = (Button) findViewById(R.id.btnStats);
        btnViewTeam = (Button) findViewById(R.id.btnViewTeam);

        btnDelete = (Button) findViewById(R.id.btnDelete);



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

        btnViewTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDatabase();
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

        btnFirstRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fbRunner.data != null) {
                    openRunner(fbRunner);
                }
                /*
                System.out.println("BTNFIRSTBASE PRESSED");
                if (fbRunner.data != null && fbRunner.next.data == null){
                    fbRunner.next.data = fbRunner.data;
                    fbRunner.next.btn.setText(fbRunner.data.data.getFirstName()+" "+fbRunner.data.data.getLastName());
                    fbRunner.btn.setText("--");
                    fbRunner.data = null;
                    push();
                }
                */
            }
        });
        btnSecondRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sbRunner.data != null) {
                    openRunner(sbRunner);
                }
                /*
                if (sbRunner.data != null && sbRunner.next.data == null){
                    sbRunner.next.data = sbRunner.data;
                    sbRunner.next.btn.setText(sbRunner.data.data.getFirstName()+" "+sbRunner.data.data.getLastName());
                    sbRunner.btn.setText("--");
                    sbRunner.data = null;
                    push();

                }
                */
            }
        });
        btnThirdRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tbRunner.data != null) {
                    openRunner(tbRunner);
                }
                /*
                if (tbRunner.data.team != null){
                    points++;
                }else{
                    opoints++;
                }
                if (tbRunner.data != null){
                    tbRunner.data = null;
                    tbRunner.btn.setText("--");
                    push();
                }
                */
                updateGameInfo();

            }
        });


        //load database
        //loadDatabase();

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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDatabase();
            }
        });

    }
    // database work
    private void loadDatabase(){
        pause("LOADING DATABASE");
        List<Player> players = db.getAllPlayers();
        PlayerNode tmp;
        int count = 0;
        if (db.getPlayerCount() > 0){
            PlayerNode newNode = new PlayerNode();
            newNode.data = players.get(0);
            newNode.team = "Bearcats";
            newNode.index = 0;
            setHead(newNode);
            tmp = getHead();
            for (Player player: players){
                if (count != 0){
                    PlayerNode newNode1 = new PlayerNode();
                    newNode1.data = player;
                    newNode1.team = "Bearcats";
                    newNode1.index = count;
                    tmp.next = newNode1;
                    tmp = tmp.next;
                }
                count++;

            }
        }



        /*
        PlayerNode tmp;
        if (db.getPlayerCount()>0){
            System.out.println("PLAYERCOUNT IS GREATER THAN 0");
            PlayerNode newNode = new PlayerNode();
            newNode.data = db.getPlayer(1);
            newNode.team = "Bearcats";
            newNode.index = 0;
            setHead(newNode);
            tmp = getHead();
            for (int i = 1; i < db.getPlayerCount();i++){
                System.out.println("i = "+i);
                PlayerNode newNode1 = new PlayerNode();
                newNode1.data = db.getPlayer(i+1);
                newNode.index = i;
                newNode1.team = "Bearcats";
                tmp.next = newNode1;
                System.out.println("TMP is: "+tmp.data.getFirstName()+" and next is: "+tmp.next);
                tmp = tmp.next;

            }
        }*/

    }

    private void printDatabase(){
        pause("PRINTING DATABSE");
        //
        List<Player> players = db.getAllPlayers();
        System.out.println("Player count is: "+ db.getPlayerCount());
        for (Player player: players){
            String log = "Id: " + player.getId() + " ,Name: " + player.getFirstName()+ " " + player.getLastName() + " ,Number: " +
                    player.getPlayerNumber();
            System.out.println(log);
        }
        List<PitchingStats> psList = db.getAllPS();
        for (PitchingStats ps: psList){
            String log = "PIT: "+ ps.getPIT();
            System.out.println(log);
        }

    }

    private void deleteDatabase(){
        List<Player> players = db.getAllPlayers();

        for (Player player: players){
            db.deletePlayer(player);
        }
    }

    private void updateDatabase(){
        pause("UPDATING DATABASE");
        deleteDatabase();
        PlayerNode tmp = getHead();
        db.addPlayer(tmp.data);
        printDatabase();
        while (tmp.next != null){
            printDatabase();
            db.addPlayer(tmp.next.data);
            tmp = tmp.next;
        }
    }
     //


    protected void openRunner(BatterNode runner){
        Intent i = new Intent(getApplicationContext(), Runner.class);
        Bundle bundle = new Bundle();
        String[] stuff = new String[4];
        stuff[0] = runner.data.data.getFirstName();
        stuff[1] = runner.data.data.getLastName();
        stuff[2] = runner.data.data.getPlayerNumber();
        stuff[3] = String.valueOf(runner.order);
        bundle.putStringArray("Runner",stuff);
        i.putExtras(bundle);
        startActivityForResult(i, REQUEST_CODE_RUNNER);
    }

    protected void substituteActivity(PlayerNode player){
        pause("SUBSTITUTING ACTIVITY");
        System.out.println(player.data.getFirstName()+" "+player.data.getLastName()+" @"+player.positon);
        Intent i = new Intent(getApplicationContext(),SubPlayer.class);
        Bundle bundle = new Bundle();
        String[] stuff = new String[3];
        stuff[0] = player.data.getFirstName();
        stuff[1] = player.data.getLastName();
        stuff[2] = player.positon;
        bundle.putStringArray("Position",new String[]{player.data.getFirstName(),player.data.getLastName(),player.positon});
        i.putExtras(bundle);
        startActivityForResult(i, REQUEST_CODE_SUBPLAYER);

    }



    protected void updateGameInfo(){
        lblGameInfo.setText("I:"+getInning(inning)+" B:"+balls+" S:"+strikes+" O:"+outs);
        lblAtBat.setText("At Bat: "+batter.data.data.getFirstName()+" "+batter.data.data.getLastName());
        lblOnDeck.setText("On Dec: "+onDeck.data.data.getFirstName()+" "+onDeck.data.data.getLastName());
        lblScores.setText("Bearcats: "+points+" Opponent: "+opoints);

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
            if(tmp.positon!=null) {
                if (tmp.positon.equals(positions[i])) {
                    positionNodes[i] = tmp;
                    break;
                }
            }else{
                System.out.println(tmp.data.getFirstName()+" "+tmp.data.getLastName()+" has no position");
            }
        }
        while (tmp.next != null){
            for (int i = 0; i < positions.length;i++){
                if(tmp.next.positon!=null) {
                    if (tmp.next.positon.equals(positions[i])) {
                        positionNodes[i] = tmp.next;
                    }
                }
            }
            tmp = tmp.next;
        }
        updateNodes(positionNodes);
    }

    protected int getLength(PlayerNode head){
        pause("GETTING LENGTH");
        int count = 0;
        PlayerNode tmp = head;
        PlayerNode first = tmp;
        System.out.println("HEAD = "+tmp.data.getFirstName()+" "+tmp.data.getLastName());
        count++;
        while(tmp.next != null){
            System.out.println("HEAD.NEXT = "+tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName());
            count++;
            System.out.println(count);
            tmp = tmp.next;
        }
        return count;
    }

    //Linked List
    protected Game newGame(){
        pause("NEW GAME");
        Game g = new Game();
        g.setStrikes(strikes);
        g.setBalls(balls);
        g.setOuts(outs);
        g.setInning(inning);

        String[][] statsArray = new String[getLength(getHead())][18];
        PlayerNode tmp = getHead();
        for (int i =0; i<getLength(getHead());i++){
            PitchingStats ps = tmp.data.pitchingStats;
            BattingStats bs = tmp.data.battingStats;
            String[] stats = {ps.getBH(),ps.getBHR(),ps.getBAB(),ps.getBK(),ps.getBSF(),ps.getGB(),ps.getPIT(),ps.getFB(),ps.getBB(),ps.getS(),ps.getC(),
                    bs.getHits(),bs.getAB(),bs.getBB(),bs.getHBP(),bs.getK(),bs.getTB(),bs.getSF()};
            for (int j = 0; j < 18; j++) {
                statsArray[i][j] = stats[j];
            }
            if (tmp.next == null) {
                break;
            }
            tmp = tmp.next;
        }
        g.setStats(statsArray);


        PlayerNode[] roster = new PlayerNode[getLength(getHead())];

        int count = 0;
        PlayerNode tmp1 = getHead();
        roster[count] = tmp1;
        System.out.println("PSLIST[COUNT] = "+ tmp1.data.pitchingStats.getPIT());
        while(tmp1.next != null){
            count++;
            roster[count] = tmp1.next;

            tmp1 = tmp1.next;
        }
        g.setRosterArray(roster);
        /*
        PlayerNode[] lineup = new PlayerNode[getLength(getStarter())];
        count = 0;
        tmp = getStarter();
        lineup[count] = tmp;
        while(tmp.next != null){
            count++;
            lineup[count] = tmp.next;
            tmp = tmp.next;
        }
        PlayerNode[] otherLineup = new PlayerNode[getLength(getOtherHead())];
        g.setRosterArray(roster);
        g.setLineupArray(lineup);
        */

        g.setLineup(getStarter());

        g.setOtherLineup(getOtherHead());

        g.setBatter(batter.data);
        g.setOnDeck(onDeck.data);
        g.setFbRunner(fbRunner.data);
        g.setSbRunner(sbRunner.data);
        g.setTbRunner(tbRunner.data);
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
    protected void createLineUp(){
        pause("CREATING LINE UP");
        int lineUpCount = 1;
        PlayerNode tmp1 = getStarter();
        System.out.println("COMPARING LINEUPCOUNT: "+lineUpCount+" TO 9");
        while (lineUpCount <= 9) {
            PlayerNode tmp = getHead();
            System.out.println("COMPARING TMP.ORDER: "+tmp.order+" TO LINEUPCOUNT: "+lineUpCount);
            while (tmp.order != lineUpCount) {
                System.out.println("THIS IS TMP BEFORE: "+tmp.data.getFirstName()+" "+tmp.data.getLastName()+" ORDER:"+tmp.order);
                tmp = tmp.next;
                System.out.println("THIS IS TMP AFTER: "+tmp.data.getFirstName()+" "+tmp.data.getLastName()+" ORDER:"+tmp.order);
            }
            lineUpCount++;
            if (getStarter() == null) {
                setStarter(tmp);
                tmp1 = getStarter();
            } else {

                tmp1.next = tmp;
                tmp1 = tmp1.next;

            }
        }

    }

    protected void setGame(Game g){
        pause("SETTING GAME");
        this.strikes = g.getStrikes();
        balls = g.getBalls();
        outs = g.getOuts();
        inning = g.getInning();



        PlayerNode[] rosterArray = g.getRosterArray();
        PlayerNode newNode = new PlayerNode();
        newNode = rosterArray[0];
        String[][] statsArray = g.getStats();
        String[] stats1 = statsArray[0];
        PitchingStats ps1 = new PitchingStats();
        BattingStats bs1 = new BattingStats();
        ps1.setBH(Integer.parseInt(stats1[0]));
        ps1.setBHR(Integer.parseInt(stats1[1]));
        ps1.setBAB(Integer.parseInt(stats1[2]));
        ps1.setBK(Integer.parseInt(stats1[3]));
        ps1.setBSF(Integer.parseInt(stats1[4]));
        ps1.setGB(Integer.parseInt(stats1[5]));
        ps1.setPIT(Integer.parseInt(stats1[6]));
        System.out.println("SETTING PIT TO "+stats1[6]);
        ps1.setFB(Integer.parseInt(stats1[7]));
        ps1.setBB(Integer.parseInt(stats1[8]));
        ps1.setS(Integer.parseInt(stats1[9]));
        ps1.setC(Integer.parseInt(stats1[10]));

        bs1.setHits(Integer.parseInt(stats1[11]));
        bs1.setAB(Integer.parseInt(stats1[12]));
        bs1.setBB(Integer.parseInt(stats1[13]));
        bs1.setHBP(Integer.parseInt(stats1[14]));
        bs1.setK(Integer.parseInt(stats1[15]));
        bs1.setTB(Integer.parseInt(stats1[16]));
        bs1.setSF(Integer.parseInt(stats1[17]));
        newNode.data.pitchingStats = ps1;
        newNode.data.battingStats = bs1;
        setHead(newNode);
        PlayerNode rtmp = getHead();

        for (int i=1; i < rosterArray.length-1;i++){
            String[] stats = statsArray[i];
            PitchingStats ps = new PitchingStats();
            BattingStats bs = new BattingStats();
            ps.setBH(Integer.parseInt(stats[0]));
            ps.setBHR(Integer.parseInt(stats[1]));
            ps.setBAB(Integer.parseInt(stats[2]));
            ps.setBK(Integer.parseInt(stats[3]));
            ps.setBSF(Integer.parseInt(stats[4]));
            ps.setGB(Integer.parseInt(stats[5]));
            ps.setPIT(Integer.parseInt(stats[6]));
            System.out.println("SETTING PIT TO "+stats[6]);
            ps.setFB(Integer.parseInt(stats[7]));
            ps.setBB(Integer.parseInt(stats[8]));
            ps.setS(Integer.parseInt(stats[9]));
            ps.setC(Integer.parseInt(stats[10]));

            bs.setHits(Integer.parseInt(stats[11]));
            bs.setAB(Integer.parseInt(stats[12]));
            bs.setBB(Integer.parseInt(stats[13]));
            bs.setHBP(Integer.parseInt(stats[14]));
            bs.setK(Integer.parseInt(stats[15]));
            bs.setTB(Integer.parseInt(stats[16]));
            bs.setSF(Integer.parseInt(stats[17]));
            rtmp.next = rosterArray[i];
            rtmp.next.data.pitchingStats = ps;
            rtmp.next.data.battingStats = bs;
            System.out.println(rosterArray[i].data.pitchingStats.getPIT());
            System.out.println(rtmp.data.getFirstName()+" "+rtmp.data.getLastName()+".next = "+rosterArray[i].data.getFirstName()+" "+rosterArray[i].data.getLastName());
            rtmp = rtmp.next;
        }
        System.out.println("HEAD: "+getHead().data.getFirstName()+".next = "+getHead().next.data.getFirstName());


        setOtherHead(g.getOtherLineup());
        setStarter(g.getLineup());

        batter.data = g.getBatter();
        onDeck.data = g.getOnDeck();
        fbRunner.data = g.getFbRunner();
        sbRunner.data = g.getSbRunner();
        tbRunner.data = g.getTbRunner();

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
        updateDatabase();


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
        pause("PRINTING LINEUP");
        PlayerNode tmp = getStarter();
        System.out.println(tmp.order+": "+tmp.data.getFirstName()+" "+tmp.data.getLastName()+" #"+tmp.data.getPlayerNumber()+" @"+tmp.positon+" ----");

        while(tmp.next!=null){
            System.out.println("next is: "+tmp.next.order+": "+tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName()+" #"+tmp.next.data.getPlayerNumber()+" @"+tmp.next.positon+" ----");
            if(tmp.next.isChecked==true) {
                System.out.println(tmp.next.order + ": " + tmp.next.data.getFirstName() + " " + tmp.next.data.getLastName() + " #" + tmp.next.data.getPlayerNumber() + " @" + tmp.next.positon);
            }
            tmp = tmp.next;
        }
        /*
        for (int i =0;i<8;i++){
            System.out.println(tmp.next.order+": "+tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName()+" #"+tmp.next.data.getPlayerNumber()+" @"+tmp.next.positon);
            tmp = tmp.next;
        }*/

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
                if (tbRunner.data.team != null) {
                    points++;
                } else {
                    opoints++;
                }
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
            swapSides();
        }

    }

    protected void swapSides(){
        if (isHome = true){
            isHome = false;
            loadFieldingPositionNodes(getOtherHead());
            //printPositionNodes();
            loadFieldingButtons();
            loadBattingPositionNodes(getStarter());
        }else{
            isHome = true;
            loadFieldingPositionNodes(getStarter());
            //printPositionNodes();
            loadFieldingButtons();
            loadBattingPositionNodes(getOtherHead());
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
                        pitcher.data.pitchingStats.addS();
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
                        pitcher.data.pitchingStats.addS();
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
                        pitcher.data.pitchingStats.addS();
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
                        pitcher.data.pitchingStats.addS();
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
                        pitcher.data.pitchingStats.addS();
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
                        pitcher.data.pitchingStats.addS();
                        moveBatter(4);
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
                deleteDatabase();
                updateDatabase();

            }
        }else if (requestCode == REQUEST_CODE_GETMESSAGE_STARTINGWINDOW){
            if (resultCode == Activity.RESULT_OK) {
                String result = LineUp.getResultKeyMessage(data);
                System.out.println(result + " IS THE RESULT");
                if (result != null) {
                    switch (result) {
                        case ("Home"):
                            loadFieldingPositionNodes(getStarter());
                            //printPositionNodes();
                            loadFieldingButtons();
                            loadBattingPositionNodes(getOtherHead());
                            break;
                        case ("Away"):
                            loadFieldingPositionNodes(getOtherHead());
                            //printPositionNodes();
                            loadFieldingButtons();
                            loadBattingPositionNodes(getStarter());
                            break;
                    }

                }else{
                    if (isHome){
                        loadFieldingPositionNodes(getStarter());
                        //printPositionNodes();
                        loadFieldingButtons();
                        loadBattingPositionNodes(getOtherHead());
                    }else{
                        loadFieldingPositionNodes(getOtherHead());
                        //printPositionNodes();
                        loadFieldingButtons();
                        loadBattingPositionNodes(getStarter());
                    }
                }
                updateGameInfo();
                push();
            }
        }else if (requestCode == REQUEST_CODE_SUBPLAYER){
            if (resultCode == Activity.RESULT_OK){
                String[] result = SubPlayer.getResultKeyMessage(data);
                switchPlayer(result[0],result[1],result[2],result[3],result[4],result[5]);
                printLineUp();
                loadFieldingPositionNodes(getStarter());
                printPositionNodes();
                loadFieldingButtons();
                printLineUp();
                printRoster();
            }else{
                System.out.println("Cancelled subplayer");
            }
        }else if (requestCode == REQUEST_CODE_RUNNER){
            if (resultCode == Activity.RESULT_OK){
                String[] result = Runner.getResultKeyMessage(data);
                BatterNode tmp = new BatterNode();
                switch (result[0]){
                    case ("1"):
                        tmp = fbRunner;
                        break;
                    case ("2"):
                        tmp = sbRunner;
                        break;
                    case ("3"):
                        tmp = tbRunner;
                        break;
                    default:
                        System.out.println("FIRST SWITCH CASE BUT NO MATCH");
                        break;
                }
                switch (result[1]){
                    case ("Out"):
                        outs++;
                        tmp.data = null;
                        tmp.btn.setText("--");
                        break;
                    case ("Safe"):
                        tmp.data.data.runningStats.addSA();
                        tmp.data.data.runningStats.addSS();
                        if (tmp != tbRunner) {
                            tmp.next.data = tmp.data;
                        }else{
                            if (tbRunner.data.team != null) {
                                points++;
                            } else {
                                opoints++;
                            }
                        }
                        tmp.btn.setText("--");
                        break;
                    case ("SOut"):
                        tmp.data.data.runningStats.addSA();
                        tmp.data = null;
                        tmp.btn.setText("--");
                        break;
                    default:
                        System.out.println("SECOND SWITCH CASE BUT NO MATCH");
                        break;
                }
                System.out.println("POST SWITCH CASES");

                if (result[0].equals("1")){
                    tmp = fbRunner;
                }else if (result[0].equals("2")){
                    tmp = sbRunner;
                }else if (result[0].equals("3")){
                    tmp = tbRunner;
                }else{
                    System.out.println("FIRST IF BUT NO MATCH");
                }
                if (result[1].equals("Out")){
                    System.out.println(tmp.data.data.getFirstName()+" "+tmp.data.data.getLastName()+" IS THE RUNNER IN OUT");
                    outs++;
                    tmp.data = null;
                    tmp.btn.setText("--");
                }else if (result[1].equals("Safe")){
                    System.out.println(tmp.data.data.getFirstName()+" "+tmp.data.data.getLastName()+" IS THE RUNNER IN SAFE");
                    tmp.data.data.runningStats.addSA();
                    tmp.data.data.runningStats.addSS();
                    if (tmp != tbRunner) {
                        tmp.next.data = tmp.data;
                    }else{
                        if (tbRunner.data.team != null) {
                            points++;
                        } else {
                            opoints++;
                        }
                    }
                    tmp.btn.setText("--");
                }else if (result[1].equals("SOut")){
                    tmp.data.data.runningStats.addSA();
                    tmp.data = null;
                    tmp.btn.setText("--");
                }else{
                }
            checkGameInfo();
            updateGameInfo();
            }
        }

    }

    protected void switchPlayer(String fn, String ln, String no, String ofn, String oln, String ono){
        pause("SWITCHING PLAYERS");
        printLineUp();
        PlayerNode tmp = getStarter();
        PlayerNode newPlayer = findPlayer(ofn,oln,ono);
        if (newPlayer.isChecked) {
            if (fn.equals(tmp.data.getFirstName()) && ln.equals(tmp.data.getLastName()) && no.equals(tmp.data.getPlayerNumber())) {
                int tmpOrder = newPlayer.order;
                newPlayer.order = tmp.order;
                tmp.order = tmpOrder;

                String tmpPos = newPlayer.positon;
                newPlayer.positon = tmp.positon;
                tmp.positon = tmpPos;
                /*
                newPlayer.next = tmp.next;
                setStarter(newPlayer);
                */
            } else {
                while (tmp.next != null) {
                    if (fn.equals(tmp.next.data.getFirstName()) && ln.equals(tmp.next.data.getLastName()) && no.equals(tmp.next.positon)) {
                        pause("FOUND^");
                        int tmpOrder = newPlayer.order;
                        newPlayer.order = tmp.next.order;
                        tmp.next.order = tmpOrder;

                        String tmpPos = newPlayer.positon;
                        newPlayer.positon = tmp.next.positon;
                        tmp.next.positon = tmpPos;

                        /*
                        newPlayer.next = tmp.next.next;
                        tmp.next = newPlayer;
                        */
                    }
                    tmp = tmp.next;
                }
            }
        }else{
            System.out.println("PLAYER IS NOT IN THE LINEUP");
            if (fn.equals(tmp.data.getFirstName()) && ln.equals(tmp.data.getLastName()) && no.equals(tmp.positon)) {
                newPlayer.positon = tmp.positon;
                newPlayer.order = tmp.order;
                newPlayer.isChecked = true;
                newPlayer.next = tmp.next;
                tmp.isChecked = false;
                tmp.positon = null;
                tmp.order = 0;
                tmp.next = null;
                setStarter(newPlayer);
            } else {
                while (tmp.next != null) {
                    if (fn.equals(tmp.next.data.getFirstName()) && ln.equals(tmp.next.data.getLastName()) && no.equals(tmp.next.positon)) {
                        pause("FOUND^");
                        PlayerNode tmp2 = getStarter();
                        while (!(tmp2.next.data.getFirstName().equals(ofn))){
                            tmp2 = tmp2.next;
                        }
                        PlayerNode tmp1 = tmp2.next.next;
                        tmp2.next = tmp.next;
                        newPlayer.positon = tmp.next.positon;
                        newPlayer.order = tmp.next.order;
                        newPlayer.isChecked = true;
                        tmp.next.isChecked = false;
                        tmp.next.positon = null;
                        tmp.next.order = 0;
                        newPlayer.next = tmp.next.next;
                        tmp.next = newPlayer;
                        tmp2.next.next =tmp1;
                        break;
                    }
                    tmp = tmp.next;
                }
            }
        }
    }


    protected PlayerNode findPlayer(String fn, String ln, String no){
        PlayerNode tmp = getHead();
        if (fn.equals(tmp.data.getFirstName())&&ln.equals(tmp.data.getLastName())&&no.equals(tmp.data.getPlayerNumber())){
            return tmp;
        }
        while (tmp.next != null){
            if (fn.equals(tmp.next.data.getFirstName())&&ln.equals(tmp.next.data.getLastName())&&no.equals(tmp.next.data.getPlayerNumber())){
                return tmp.next;
            }
            tmp = tmp.next;
        }
        return tmp;
    }



    protected void loadBattingPositionNodes(PlayerNode head){
        pause("Loading Batting Position Nodes");
        if (head.designatedHitter == null){
            batter.data = head;

        }else{
            batter.data = head.designatedHitter;
        }
        batter.data.data.battingStats.addAB();
        batter.btn.setText(batter.data.data.getFirstName()+" "+batter.data.data.getLastName());
        if (head.next.designatedHitter == null){
            onDeck.data = head.next;
        }else{
            onDeck.data = head.next.designatedHitter;
        }

    }

    public void printRoster(){
        pause("PRINTING ROSTER");
        PlayerNode tmp = getHead();
        System.out.println(tmp.order+": "+tmp.data.getFirstName()+" "+tmp.data.getLastName()+" #"+tmp.data.getPlayerNumber()+" ----");
        while(tmp.next!=null){
            System.out.println("next is: "+tmp.next.order+": "+tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName()+" #"+tmp.next.data.getPlayerNumber()+" ----");
            if(tmp.next.isChecked==true) {
                System.out.println(tmp.next.order + ": " + tmp.next.data.getFirstName() + " " + tmp.next.data.getLastName() + " #" + tmp.next.data.getPlayerNumber());
            }
            tmp = tmp.next;
        }
    }


    public void setHome(boolean home) {
        isHome = home;
    }
}
