package com.example.torgammelgard.pokerhourly;

/**
 * Created by Tor on 2014-08-06.
 */
public class Session {
    /*private fields */
    private GameInfo gameInfo;
    private int hours;
    private int result;

    /*constructor */
    public Session (GameInfo gameInfo, int hours, int result) {
        this.gameInfo = gameInfo;
        this.hours = hours;
        this.result = result;
    }

    /*methods */
    public GameInfo getGameInfo() {
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
