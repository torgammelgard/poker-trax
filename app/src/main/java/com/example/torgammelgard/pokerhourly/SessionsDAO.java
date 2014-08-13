package com.example.torgammelgard.pokerhourly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SessionsDAO {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
        MySQLiteHelper.COLUMN_GAMEINFO, MySQLiteHelper.COLUMN_TIME,
        MySQLiteHelper.COLUMN_RESULT};

    public SessionsDAO (Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public ArrayList<Session> getAllSessions() {
        ArrayList<Session> sessions = new ArrayList<Session>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SESSIONS, allColumns, null, null,
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
        values.put(MySQLiteHelper.COLUMN_GAMEINFO, session.getGameInfo());
        values.put(MySQLiteHelper.COLUMN_TIME, session.getHours());
        values.put(MySQLiteHelper.COLUMN_RESULT, session.getResult());

        long id = database.insert(MySQLiteHelper.TABLE_SESSIONS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SESSIONS,
                allColumns,
                MySQLiteHelper.COLUMN_ID + " = " + id,
                null, null, null, null);
        cursor.moveToFirst();
        Session newSession = cursorToSession(cursor);
        cursor.close();
        return  newSession;
    }

    public void deleteSession(long sessionID) {
        //long id = session.getId();
        database.delete(MySQLiteHelper.TABLE_SESSIONS,
                MySQLiteHelper.COLUMN_ID + " = " + sessionID,
                null);
    }

    private Session cursorToSession(Cursor cursor) {
        Session session = new Session();
        session.setId(cursor.getLong(0));
        session.setGameInfo(cursor.getString(1));
        session.setHours(cursor.getInt(2));
        session.setResult(cursor.getInt(3));
        return session;
    }
}
