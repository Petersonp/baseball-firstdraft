package com.example.a12fixedfirstdraft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_PLAYERS = "players";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "firstNmae";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_PLAYER_NUMBER = "playerNumber";

    //Pitching stats:
    private static final String KEY_BH = "BH";
    private static final String KEY_BHR = "BHR";
    private static final String KEY_BAB = "BAB";
    private static final String KEY_BK = "BK";
    private static final String KEY_BSF = "BSF";
    private static final String KEY_GB = "GB";
    private static final String KEY_PIT = "PIT";
    private static final String KEY_FB = "FB";
    private static final String KEY_BB = "BB";
    private static final String KEY_S = "S";
    private static final String KEY_C = "C";

    //Batting stats:
    private static final String KEY_HITS = "hits";
    private static final String KEY_AB = "AB";
    private static final String KEY_BBB = "BBB";
    private static final String KEY_HBP = "HBP";
    private static final String KEY_K = "K";
    private static final String KEY_TB = "TB";
    private static final String KEY_SF = "SF";

    private static final String[] KEY_LIST= {KEY_FIRST_NAME,KEY_LAST_NAME,KEY_PLAYER_NUMBER,
            KEY_BH,KEY_BHR,KEY_BAB,KEY_BK,KEY_BSF,KEY_GB,KEY_PIT,KEY_FB,KEY_BB,KEY_S,KEY_C,
            KEY_HITS,KEY_AB,KEY_BBB,KEY_HBP,KEY_K,KEY_TB,KEY_SF};
    private static final String[] KEY_PITCHING= {KEY_BH,KEY_BHR,KEY_BAB,KEY_BK,KEY_BSF,KEY_GB,KEY_PIT,KEY_FB,KEY_BB,KEY_S,KEY_C};
    private static final String[] KEY_BATTING= {KEY_HITS,KEY_AB,KEY_BBB,KEY_HBP,KEY_K,KEY_TB,KEY_SF};


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
        PitchingStats ps = player.pitchingStats;
        BattingStats bs = player.battingStats;
        String [] playerInfo = {player.getFirstName(),player.getLastName(),player.getPlayerNumber(),
        ps.getBH(),ps.getBHR(),ps.getBAB(),ps.getBK(),ps.getBSF(),ps.getGB(),ps.getPIT(),ps.getFB(),ps.getBB(),ps.getS(),ps.getC(),
        bs.getHits(),bs.getAB(),bs.getBB(),bs.getHBP(),bs.getK(),bs.getTB(),bs.getSF()};

        ContentValues values = new ContentValues();
        for (int i = 0; i < KEY_LIST.length; i++){
            System.out.println("INSERTING: "+KEY_LIST[i]+" : "+playerInfo[i]);
            values.put(KEY_LIST[i],playerInfo[i]);
        }

        db.insert(TABLE_PLAYERS,null,values);
    }

    public List<PitchingStats> getAllPS(){
        List<PitchingStats> psList = new ArrayList<PitchingStats>();
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PitchingStats ps = new PitchingStats();
                ps.setBH(Integer.parseInt(cursor.getString(4)));
                ps.setBHR(Integer.parseInt(cursor.getString(5)));
                ps.setBAB(Integer.parseInt(cursor.getString(6)));
                ps.setBK(Integer.parseInt(cursor.getString(7)));
                ps.setBSF(Integer.parseInt(cursor.getString(8)));
                ps.setGB(Integer.parseInt(cursor.getString(9)));
                ps.setPIT(Integer.parseInt(cursor.getString(10)));
                ps.setFB(Integer.parseInt(cursor.getString(11)));
                ps.setBB(Integer.parseInt(cursor.getString(12)));
                ps.setS(Integer.parseInt(cursor.getString(13)));
                ps.setC(Integer.parseInt(cursor.getString(14)));
                // Adding contact to list
                psList.add(ps);
            } while (cursor.moveToNext());
        }

        // return contact list
        return psList;
    }

    public List<BattingStats> getAllBS(){
        List<BattingStats> bsList = new ArrayList<BattingStats>();
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                BattingStats bs = new BattingStats();
                bs.setHits(Integer.parseInt(cursor.getString(15)));
                bs.setAB(Integer.parseInt(cursor.getString(16)));
                bs.setBB(Integer.parseInt(cursor.getString(17)));
                bs.setHBP(Integer.parseInt(cursor.getString(18)));
                bs.setK(Integer.parseInt(cursor.getString(19)));
                bs.setTB(Integer.parseInt(cursor.getString(20)));
                bs.setSF(Integer.parseInt(cursor.getString(21)));
                // Adding contact to list
                bsList.add(bs);
            } while (cursor.moveToNext());
        }

        // return contact list
        return bsList;
    }

    public List<Player> getAllPlayers() {
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
                System.out.println("ID OF: "+cursor.getString(1)+" IS: "+cursor.getString(0));
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
