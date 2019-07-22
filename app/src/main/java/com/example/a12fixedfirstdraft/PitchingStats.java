package com.example.a12fixedfirstdraft;

public class PitchingStats {
    private int BH;
    private int BHR;
    private int BAB;
    private int BK;
    private int BSF;
    private int GB;
    private int PIT;
    private int FB;
    private int BB;
    private int S;
    private int C;
    private double BABIP;
    private double GF;
    private double KBB;
    private double SP;
    private double CP;

    // hits
    public void addBH(){
        BH++;
    }

    // home runs
    public void addBHR(){
        BHR++;
    }

    // at bats
    public void addBAB(){
        BAB++;
    }

    // strikeouts
    public void addBK(){
        BK++;
    }

    // Sac flies
    public void addBSF(){
        BSF++;
    }

    // ground balls
    public void addGB(){
        GB++;
    }

    //pitch count
    public void addPIT(){
        PIT++;
    }

    // fly balls
    public void addFB(){
        FB++;
    }

    // walks
    public void addBB(){
        BB++;
    }

    // Strikes
    public void addS(){
        S++;
    }

    // contact (Including foul ball)
    public void addC(){
        C++;
    }

    public double getBABIP() {
        return ((BH-BHR)/(BAB-BK-BHR+BSF));
    }

    public double getGF() {
        return GB/FB;
    }

    public double getKBB() {
        return BK/BB;
    }

    public double getSP() {
        return S/PIT;
    }

    public double getCP() {
        return C/PIT;
    }
}
