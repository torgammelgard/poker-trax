package se.torgammelgard.pokertrax;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable {

    /*private fields */
    private long id;
    private int game_type_ref;
    private String location;
    private int game_structure_ref;
    private int duration;                       //in minutes
    private Date date;
    private int result;
    private String game_notes;

    /*constructors */
    public Session () {
        this.id = 0;
        date = new Date();
    }

    /*getters and setters */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getGame_type_ref() {
        return game_type_ref;
    }

    public void setGame_type_ref(int game_type_ref) {
        this.game_type_ref = game_type_ref;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGame_structure_ref() {
        return game_structure_ref;
    }

    public void setGame_structure_ref(int game_structure_ref) {
        this.game_structure_ref = game_structure_ref;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getGame_notes() {
        return game_notes;
    }

    public void setGame_notes(String game_notes) {
        this.game_notes = game_notes;
    }

    @Override
    public String toString() {
        return ""; //TODO
    }
}
