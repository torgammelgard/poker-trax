package com.example.torgammelgard.pokerhourly;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SessionsDAO {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
        MySQLiteHelper.COLUMN_GAMEINFO, MySQLiteHelper.COLUMN_TIME,
        MySQLiteHelper.COLUMN_RESULT};

    public SessionsDAO (Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<Session>();

        Cursor cursor = database.query("sessions", allColumns, null, null,
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

    private Session cursorToSession(Cursor cursor) {
        Session session = new Session();
        session.setId(cursor.getLong(0));
        session.setGameInfo(cursor.getString(1));
        session.setHours(cursor.getInt(2));
        session.setResult(cursor.getInt(3));
        return session;
    }
}
