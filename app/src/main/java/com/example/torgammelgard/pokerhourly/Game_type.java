package com.example.torgammelgard.pokerhourly;

/**
 * Game_type refers to table game_type , for example NL, PL, Limit
 */
public class Game_type {
    private long id;             //refers to id in table game_type
    private String type;

    public Game_type() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return type;
    }
}
