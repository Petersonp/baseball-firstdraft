package com.example.a12fixedfirstdraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class LineUp extends GameActivity {
    public static final int REQUEST_CODE_GETMESSAGE_ADDFROMROSTER = 0;
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.LineUp - Return Message";
    public static final int REQUEST_CODE_GETMESSAGE_HOMEOREAWAY = 9;
    Button btnAdd;
    Button btnBack;
    Button btnContinue;
    Button btnPrint;

    TextView lblError;

    TableLayout tblLineUp;

    String[] positions = {"P","C","FB","SB","TB","SS","LF","CF","RF"};

    int count = 0;
    int index_id = 3083;
    int first_id = 3183;
    int last_id = 3283;
    int number_id = 3383;
    int pos_id = 3483;
    int remove_id = 3583;
    int row_id = 3683;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lineup_layout);

        //setting Views
        System.out.println(count+" THIS IS COUNT AT THE START");
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        tblLineUp = (TableLayout) findViewById(R.id.tblStats);
        lblError = (TextView) findViewById(R.id.lblError);
        btnPrint = (Button) findViewById(R.id.btnPrint);

        //Header
        TableRow head = new TableRow(getApplicationContext());
        head.setPadding(10,0,0,0);

        String[] labels = {"Order","First Name","Last Name","#","Pos.","Remove"};
        for (int i = 0 ; i <labels.length;i++){
            TextView headTmp = new TextView(getApplicationContext());
            headTmp.setText(labels[i]);
            headTmp.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            headTmp.setTextSize(20);
            headTmp.setPadding(50,0,50,0);
            head.addView(headTmp);
        }
        tblLineUp.addView(head);

        //Add from Roster button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoster();

            }
        });

        //Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLineUp();
                printIndex();
                //setValues();
                finish();
            }
        });

        //print button
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printOrder();
            }
        });

        //Continue Button
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 9){
                    Intent i = new Intent(getApplicationContext(),HomeOrAway.class);
                    startActivityForResult(i, REQUEST_CODE_GETMESSAGE_HOMEOREAWAY);

                } else if (count > 9){
                    lblError.setText("Too many Players!");
                } else if (count < 9){
                    lblError.setText("Not enough Players!");
                }
            }
        });

        updateLineUp();
        setValues();

    }
    // can delete
    protected void printOrder(){
        PlayerNode tmp = getHead();
        if (tmp.isChecked){
            System.out.println(tmp.data.getFirstName()+" "+tmp.data.getFirstName()+" @"+tmp.order+" @"+tmp.positon);
        }
        while(tmp.next != null){
            System.out.println("CHECKING IF: "+tmp.next.data.getFirstName()+" IS NULL");
            if (tmp.next.isChecked){
                System.out.println(tmp.next.data.getFirstName()+" "+tmp.next.data.getLastName()+" @"+tmp.next.order+" @"+tmp.next.positon);
            }
            tmp = tmp.next;
        }
        System.out.println(tmp.next+" IS NULL");
    }

    protected void createLineUp(){
        int lineUpCount = 1;
        PlayerNode tmp1 = getStarter();
        while (lineUpCount <= 9) {
            PlayerNode tmp = getHead();

            while (tmp.order != lineUpCount) {
                tmp = tmp.next;
            }
            System.out.println("THE ORDER OF: " + tmp.data.getFirstName() + ": " + tmp.order + " IS EQUAL TO " + lineUpCount);
            lineUpCount++;
            if (getStarter() == null) {
                setStarter(tmp);
                System.out.println("THE STARTER IS: "+getStarter().data.getFirstName()+" "+getStarter().data.getLastName()+" "+getStarter().data.getPlayerNumber());
                tmp1 = getStarter();
            } else {
                System.out.println("ELSE");

                tmp1.next = tmp;
                tmp1 = tmp1.next;

            }
        }

    }

    protected void createLineUp1(){
        for (int i = 1; i < 10;i++){
            PlayerNode tmp = getHead();
            System.out.println("COMPARING: "+tmp.order+" TO:"+i);
            while (tmp.order != i){
                System.out.println("COMPARING: "+tmp.order+" TO:"+i);
                tmp =tmp.next;
            }
            if (getStarter() == null){
                setStarter(tmp);
            }else{
                PlayerNode tmp2 = getStarter();
                while (tmp2.next!= null){
                    System.out.println("COMPARING: "+tmp2.next.data.getFirstName()+" TO NULL");
                    tmp2 = tmp2.next;
                }
                tmp2.next = tmp;
            }

        }
    }

    protected void setValues(){
        int valueCount = 0;
        PlayerNode tmp = getHead();
        if (tmp != null){
            if (tmp.isChecked){
                Spinner spnIndex = (Spinner) findViewById(index_id);
                Spinner spnPos = (Spinner) findViewById(pos_id);
                spnIndex.setSelection(getSpinnerIndex(tmp.index));
                spnPos.setSelection(getSpinnerPos(tmp.positon));

            }

            while(tmp.next!= null){
                if (tmp.next.isChecked){
                    Spinner spnIndex = (Spinner) findViewById(index_id+valueCount);
                    Spinner spnPos = (Spinner) findViewById(pos_id+valueCount);
                    spnIndex.setSelection(getSpinnerIndex(tmp.next.index));
                    spnPos.setSelection(getSpinnerPos(tmp.next.positon));
                }
                tmp = tmp.next;
                valueCount++;
            }

        }
    }

    protected void printIndex(){
        if(getHead() != null) {
            System.out.println("PRINGINT INDEXES");
            PlayerNode tmp = getHead();
            System.out.println(tmp.data.getFirstName() + ": " + tmp.index+": "+ tmp.order+": "+tmp.positon);
            while (tmp.next != null) {
                System.out.println(tmp.next.data.getFirstName() + ": " + tmp.next.index+": "+tmp.next.order+": "+tmp.next.positon);
                tmp = tmp.next;

            }
        }
    }

    protected void clearLineUp(){
        for (int i = 0; i < count +1; i ++){
            TableRow trTmp = (TableRow) findViewById(row_id+i);
            tblLineUp.removeView(trTmp);
        }
        count = 0;
    }

    protected void updateLineUp(){
        printIndex();
        PlayerNode tmp = getHead();
        if (tmp != null){
            if (tmp.isChecked){
                int[] ids = {index_id+count, first_id+count, last_id+count, number_id+count, pos_id+count,remove_id+count, row_id+count};
                String[] msgs = {String.valueOf(count+1),tmp.data.getFirstName(),tmp.data.getLastName(),tmp.data.getPlayerNumber()};
                addTableRow(ids,msgs);
                count++;
            }

            while (tmp.next != null){
                if (tmp.next.isChecked){
                    int[] ids = {index_id+count, first_id+count, last_id+count, number_id+count, pos_id+count,remove_id+count, row_id+count};
                    String[] msgs = {String.valueOf(count+1),tmp.next.data.getFirstName(),tmp.next.data.getLastName(),tmp.next.data.getPlayerNumber()};
                    addTableRow(ids,msgs);
                    count++;
                }
                tmp=tmp.next;
            }
        }
        printIndex();
    }


    protected void openRoster(){
        Intent i = new Intent(getApplicationContext(),AddFromRoster.class);
        //startActivity(i);
        startActivityForResult(i,REQUEST_CODE_GETMESSAGE_ADDFROMROSTER);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode+" IS THE REQUESTCODE FOR LINEUP");
        if (requestCode == REQUEST_CODE_GETMESSAGE_HOMEOREAWAY){
            if (Activity.RESULT_OK == resultCode){
                System.out.println("RETURNING HOMEORAWAY");
                Intent i = new Intent();
                String result = HomeOrAway.getResultKeyMessage(data);
                System.out.println(result+ " IS THE RESULT FOR LINEUP");
                i.putExtra(RESULT_KEY_MESSAGE,result);
                setResult(Activity.RESULT_OK,i);
                //setValues();
                //printOrder();
                createLineUp();
                //printLineUp();
                finish();
            }
        }
        switch (requestCode){
            case REQUEST_CODE_GETMESSAGE_ADDFROMROSTER:
                if(resultCode == Activity.RESULT_OK){
                    clearLineUp();
                    updateLineUp();


                }
        }
    }

    public static String getResultKeyMessage(Intent intent){
        return intent.getStringExtra(RESULT_KEY_MESSAGE);
    }

    protected int getSpinnerIndex(int index){
        System.out.println("CHECKING SPINNERINDEX");
        for (int i =0; i < 10;i++){
            if (index == i+1){
                return i;
            }
        }
        System.out.println("NONE FOR SPINNERINDEX");
        return 0;
    }

    protected int getSpinnerPos(String pos){
        System.out.println("CHECKING SPINNERPOS");
        for (int i =0; i < positions.length;i++){
            if (pos.equals(positions[i].toString())){
                System.out.println(pos+" == "+ positions[i]);
                return i;
            }
        }
        System.out.println("NONE FOR SPINNERPOS");
        return 0;
    }

    protected void addTableRow(int[] ids, String[] msg){
        TableRow tr = new TableRow(getApplicationContext());
        tr.setId(ids[6]);
        final Spinner spnIndex = new Spinner(getApplicationContext());
        spnIndex.setId(ids[0]);
        spnIndex.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        ArrayList<String> indexArray = new ArrayList<String>();
        for (int i = 1; i < 10;i++){
            indexArray.add(String.valueOf(i));
        }
        ArrayAdapter<String> spnIndexAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, indexArray);

        spnIndex.setAdapter(spnIndexAdapter);
        spnIndex.setSelection(count);

        tr.addView(spnIndex);

        for (int i =1; i <4;i++){
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setId(ids[i]);
            lblTmp.setText(msg[i]);
            lblTmp.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            lblTmp.setTextSize(20);
            lblTmp.setPadding(40,0,55,0);
            tr.addView(lblTmp);
        }

        final Spinner spnPos = new Spinner(getApplicationContext());
        spnPos.setId(ids[4]);
        spnPos.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        ArrayList<String> posArray = new ArrayList<String>();
        for (int i = 0; i < positions.length;i++){
            posArray.add(positions[i]);
        }
        ArrayAdapter<String> spnPosAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, posArray);
        spnPos.setAdapter(spnPosAdapter);
        spnPos.setSelection(count);

        tr.addView(spnPos);

        Button btnRemove = new Button(getApplicationContext());
        btnRemove.setId(ids[5]);
        btnRemove.setText("Remove");
        btnRemove.setPadding(20,0,20,0);
        tr.addView(btnRemove);

        tblLineUp.addView(tr);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlayerNode(v);
                clearLineUp();
                updateLineUp();
            }
        });

        spnIndex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int ref = parent.getId()-index_id;
                PlayerNode tmp = getHead();
                for (int i = 0; i <ref ; i ++){
                    tmp = tmp.next;
                }
                System.out.println(spnIndex.getItemAtPosition(position).toString() + " IS THE INDEX FOR: "+ tmp.data.getFirstName());
                tmp.order = Integer.valueOf(spnIndex.getItemAtPosition(position).toString());
                System.out.println(tmp.order);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        spnPos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int ref = parent.getId()-pos_id;
                PlayerNode tmp = getHead();
                for (int i = 0; i <ref ; i ++){
                    tmp = tmp.next;

                }
                System.out.println(spnPos.getItemAtPosition(position).toString()+ " IS THE POSITION FOR: "+ tmp.data.getFirstName());
                tmp.positon = spnPos.getItemAtPosition(position).toString();
                System.out.println(tmp.positon);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

    }

    protected void removePlayerNode(View v){
        int ref = v.getId()-remove_id;
        System.out.println(ref+" THIS IS REF");
        int removeCount = 0;
        TableRow tr = (TableRow) findViewById(row_id+ref);
        PlayerNode tmp = getHead();
        if (tmp.isChecked){
            if (removeCount == ref) {
                tmp.isChecked = false;
            }
            removeCount++;
        }
        while (tmp.next != null){
            if (tmp.next.isChecked){
                if (removeCount == ref) {
                    tmp.next.isChecked = false;
                }
                removeCount++;
            }
            tmp = tmp.next;
        }

    }

}
