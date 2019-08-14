package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AddFromRoster extends GameActivity {
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.AddFromRoster - Return Message";
    Button btnAddPlayer;

    TableLayout tblAddFromRoster;
    static int lineUpCount = 0;

    int count = 0;
    int index_id = 4083;
    int first_id = 4183;
    int last_id = 4283;
    int number_id = 4383;
    int select_id = 4483;
    int row_id = 4583;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfromroster_layout);

        //Setting views
        btnAddPlayer = (Button) findViewById(R.id.btnAddPlayer);
        tblAddFromRoster = (TableLayout) findViewById(R.id.tblStats);

        //Setting header
        TableRow header = new TableRow(getApplicationContext());
        header.setPadding(10, 0, 0, 0);

        String[] labels = {"   ", "First Name", "Last Name", "#", "Select"};
        for (int i = 0; i < labels.length; i++) {
            TextView headTmp = new TextView(getApplicationContext());
            headTmp.setText(labels[i]);
            headTmp.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            headTmp.setTextSize(25);
            headTmp.setPadding(60, 0, 60, 0);
            header.addView(headTmp);
        }
        tblAddFromRoster.addView(header);

        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                String result = "NOTHING";
                i.putExtra(RESULT_KEY_MESSAGE,result);
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });

        loadTable();


    }

    protected void loadTable() {
        if (getHead() == null) {
            System.out.println("HEAD IS NULL");
            return;
        } else {

            System.out.println("HEAD IS NOT NULL");
            PlayerNode tmp = getHead();
            if (tmp.isChecked == false) {
                int[] id = {index_id + count, first_id + count, last_id + count, number_id + count, select_id + count, row_id + count};
                String[] msg = {"  ", tmp.data.getFirstName(), tmp.data.getLastName(), tmp.data.getPlayerNumber()};
                addTableRow(id, msg);

            }
            count++;
            while (tmp.next != null) {
                if (tmp.next.isChecked == false) {
                    int[] ids = {index_id + count, first_id + count, last_id + count, number_id + count, select_id + count, row_id + count};
                    String[] msgs = {"  ", tmp.next.data.getFirstName(), tmp.next.data.getLastName(), tmp.next.data.getPlayerNumber()};
                    addTableRow(ids, msgs);

                }
                count++;
                tmp = tmp.next;


            }


        }

    }

    protected void addTableRow(int[] ids, String[] msg) {
        TableRow tr = new TableRow(getApplicationContext());
        tr.setId(ids[5]);
        tr.setPadding(10, 0, 0, 0);
        for (int i = 0; i < 4; i++) {
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setId(ids[i]);
            lblTmp.setText(msg[i]);
            lblTmp.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            lblTmp.setTextSize(20);
            lblTmp.setPadding(50, 0, 70, 0);
            tr.addView(lblTmp);
        }
        /*
        TextView space = new TextView(getApplicationContext());
        space.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        space.setText("Add:");
        space.setPadding(0,0,0,0);
        tr.addView(space);
        */

        CheckBox cbTmp = new CheckBox(getApplicationContext());
        cbTmp.setId(ids[4]);
        cbTmp.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        cbTmp.setPadding(0, 0, 0, 0);
        cbTmp.setChecked(true);
        addPlayer(ids[4]-select_id,true);
        tr.addView(cbTmp);

        tblAddFromRoster.addView(tr);

        cbTmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                int ref = v.getId() - select_id;
                if (checked) {
                    addPlayer(ref,true);
                    //getPlayer(ref);
                    //printIndex();
                    //LineUp.updateLineUp();
                }else{
                    addPlayer(ref,false);
                    //removePlayer(ref);
                }
            }
        });


    }

    protected void addPlayer(int ref, boolean isChecked){
        PlayerNode tmp = getHead();
        System.out.println(tmp.index+ " COMPARED TO THE REF: "+ref);
        if (tmp.index != ref) {
            while (tmp.index != ref) {
                tmp = tmp.next;
            }
        }
        tmp.isChecked = isChecked;
        System.out.println( tmp.data.getFirstName() + " WAS ADDED TO THE LINEUP");

    }

    protected void removePlayer(int ref){
        printIndex();

        PlayerNode tmp = getStarter();
        System.out.println(tmp.index+" COMPARED WITH THE REF: "+ref);


        //If deleting at head
        if (tmp.index == ref){
            if (tmp.next == null){
                setStarter(null);
            }else{
                setStarter(getStarter().next);
                while(tmp.next != null){
                    tmp.next.index =(tmp.next.index-1);
                    tmp = tmp.next;
                }
            }
            //deleting in the mid /end
        }else{
            System.out.println("CHECKING INDEX OF NEXT NODE: "+tmp.next.index);
            while (tmp.next.index != ref){
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
        printIndex();
    }

    protected void printIndex(){
        if(getStarter() != null) {
            System.out.println("PRINGINT INDEXES");
            PlayerNode tmp = getStarter();
            System.out.println(tmp.data.getFirstName() + ": " + tmp.index);
            while (tmp.next != null) {
                System.out.println(tmp.next.data.getFirstName() + ": " + tmp.next.index);
                tmp = tmp.next;

            }
        }
    }

    protected void getPlayer(int ref) {
        PlayerNode tmp = getHead();
        if (ref == tmp.index) {
            if (getStarter() == null) {
                PlayerNode newNode = new PlayerNode();
                newNode.data = tmp.data;
                newNode.index = lineUpCount;
                setStarter(newNode);
                lineUpCount++;
            } else {
                PlayerNode tmp1 = getStarter();
                while (tmp1.next != null) {
                    tmp1 = tmp1.next;
                }
                PlayerNode newNode = new PlayerNode();
                newNode.data = tmp.data;
                newNode.index = lineUpCount;
                tmp1.next = newNode;
                lineUpCount++;
            }
        } else {
            while (tmp.index != ref) {
                tmp = tmp.next;
            }
            if (getStarter() == null) {
                PlayerNode newNode = new PlayerNode();
                newNode.data = tmp.data;
                newNode.index = lineUpCount;
                setStarter(newNode);
                lineUpCount++;
            } else {
                PlayerNode tmp1 = getStarter();
                while (tmp1.next != null) {
                    tmp1 = tmp1.next;
                }
                PlayerNode newNode = new PlayerNode();
                newNode.data = tmp.data;
                newNode.index = lineUpCount;
                tmp1.next = newNode;
                lineUpCount++;

            }

        }


    }
}
