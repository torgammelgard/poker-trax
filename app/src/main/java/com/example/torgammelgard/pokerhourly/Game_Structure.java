package com.example.torgammelgard.pokerhourly;

/**
 * TODO: Class header comment.
 */
public class Game_Structure {
    private long id;
    private int small_blind;
    private int big_blind;
    private int ante;

    public int getAnte() {
        return ante;
    }

    public void setAnte(int ante) {
        this.ante = ante;
    }

    public Game_Structure(){}

    public int getSmall_blind() {
        return small_blind;
    }

    public void setSmall_blind(int small_blind) {
        this.small_blind = small_blind;
    }

    public int getBig_blind() {
        return big_blind;
    }

    public void setBig_blind(int big_blind) {
        this.big_blind = big_blind;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String returnString = small_blind + " - " + big_blind;
        return (ante == 0) ? returnString : returnString + " ante " + ante;
    }
}
