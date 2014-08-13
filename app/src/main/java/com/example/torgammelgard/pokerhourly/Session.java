package com.example.torgammelgard.pokerhourly;

public class Session {
    /*private fields */
    private long id;
    private String gameInfo;
    private int hours;
    private int result;

    /*constructors */
    public Session () {
        this.id = 0;
        this.gameInfo = "";
        this.hours = 0;
        this.result = 0;
    }

    public Session (String gameInfo, int hours, int result) {
        this.id = 0;
        this.gameInfo = gameInfo;
        this.hours = hours;
        this.result = result;
    }

    /*getters and setters */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(String gameInfo) {
        this.gameInfo = gameInfo;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return gameInfo.toString() + ", " + hours + " hours, Result:" + result;
    }
}
