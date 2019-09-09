package com.example.a12fixedfirstdraft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_PLAYERS = "players";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "firstNmae";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_PLAYER_NUMBER = "playerNumber";
    private static final String[] KEY_LIST= {KEY_FIRST_NAME,KEY_LAST_NAME,KEY_PLAYER_NUMBER};


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYERS_TABLE = "CREATE TABLE " + TABLE_PLAYERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        for (int i =0; i < KEY_LIST.length; i++){
            CREATE_PLAYERS_TABLE += KEY_LIST[i] + " TEXT";
            if (i < KEY_LIST.length-1){
                CREATE_PLAYERS_TABLE+=",";
            }
        }
        CREATE_PLAYERS_TABLE += ")";
        System.out.println(CREATE_PLAYERS_TABLE);
        /*
        String CREATE_PLAYERS_TABLE = "CREATE TABLE " + TABLE_PLAYERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT," + KEY_PLAYER_NUMBER + " TEXT," + KEY_ISCHECKED + "TEXT,"
                + KEY_POSITION + " TEXT," + KEY_ORDERS + " TEXT"

                +")";
                */
        db.execSQL(CREATE_PLAYERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PLAYERS);

        onCreate(db);
    }

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    void addPlayer(Player player){
        System.out.println("@ADDING PLAYER TO DATABSE <------------------->");
        SQLiteDatabase db = this.getWritableDatabase();
        String [] playerInfo = {player.getFirstName(),player.getLastName(),player.getPlayerNumber()};

        ContentValues values = new ContentValues();
        for (int i = 0; i < KEY_LIST.length; i++){
            values.put(KEY_LIST[i],playerInfo[i]);
        }

        db.insert(TABLE_PLAYERS,null,values);
    }

    public List<Player> getAllPlayers() {
        System.out.println("@GETTING ALL PLAYERS FROM DATABSE <------------------->");
        List<Player> playerList = new ArrayList<Player>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Player player = new Player();
                player.setId(Integer.parseInt(cursor.getString(0)));
                player.setFirstName(cursor.getString(1));
                player.setLastName(cursor.getString(2));
                player.setPlayerNumber(cursor.getString(3));

                // Adding contact to list
                playerList.add(player);
            } while (cursor.moveToNext());
        }

        // return contact list
        return playerList;
    }

    public Player[] getPlayerArray() {
        System.out.println("@GETTING ALL PLAYERS FROM DATABSE <------------------->");
        Player[] players = new Player[getPlayerCount()];
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 0;
        if (cursor.moveToFirst()){
            do {
                Player player = new Player();
                player.setId(Integer.parseInt(cursor.getString(0)));
                player.setFirstName(cursor.getString(1));
                player.setLastName(cursor.getString(2));
                player.setPlayerNumber(cursor.getString(3));
                players[i] = player;
                i++;
            } while (cursor.moveToNext());
        }
        return players;

    }

    Player getPlayer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLAYERS, new String[] { KEY_ID,
                        KEY_FIRST_NAME, KEY_LAST_NAME,KEY_PLAYER_NUMBER }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Player player = new Player();
        player.setId(Integer.parseInt(cursor.getString(0)));
        player.setFirstName(cursor.getString(1));
        player.setLastName(cursor.getString(2));
        player.setPlayerNumber(cursor.getString(3));
        // return contact
        return player;
    }

    public void updatePlayer(Player player, int id){
        String [] playerInfo = {player.getFirstName(),player.getLastName(),player.getPlayerNumber()};
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < playerInfo.length; i ++){
            cv.put(KEY_LIST[i],playerInfo[i]);
        }
        db.update(TABLE_PLAYERS,cv,KEY_ID+" = ?",new String[] {String.valueOf(id)});
    }

    public int getPlayerCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PLAYERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }

    public void deletePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYERS, KEY_ID + " = ?",
                new String[] { String.valueOf(player.getId()) });
        db.close();
    }
}
