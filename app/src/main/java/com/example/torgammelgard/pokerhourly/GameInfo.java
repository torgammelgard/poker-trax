package com.example.torgammelgard.pokerhourly;

/**
 * Created by Tor on 2014-08-06.
 */
public class GameInfo {
    /*private fields*/
    private String name;

    /*constructor*/
    public GameInfo(String name) {
        this.name = name;
    }

    /*methods*/

    @Override
    public String toString() {
        return name;
    }
}
