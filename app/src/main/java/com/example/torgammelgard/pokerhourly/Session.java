package com.example.torgammelgard.pokerhourly;

/**
 * Created by Tor on 2014-08-06.
 */
public class Session {
    /*private fields */
    private long id;

    private String gameInfo;
    private int hours;
    private int result;

    public void setGameInfo(String gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setResult(int result) {
        this.result = result;
    }

    /*constructors */
    public Session () {}

    public Session (String gameInfo, int hours, int result) {
        this.gameInfo = gameInfo;
        this.hours = hours;
        this.result = result;
    }

    /*methods */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGameInfo() {
        return gameInfo;
    }

    public int getHours() {
        return hours;
    }

    public int getResult() {
        return result;
    }

    @Override
    public String toString() {
        return gameInfo.toString() + ", " + hours + " hours, Result:" + result;
    }
}
