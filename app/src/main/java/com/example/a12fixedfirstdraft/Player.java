package com.example.a12fixedfirstdraft;

public class Player {
    private String firstName;
    private String lastName;
    private String playerNumber;
    private int gamesPlayed;
    //RunningStats runningStats;
    BattingStats battingStats;
    //FieldingStats fieldingStats;
    PitchingStats pitchingStats;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(String playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void addGamesPlayed() {
        gamesPlayed++;
    }
}
