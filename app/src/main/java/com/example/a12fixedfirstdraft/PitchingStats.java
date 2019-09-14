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
        setBH(BH + 1);
    }

    // home runs
    public void addBHR(){
        setBHR(BHR + 1);
    }

    // at bats
    public void addBAB(){
        setBAB(BAB + 1);
    }

    // strikeouts
    public void addBK(){
        setBK(BK + 1);
    }

    // Sac flies
    public void addBSF(){
        setBSF(BSF + 1);
    }

    // ground balls
    public void addGB(){
        setGB(GB + 1);
    }

    //pitch count
    public void addPIT(){
        setPIT(PIT + 1);
    }

    // fly balls
    public void addFB(){
        setFB(FB + 1);
    }

    // walks
    public void addBB(){
        setBB(BB + 1);
    }

    // Strikes
    public void addS(){
        setS(S + 1);
    }

    // contact (Including foul ball)
    public void addC(){
        setC(C + 1);
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
        return String.valueOf(S);
    }

    public String getC() {
        return String.valueOf(C);
    }

    public void setBH(int BH) {
        this.BH = BH;
    }

    public void setBHR(int BHR) {
        this.BHR = BHR;
    }

    public void setBAB(int BAB) {
        this.BAB = BAB;
    }

    public void setBK(int BK) {
        this.BK = BK;
    }

    public void setBSF(int BSF) {
        this.BSF = BSF;
    }

    public void setGB(int GB) {
        this.GB = GB;
    }

    public void setPIT(int PIT) {
        this.PIT = PIT;
    }

    public void setFB(int FB) {
        this.FB = FB;
    }

    public void setBB(int BB) {
        this.BB = BB;
    }

    public void setS(int s) {
        S = s;
    }

    public void setC(int c) {
        C = c;
    }
}
