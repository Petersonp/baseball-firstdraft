package com.example.a12fixedfirstdraft;

public class PitchingStats {
    private int BH=0;
    private int BHR=0;
    private int BAB=0;
    private int BK=0;
    private int BSF=0;
    private int GB=0;
    private int PIT=0;
    private int FB=0;
    private int BB=0;
    private int S=0;
    private int C=0;
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

    public String getBABIP() {
        if ((BAB - BK - BHR + BSF) == 0){
            return "--";
        }
        return String.valueOf((double)((BH - BHR)/(BAB - BK - BHR + BSF)));
    }

    public String getGF() {
        if (FB == 0){
            return "--";
        }
        return String.valueOf((double)(GB / FB));
    }

    public String getKBB() {
        if (BB == 0){
            return "--";
        }
        return String.valueOf((double)(BK / BB));
    }

    public String getSP() {
        if (PIT == 0){
            return "--";
        }
        System.out.println("PIT IS: "+PIT+" S IS: "+S);
        System.out.println("S/PIT IS: "+((double)(S/PIT)));
        return String.valueOf((double)S/PIT);
    }

    public String getCP() {
        if (PIT == 0){
            return "--";
        }
        return String.valueOf((double)(C /PIT));
    }

    public String getBH() {
        return String.valueOf(BH);
    }

    public String getBHR() {
        return String.valueOf(BHR);
    }

    public String getBAB() {
        return String.valueOf(BAB);
    }

    public String getBK() {
        return String.valueOf(BK);
    }

    public String getBSF() {
        return String.valueOf(BSF);
    }

    public String getGB() {
        return String.valueOf(GB);
    }

    public String getPIT() {
        return String.valueOf(PIT);
    }

    public String getFB() {
        return String.valueOf(FB);
    }

    public String getBB() {
        return String.valueOf(BB);
    }

    public String getS() {
        System.out.println("GETTING S");
        return String.valueOf(S);
    }

    public String getC() {
        System.out.println("GETTING C");
        return String.valueOf(C);
    }
}
