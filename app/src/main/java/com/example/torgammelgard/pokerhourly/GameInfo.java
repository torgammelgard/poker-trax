package com.example.torgammelgard.pokerhourly;

/**
 * Class
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
