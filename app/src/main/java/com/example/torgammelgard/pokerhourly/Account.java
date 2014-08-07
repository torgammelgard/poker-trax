package com.example.torgammelgard.pokerhourly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tor on 2014-08-06.
 */
public class Account {

    /*private fields*/
    private static int counter = 0;
    private String userName;
    private List<Session> sessions;     /*a list for now, maybe SQLite later on*/

    /*constructor*/
    public Account () {
        sessions = new ArrayList<Session>();
    }

    /*methods*/
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return userName;
    }
}
