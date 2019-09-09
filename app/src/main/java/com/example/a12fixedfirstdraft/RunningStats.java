package com.example.a12fixedfirstdraft;

public class RunningStats {
    private int SA = 0;
    private int SS = 0;
    private double SP;

    public void addSS(){
        SS++;
    }
    public void addSA(){
        SA++;
    }
    public String getSS(){
        return String.valueOf(SS);
    }
    public String getSA(){
        return String.valueOf(SA);
    }
    public String getSP(){
        if (SA == 0){
            return "--";
        }
        return String.valueOf((double)(SA/SS));
    }
}
