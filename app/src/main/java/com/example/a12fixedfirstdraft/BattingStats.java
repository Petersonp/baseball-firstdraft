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

    public void addSF(){
        SF++;
    }

    public void addHits() {
        hits++;
    }

    // at bats
    public void addAB() {
        AB++;
    }

    // walks
    public void addBB() {
        BB++;
    }

    // hit by pitch
    public void addHBP() {
        HBP++;
    }

    // strike out
    public void addK() {
        K++;
    }

    // total bases
    public void addTB() {
        TB++;
    }

    public double getBA() {
        return hits/AB;
    }

    public double getOBP() {
        return (hits+BB+HBP)/(AB+BB+HBP+SF);
    }

    public double getBBK() {
        return BB/K;
    }

    public double getOPS() {
        return ((hits+BB+HBP)/(AB+BB+HBP+SF))+(TB/AB);
    }
}
