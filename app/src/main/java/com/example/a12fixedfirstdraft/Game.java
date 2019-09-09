package com.example.a12fixedfirstdraft;

public class Game {
    private PlayerNode roster;
    private PlayerNode lineup;
    private PlayerNode otherLineup;

    private PlayerNode[] rosterArray;
    private PlayerNode[] lineupArray;
    private PlayerNode[] otherLineUpArray;

    private int strikes;
    private int balls;
    private int inning;
    private int outs;

    private PlayerNode onDeck;
    private PlayerNode batter;
    private PlayerNode fbRunner;
    private PlayerNode sbRunner;
    private PlayerNode tbRunner;


    private PlayerNode pitcher;
    private PlayerNode catcher;
    private PlayerNode firstBase;
    private PlayerNode secondBase;
    private PlayerNode thirdBase;
    private PlayerNode shortStop;
    private PlayerNode leftField;
    private PlayerNode centerField;
    private PlayerNode rightField;

    public PlayerNode getRoster() {
        return roster;
    }

    public void setRoster(PlayerNode roster) {
        this.roster = roster;
    }

    public PlayerNode getLineup() {
        return lineup;
    }

    public void setLineup(PlayerNode lineup) {
        this.lineup = lineup;
    }

    public PlayerNode getOtherLineup() {
        return otherLineup;
    }

    public void setOtherLineup(PlayerNode otherLineup) {
        this.otherLineup = otherLineup;
    }

    public int getStrikes() {
        return strikes;
    }

    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public int getInning() {
        return inning;
    }

    public void setInning(int inning) {
        this.inning = inning;
    }

    public int getOuts() {
        return outs;
    }

    public void setOuts(int outs) {
        this.outs = outs;
    }

    public PlayerNode getOnDeck() {
        return onDeck;
    }

    public void setOnDeck(PlayerNode onDeck) {
        this.onDeck = onDeck;
    }

    public PlayerNode getBatter() {
        return batter;
    }

    public void setBatter(PlayerNode batter) {
        this.batter = batter;
    }

    public PlayerNode getFbRunner() {
        return fbRunner;
    }

    public void setFbRunner(PlayerNode fbRunner) {
        this.fbRunner = fbRunner;
    }

    public PlayerNode getSbRunner() {
        return sbRunner;
    }

    public void setSbRunner(PlayerNode sbRunner) {
        this.sbRunner = sbRunner;
    }

    public PlayerNode getTbRunner() {
        return tbRunner;
    }

    public void setTbRunner(PlayerNode tbRunner) {
        this.tbRunner = tbRunner;
    }

    public PlayerNode getPitcher() {
        return pitcher;
    }

    public void setPitcher(PlayerNode pitcher) {
        this.pitcher = pitcher;
    }

    public PlayerNode getCatcher() {
        return catcher;
    }

    public void setCatcher(PlayerNode catcher) {
        this.catcher = catcher;
    }

    public PlayerNode getFirstBase() {
        return firstBase;
    }

    public void setFirstBase(PlayerNode firstBase) {
        this.firstBase = firstBase;
    }

    public PlayerNode getSecondBase() {
        return secondBase;
    }

    public void setSecondBase(PlayerNode secondBase) {
        this.secondBase = secondBase;
    }

    public PlayerNode getThirdBase() {
        return thirdBase;
    }

    public void setThirdBase(PlayerNode thirdBase) {
        this.thirdBase = thirdBase;
    }

    public PlayerNode getShortStop() {
        return shortStop;
    }

    public void setShortStop(PlayerNode shortStop) {
        this.shortStop = shortStop;
    }

    public PlayerNode getLeftField() {
        return leftField;
    }

    public void setLeftField(PlayerNode leftField) {
        this.leftField = leftField;
    }

    public PlayerNode getCenterField() {
        return centerField;
    }

    public void setCenterField(PlayerNode centerField) {
        this.centerField = centerField;
    }

    public PlayerNode getRightField() {
        return rightField;
    }

    public void setRightField(PlayerNode rightField) {
        this.rightField = rightField;
    }

    public PlayerNode[] getRosterArray() {
        return rosterArray;
    }

    public void setRosterArray(PlayerNode[] rosterArray) {
        this.rosterArray = rosterArray;
    }

    public PlayerNode[] getOtherLineUpArray() {
        return otherLineUpArray;
    }

    public void setOtherLineUpArray(PlayerNode[] otherLineUpArray) {
        this.otherLineUpArray = otherLineUpArray;
    }

    public PlayerNode[] getLineupArray() {
        return lineupArray;
    }

    public void setLineupArray(PlayerNode[] lineupArray) {
        this.lineupArray = lineupArray;
    }
}
