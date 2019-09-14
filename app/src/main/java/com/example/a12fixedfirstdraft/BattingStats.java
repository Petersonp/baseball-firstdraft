package com.example.a12fixedfirstdraft;

public class BattingStats {
    private int hits = 0;
    private int AB = 0;
    private int BB = 0;
    private int HBP = 0;
    private int K = 0;
    private int TB = 0;
    private int SF = 0;
    private double BA;
    private double OBP;
    private double BBK;
    private double OPS;

    //sac flies
    public void addSF(){
        setSF(SF + 1);
    }
    public String getSF() { return String.valueOf(SF); }

    public void addHits() {
        setHits(hits + 1);
    }
    public String getHits() { return String.valueOf(hits); }

    // at bats
    public void addAB() {
        setAB(AB + 1);
    }
    public String getAB() { return String.valueOf(AB); }
    // walks
    public void addBB() {
        setBB(BB + 1);
    }
    public String getBB() { return String.valueOf(BB); }

    // hit by pitch
    public void addHBP() {
        setHBP(HBP + 1);
    }

    // strike out
    public void addK() {
        setK(K + 1);
    }

    // total bases
    public void addTB(int i) {
        setTB(TB + i);
    }



    public String getBA() {
        if (AB == 0){
            return "--";
        }
        return String.valueOf((double)(hits/AB));
    }

    public String getOBP() {
        if((AB+BB+ HBP +SF) == 0){
            return "--";
        }
        return String.valueOf((double)((hits+BB+ HBP)/(AB+BB+ HBP +SF)));
    }

    public String getBBK() {
        if (K == 0){
            return "--";
        }
        return String.valueOf((double)(BB/ K));
    }

    public String getOPS() {
        if (AB == 0){
            return "--";
        }
        else if ((AB+BB+ HBP +SF)+(TB /AB)==0){
            return "--";
        }
        return String.valueOf((double)(((hits+BB+ HBP)/(AB+BB+ HBP +SF))+(HBP /AB)));
    }


    // DELETE THESE AFTER TESTING
    public String getHBP() {
        return String.valueOf(HBP);
    }

    public String getK() {
        return String.valueOf(K);
    }

    public String getTB() {
        return String.valueOf(TB);
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setAB(int AB) {
        this.AB = AB;
    }

    public void setBB(int BB) {
        this.BB = BB;
    }

    public void setHBP(int HBP) {
        this.HBP = HBP;
    }

    public void setK(int k) {
        K = k;
    }

    public void setTB(int TB) {
        this.TB = TB;
    }

    public void setSF(int SF) {
        this.SF = SF;
    }
}
