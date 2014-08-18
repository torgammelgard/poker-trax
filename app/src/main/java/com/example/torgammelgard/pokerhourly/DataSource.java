package com.example.torgammelgard.pokerhourly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class DataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private String[] allGameTypes =  {Game_TypeTable.COLUMN_ID, Game_TypeTable.COLUMN_NAME};
    private String[] allGameStructures = {Game_StructureTable.COLUMN_ID,
            Game_StructureTable.COLUMN_SMALLBLIND, Game_StructureTable.COLUMN_BIGBLIND,
            Game_StructureTable.COLUMN_ANTE};
    private String[] allColumns = {SessionTable.COLUMN_ID,
            SessionTable.COLUMN_GAMETYPE, SessionTable.COLUMN_LOCATION,
            SessionTable.COLUMN_GAMESTRUCTURE,
            SessionTable.COLUMN_DURATION, SessionTable.COLUMN_DATE,
            SessionTable.COLUMN_RESULT, SessionTable.COLUMN_GAMENOTES};


    public DataSource(Context context) {
        dbHelper = MySQLiteHelper.getInstance(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        //database.close();
        dbHelper.close();
    }

    public ArrayList<String> getAllGameTypes() {
        ArrayList<String> game_types = new ArrayList<String>();
        Cursor cursor = database.query(Game_TypeTable.TABLE_GAMETYPE, allGameTypes,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Game_type game_type = cursorToGameType(cursor);
            game_types.add(game_type.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return game_types;
    }

    public ArrayList<String> getAllGameStructures() {
        ArrayList<String> game_structures = new ArrayList<String>();
        Cursor cursor = database.query(Game_StructureTable.TABLE_STRUCTURETABLE, allGameStructures,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Game_Structure game_structure = cursorToGameStructure(cursor);
            game_structures.add(game_structure.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return game_structures;
    }

    public ArrayList<Session> getAllSessions() {
        ArrayList<Session> sessions = new ArrayList<Session>();

        Cursor cursor = database.query(SessionTable.TABLE_SESSIONS, allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Session session = cursorToSession(cursor);
            sessions.add(session);
            cursor.moveToNext();
        }
        cursor.close();
        return sessions;
    }

    public Session addSession(Session session) {
        ContentValues values = new ContentValues();
        values.put(SessionTable.COLUMN_GAMETYPE, session.getGame_type_ref());
        values.put(SessionTable.COLUMN_LOCATION, session.getLocation());
        values.put(SessionTable.COLUMN_GAMESTRUCTURE, session.getGame_structure_ref());
        values.put(SessionTable.COLUMN_DURATION, session.getDuration());
        values.put(SessionTable.COLUMN_DATE, formatter.format(session.getDate())); //TODO checkDate
        values.put(SessionTable.COLUMN_RESULT, session.getResult());
        values.put(SessionTable.COLUMN_GAMENOTES, session.getGame_notes());

        long id = database.insert(SessionTable.TABLE_SESSIONS, null, values);
        Cursor cursor = database.query(SessionTable.TABLE_SESSIONS,
                allColumns,
                SessionTable.COLUMN_ID + " = " + id,
                null, null, null, null);
        cursor.moveToFirst();
        Session newSession = cursorToSession(cursor);
        cursor.close();
        return newSession;
    }

    public void deleteSession(long sessionID) {
        //long id = session.getId();
        database.delete(SessionTable.TABLE_SESSIONS,
                SessionTable.COLUMN_ID + " = " + sessionID,
                null);
    }

    private Game_type cursorToGameType(Cursor cursor) {
        Game_type game_type = new Game_type();
        game_type.setId(cursor.getLong(0));
        game_type.setType(cursor.getString(1));
        return game_type;
    }

    private Game_Structure cursorToGameStructure(Cursor cursor) {
        Game_Structure game_structure = new Game_Structure();
        game_structure.setId(cursor.getLong(0));
        game_structure.setSmall_blind(cursor.getInt(1));
        game_structure.setBig_blind(cursor.getInt(2));
        game_structure.setAnte(cursor.getInt(3));
        return game_structure;
    }

    private Session cursorToSession(Cursor cursor) {
        Session session = new Session();
        session.setId(cursor.getLong(0));
        session.setGame_type_ref(cursor.getInt(1));
        session.setLocation(cursor.getString(2));
        session.setGame_structure_ref(cursor.getInt(3));
        session.setDuration(cursor.getInt(4));
        session.setDate(formatter.parse(cursor.getString(5), new ParsePosition(0))); //TODO checkDate
        session.setResult(cursor.getInt(6));
        session.setGame_notes(cursor.getString(7));
        return session;
    }
}
