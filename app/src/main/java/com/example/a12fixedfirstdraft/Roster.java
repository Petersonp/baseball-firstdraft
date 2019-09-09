package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Roster extends StartingWindow {
    public static final int REQUEST_CODE_GETMESSAGE_PLAYERINFO = 5;
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.Roster - Return Message";
    Button btnNewPlayer;
    TableLayout tblRoster;
    Button btnBack;
    Button btnPrint;

    boolean isEdit;



    int count = 0;
    int index_id = 4083;
    int first_id = 4183;
    int last_id = 4283;
    int number_id = 4383;
    int edit_id = 4483;
    int remove_id = 4583;
    int row_id = 4683;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roster_layout);
        pause("OPENING ROSTER");

        //setting views
        tblRoster = (TableLayout) findViewById(R.id.tblStats);
        btnNewPlayer = (Button) findViewById(R.id.btnNewPlayer);
        btnBack = (Button) findViewById(R.id.btnBack);

        //Header
        TableRow header = new TableRow(getApplicationContext());
        header.setPadding(10,0,0,0);

        String[] labels = {"   ","First Name","Last Name","#","Edit","Remove"};
        for (int i = 0 ; i <labels.length;i++){
            TextView headTmp = new TextView(getApplicationContext());
            headTmp.setText(labels[i]);
            headTmp.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            headTmp.setTextSize(25);
            headTmp.setPadding(50,0,50,0);
            header.addView(headTmp);
        }

        tblRoster.addView(header);


        btnNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = false;
                openPlayerInfo(isEdit,null,null,null,null);
            }
        });

        loadTable();

        //Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("BTN BACK ---------------------------------");
                printIndex();
                if(getHead()!= null) {
                    System.out.println("HEAD IS: " + getHead().data.getFirstName() + " " + getHead().data.getLastName() + " #" + getHead().data.getPlayerNumber());
                }else{
                    System.out.println("HEAD IS NULL ------------------------------------------------------------------------");
                }
                finish();

            }
        });

        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 9;i++){

                    int[] id = {index_id + count, first_id + count, last_id + count, number_id + count, edit_id + count, remove_id + count, row_id + count};
                    String[] msg = {String.valueOf(count + 1), "fn" + String.valueOf(count + 1), "ln" + String.valueOf(count + 1), String.valueOf(count + 1)};
                    addTableRow(id, msg);
                    System.out.println(count + " COUNT BEFORE------");
                    count++;
                    System.out.println(count + " COUNT AFTER------");
                    Player player = new Player();
                    player.setFirstName("fn" + String.valueOf(count));
                    player.setLastName("ln" + String.valueOf(count));
                    player.setPlayerNumber(String.valueOf(count));
                    addPlayerNode(player, count - 1);
                    printIndex();
                }
            }
        });


    }

    protected void loadTable(){
        pause("LOADING TABLE");
        if (getHead() == null){
            System.out.println("HEAD IS NULL");
            return;
        }else{
            System.out.println("HEAD IS NOT NULL");
            PlayerNode tmp = getHead();
            int[] id = {index_id+count, first_id+count, last_id+count, number_id+count, edit_id+count, remove_id+count, row_id+count};
            String[] msg = {String.valueOf(count+1),tmp.data.getFirstName(),tmp.data.getLastName(),tmp.data.getPlayerNumber()};
            addTableRow(id,msg);
            count++;
            while (tmp.next!= null){
                System.out.println("TMP.NEXT");
                int[] ids = {index_id+count, first_id+count, last_id+count, number_id+count, edit_id+count, remove_id+count, row_id+count};
                String[] msgs = {String.valueOf(count+1),tmp.next.data.getFirstName(),tmp.next.data.getLastName(),tmp.next.data.getPlayerNumber()};
                addTableRow(ids,msgs);
                count++;
                tmp = tmp.next;

            }


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println(isEdit + " ISEDIT");
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_GETMESSAGE_PLAYERINFO:
                if (isEdit){
                    System.out.println("RUNNING ISEDIT");
                    final Intent d = data;
                    String[] result = PlayerInfo.getResultKeyMessage(data);
                    int[] ids = {first_id,last_id,number_id};
                    int ref = Integer.valueOf(result[3])-1;
                    for (int i = 0;i<ids.length;i++){
                        System.out.println(i+" i YEET --------------");
                        TextView lblTmp = (TextView) findViewById(ids[i]+ref);
                        System.out.println((ids[i]+ref)+" THIS IS THE ID OF THE LBLTMP -----------------");
                        lblTmp.setText(result[i].toString());
                    }

                    PlayerNode tmp = getHead();
                    System.out.println(tmp.data.getFirstName());
                    System.out.println(tmp.data.getLastName());
                    System.out.println(tmp.data.getPlayerNumber());
                    if (tmp.index == ref){
                        tmp.data.setFirstName(result[0]);
                        tmp.data.setLastName(result[1]);
                        tmp.data.setPlayerNumber(result[2]);
                    }
                    else if(tmp.next != null) {
                        System.out.println(tmp.next.index+" tmp.next.index");
                        System.out.println(tmp.index + " tmp.index");
                        System.out.println(ref+ "ref");

                        while (tmp.next.index != ref) {
                            tmp = tmp.next;
                        }
                        System.out.println("OLD PLAYER: "+tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName()+" #"+tmp.next.data.getPlayerNumber());
                        tmp.next.data.setFirstName(result[0]);
                        tmp.next.data.setLastName(result[1]);
                        tmp.next.data.setPlayerNumber(result[2]);
                        System.out.println("NEW PLAYER: "+tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName()+" #"+tmp.next.data.getPlayerNumber());
                    }


                    Button btnEdit = (Button) findViewById(edit_id+ref);
                    Button btnRemove = (Button) findViewById(remove_id+ref);

                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openEdit(v);
                        }
                    });

                    btnRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removePlayer(v);
                        }
                    });

                }
                else if (Activity.RESULT_OK == resultCode){
                    System.out.println("NOT RUNNING IS EDIT");
                    String[] result = PlayerInfo.getResultKeyMessage(data);
                    int[] id = {index_id+count, first_id+count, last_id+count, number_id+count, edit_id+count, remove_id+count, row_id+count};
                    String[] msg = {String.valueOf(count+1),result[0],result[1],result[2]};
                    addTableRow(id,msg);
                    System.out.println(count + " COUNT BEFORE------");
                    count++;
                    System.out.println(count + " COUNT AFTER------");
                    Player player = new Player();
                    player.setFirstName(result[0]);
                    player.setLastName(result[1]);
                    player.setPlayerNumber(result[2]);
                    addPlayerNode(player,count-1);


                }
        }
    }

    protected void addTableRow(int[] ids, String[] msg){
        TableRow tr = new TableRow(getApplicationContext());
        tr.setId(ids[6]);
        tr.setPadding(10,0,0,0);
        for (int i =0; i <4;i++){
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setId(ids[i]);
            lblTmp.setText(msg[i]);
            lblTmp.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            lblTmp.setTextSize(20);
            lblTmp.setPadding(40,0,55,0);
            tr.addView(lblTmp);
        }
        Button btnEdit = new Button(getApplicationContext());
        btnEdit.setId(ids[4]);
        btnEdit.setText("Edit");
        btnEdit.setPadding(20,0,20,0);
        tr.addView(btnEdit);

        Button btnRemove = new Button(getApplicationContext());
        btnRemove.setId(ids[5]);
        btnRemove.setText("Remove");
        btnRemove.setPadding(20,0,20,0);
        tr.addView(btnRemove);

        tblRoster.addView(tr);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = removePlayer(v);
                removePlayerNode(i);
                printIndex();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEdit(v);
            }
        });

    }

    protected int removePlayer(View v){
        int ref =  v.getId()-remove_id;
        System.out.println("REMOVE REFERENCE IS: "+ref);
        int[] ids1 = {first_id,last_id,number_id};
        int[] ids2 = {edit_id,remove_id};
        TableRow tr = (TableRow) findViewById(row_id+ref);
        tblRoster.removeView(tr);
        for (int i = ref+1; i <count;i++){
            TextView lblIndex = (TextView) findViewById(index_id+i);
            System.out.println("OLD ID OF: "+lblIndex.getText()+": "+lblIndex.getId());
            lblIndex.setText(String.valueOf(Integer.valueOf(lblIndex.getText().toString())-1));
            lblIndex.setId(index_id+i-1);
            System.out.println("NEW ID OF: "+lblIndex.getText()+": "+lblIndex.getId());
            System.out.println(" ");
            TableRow trTmp = (TableRow) findViewById(row_id+i);
            trTmp.setId(row_id+i-1);

            for (int j = 0; j<ids1.length; j++ ){
                TextView lblTmp = (TextView) findViewById(ids1[j]+i);
                lblTmp.setId(ids1[j]+i-1);
            }
            for (int k = 0; k<ids2.length;k++){
                Button btnTmp = (Button) findViewById(ids2[k]+i);
                btnTmp.setId(ids2[k]+i-1);
            }
        }
        count--;

        return ref;
    }

    protected void openEdit(View v){
        isEdit = true;
        int ref = v.getId()-edit_id;
        TextView lblIndex = (TextView) findViewById(index_id+ref);
        TextView lblFirst = (TextView) findViewById(first_id+ref);
        TextView lblLast = (TextView) findViewById(last_id+ref);
        TextView lblNumber = (TextView) findViewById(number_id+ref);
        openPlayerInfo(isEdit,
                lblFirst.getText().toString(),
                lblLast.getText().toString(),
                lblNumber.getText().toString(),
                lblIndex.getText().toString());
    }

    protected void openPlayerInfo(boolean isEdit, String firstName, String lastName, String playerNumber, String index){
        String[] info = {String.valueOf(isEdit),firstName,lastName,playerNumber,index};
        Intent i = new Intent(getApplicationContext(),PlayerInfo.class);
        Bundle b = new Bundle();
        b.putStringArray("info",info);
        i.putExtras(b);
        startActivityForResult(i, REQUEST_CODE_GETMESSAGE_PLAYERINFO);
    }

    protected void addPlayerNode(Player player, int index){
        PlayerNode newPlayer = new PlayerNode();
        newPlayer.data = player;
        newPlayer.team = "Bearcats";
        System.out.println(index+ " IS THE INDEX FOR THE NEW PLAYERNODE");
        newPlayer.index = index;
        System.out.println(newPlayer.index + " IS THE INDEX OF THE NEW PLAYER");
        PlayerNode tmp32 = newPlayer;
        System.out.println(tmp32.index + " IS THE INDEX OF COPY NEWPLAYER");
        if (getHead() == null){
            setHead(newPlayer);
            db.addPlayer(newPlayer.data);
            System.out.println(getHead().index+ " THIS IS THE INDEX OF THE HEAD NODE");
        }else{
            PlayerNode tmp = getHead();
            while(tmp.next != null){
                tmp = tmp.next;
            }
            tmp.next = newPlayer;
            db.addPlayer(newPlayer.data);
            System.out.println(tmp.next.index + " THIS IS THE INDEX OF "+tmp.next.data.getFirstName());
        }
        printIndex();
    }

    protected void printIndex(){
        if(getHead() != null) {
            System.out.println("PRINGINT INDEXES");
            PlayerNode tmp = getHead();
            System.out.println(tmp.data.getFirstName() + ": " + tmp.index);
            while (tmp.next != null) {
                System.out.println(tmp.next.data.getFirstName() + ": " + tmp.next.index);
                tmp = tmp.next;

            }
        }
    }

    protected void removePlayerNode(int index){
        printIndex();
        PlayerNode tmp = getHead();

        //If deleting at head
        if (tmp.index == index){
            if (tmp.next == null){
                setHead(null);
            }else{
                setHead(getHead().next);
                while(tmp.next != null){
                    tmp.next.index =(tmp.next.index-1);
                    tmp = tmp.next;
                }
            }
            //deleting in the mid /end
        }else{
            System.out.println("PRE WHILE LOOP: "+tmp.next.index);
            while (tmp.next.index != index){
                System.out.println("IN WHILE LOOP: "+tmp.next.index);
                    tmp = tmp.next;
                }
            if (tmp.next.next == null){
                tmp.next = null;
            }else {
                tmp.next = tmp.next.next;
                while (tmp.next != null) {
                    tmp.next.index = (tmp.next.index - 1);
                    tmp = tmp.next;
                }
            }
        }
    }
}
