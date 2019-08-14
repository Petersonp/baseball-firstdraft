package com.example.a12fixedfirstdraft;

public class BattingStats {
    private int hits;
    private int AB;
    private int BB;
    private int HBP;
    private int K;
    private int TB;
    private int SF;
    private double BA;
    private double OBP;
    private double BBK;
    private double OPS;

    //sac flies
    public void addSF(){
        SF++;
    }
    public String getSF() { return String.valueOf(SF); }

    public void addHits() {
        hits++;
    }
    public String getHits() { return String.valueOf(hits); }

    // at bats
    public void addAB() {
        AB++;
    }
    public String getAB() { return String.valueOf(AB); }
    // walks
    public void addBB() {
        BB++;
    }
    public String getBB() { return String.valueOf(BB); }

    // hit by pitch
    public void addHBP() {
        HBP++;
    }

    // strike out
    public void addK() {
        K++;
    }

    // total bases
    public void addTB(int i) {
        TB+=i;
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
}
