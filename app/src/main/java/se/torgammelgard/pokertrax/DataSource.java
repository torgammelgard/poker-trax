package se.torgammelgard.pokertrax;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import se.torgammelgard.pokertrax.Database.Game_StructureTable;
import se.torgammelgard.pokertrax.Database.Game_TypeTable;
import se.torgammelgard.pokertrax.Database.MySQLiteHelper;
import se.torgammelgard.pokertrax.Database.SessionTable;
import se.torgammelgard.pokertrax.model.Session;

public class DataSource {
    private static final String LOG = "DataSource";
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private String[] allColumns = {
            SessionTable.COLUMN_ID,
            SessionTable.COLUMN_GAMETYPE,
            SessionTable.COLUMN_LOCATION,
            SessionTable.COLUMN_GAMESTRUCTURE,
            SessionTable.COLUMN_DURATION,
            SessionTable.COLUMN_DATE,
            SessionTable.COLUMN_RESULT,
            SessionTable.COLUMN_GAMENOTES};

    public DataSource(Context context) {
        dbHelper = MySQLiteHelper.getInstance(context);
    }

    public void close() {
        database.close();
    }

    public int getEntriesCount() {
        int count = 0;
        try {
            database = dbHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT count(*) FROM " + SessionTable.TABLE_SESSION, null);
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        } catch (SQLiteException e) {
            Log.d(LOG, "Failed to connect to database", e);
        }
        return count;
    }

    /* Returns total game play in minutes*/
    public int getTotalTimePlayed() {
        int total = 0;
        try {
            database = dbHelper.getReadableDatabase();
            Cursor cursor = database.query(SessionTable.TABLE_SESSION,
                    new String[]{"TOTAL(" + SessionTable.COLUMN_DURATION + ")"},
                    null, null, null, null, null);
            cursor.moveToFirst();
            total = cursor.getInt(0);
            cursor.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
        return total;
    }

    public double getAvgbbPH() {
        double avg = 0.0;
        try {
            database = dbHelper.getReadableDatabase();
            Cursor sessionCursor = database.rawQuery(
                    "SELECT total(CAST (" + SessionTable.COLUMN_RESULT + " AS float)/(SELECT " +
                            Game_StructureTable.COLUMN_BIGBLIND + " FROM " +
                            Game_StructureTable.TABLE_GAME_STRUCTURE +
                            " WHERE " + Game_StructureTable.COLUMN_ID + " = " +
                            SessionTable.COLUMN_GAMESTRUCTURE + "))/total(CAST (" + SessionTable.COLUMN_DURATION +
                            " AS float)/?) FROM " + SessionTable.TABLE_SESSION,
                    new String[]{String.valueOf(60)}
            );
            sessionCursor.moveToFirst();
            avg = sessionCursor.getDouble(0);
            sessionCursor.close();
        } catch (SQLiteException e) {
            Log.d(LOG, "Failed to connect to database", e );
        }
        return avg;
    }



    public int getTotalProfit() {
        int total = 0;
        try {
            database = dbHelper.getReadableDatabase();
            Cursor cursor = database.query(SessionTable.TABLE_SESSION,
                    new String[]{"TOTAL(" + SessionTable.COLUMN_RESULT + ")"},
                    null, null, null, null, null);
            cursor.moveToFirst();
            total = cursor.getInt(0);
            cursor.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
        return total;
    }

    /** Returns a list of all the GameTypes */
    public ArrayList<String> getAllGameTypes() {
        ArrayList<String> game_types = null;
        try {
            database = dbHelper.getReadableDatabase();
            game_types = new ArrayList<>();
            Cursor cursor = database.query(Game_TypeTable.TABLE_GAMETYPE, null,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Game_type game_type = cursorToGameType(cursor);
                game_types.add(game_type.toString());
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
        return game_types;
    }

    public ArrayList<Game_Structure> getAllGameStructures() {
        ArrayList<Game_Structure> game_structures = null;
        try {
            database = dbHelper.getReadableDatabase();
            game_structures = new ArrayList<>();
            Cursor cursor = database.query(Game_StructureTable.TABLE_GAME_STRUCTURE, null,
                    null, null, null, null, null);

            while(cursor.moveToNext()) {
                Game_Structure game_structure = cursorToGameStructure(cursor);
                game_structures.add(game_structure);
            }
            cursor.close();
            database.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
        return game_structures;
    }

    public void addGameStructure(Game_Structure gs) {
        try {
            database = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Game_StructureTable.COLUMN_SMALLBLIND,gs.getSmall_blind());
            cv.put(Game_StructureTable.COLUMN_BIGBLIND, gs.getBig_blind());
            cv.put(Game_StructureTable.COLUMN_ANTE, gs.getAnte());
            database.insert(Game_StructureTable.TABLE_GAME_STRUCTURE, null, cv);
            database.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
    }

    /* returns the last sessions, max number needs to be positive */
    public ArrayList<Session> getLastSessions(int maxNumberOfSessions) {
        ArrayList<Session> sessions = null;
        try {
            database = dbHelper.getReadableDatabase();
            sessions = new ArrayList<>();
            //Cursor cursor = database.query(SessionTable.TABLE_SESSION, null, null, null,
            //        null, null, SessionTable.COLUMN_ID + " ASC", "3");
            Cursor cursor = database.rawQuery("SELECT * FROM (SELECT * FROM " +
                    SessionTable.TABLE_SESSION + " ORDER BY " +
                    SessionTable.COLUMN_ID + " DESC LIMIT ?)" +
                    " ORDER BY " + SessionTable.COLUMN_ID + " ASC;",
                    new String[]{String.valueOf(maxNumberOfSessions)});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Session session = cursorToSession(cursor);
                sessions.add(session);
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
        return sessions;
    }


    /** returns a list over the result from all different game types */
    public ArrayList<String> getResultFromGametypes() {
        ArrayList<String> resultList = new ArrayList<>();
        try {
            database = dbHelper.getReadableDatabase();
            ArrayList<Game_type> game_types = new ArrayList<>();
            Cursor cursor = database.query(Game_TypeTable.TABLE_GAMETYPE, null,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Game_type game_type = cursorToGameType(cursor);
                game_types.add(game_type);
                cursor.moveToNext();
            }
            cursor.close();

            for (Game_type game_type : game_types) {

                Cursor resultCursor = database.query(
                        SessionTable.TABLE_SESSION,
                        new String[]{SessionTable.COLUMN_RESULT},
                        SessionTable.COLUMN_GAMETYPE + " = " + String.valueOf(game_type.getId()),
                        null, null, null, null);
                resultCursor.moveToFirst();
                int result = 0;
                while (!resultCursor.isAfterLast()) {
                    result += resultCursor.getInt(0);
                    resultCursor.moveToNext();
                }
                resultList.add(String.valueOf(result));
                resultCursor.close();
            }
        } catch(SQLiteException e){
            Log.e(LOG, "Failed to connect to database", e);
        }

        return  resultList;
    }

    public Cursor getSomeSessionTables() {
        Cursor cursor = null;
        try {
            database = dbHelper.getReadableDatabase();
            cursor = database.query(SessionTable.TABLE_SESSION,
                    new String[]{SessionTable.COLUMN_ID, SessionTable.COLUMN_DURATION, SessionTable.COLUMN_RESULT},
                    null, null, null, null, null);
            database.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
        return cursor; // won't work because we closed the database already (I think)
    }

    public ArrayList<String> getLocations() {
        ArrayList<String> locations = new ArrayList<>();
        try {
            database = dbHelper.getReadableDatabase();
            Cursor cursor = database.query(
                    true,
                    SessionTable.TABLE_SESSION,
                    new String[]{SessionTable.COLUMN_LOCATION},
                    null, null, null, null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                locations.add(cursor.getString(cursor.getColumnIndex(SessionTable.COLUMN_LOCATION)));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
        return locations;
    }

    public Session addSession(Session session) {
        ContentValues values = new ContentValues();
        values.put(SessionTable.COLUMN_GAMETYPE, session.getGame_type_ref());
        values.put(SessionTable.COLUMN_LOCATION, session.getLocation());
        values.put(SessionTable.COLUMN_GAMESTRUCTURE, session.getGame_structure_ref());
        values.put(SessionTable.COLUMN_DURATION, session.getDuration());
        values.put(SessionTable.COLUMN_DATE, formatter.format(session.getDate()));
        values.put(SessionTable.COLUMN_RESULT, session.getResult());
        values.put(SessionTable.COLUMN_GAMENOTES, session.getGame_notes());

        Session newSession = null;
        try {
            database = dbHelper.getWritableDatabase();
            long id = database.insertWithOnConflict(SessionTable.TABLE_SESSION, null, values,
                    SQLiteDatabase.CONFLICT_IGNORE);
            Cursor cursor = database.query(SessionTable.TABLE_SESSION,
                    allColumns,
                    SessionTable.COLUMN_ID + " = " + id,
                    null, null, null, null);
            cursor.moveToFirst();
            newSession = cursorToSession(cursor);
            cursor.close();
            database.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
        return newSession;
    }

    public void deleteSession(long sessionID) {
        //long id = session.getId();
        try {
            database = dbHelper.getWritableDatabase();
            database.delete(SessionTable.TABLE_SESSION,
                    SessionTable.COLUMN_ID + " = " + sessionID,
                    null);
            database.close();
        } catch (SQLiteException e) {
            Log.e(LOG, "Failed to connect to database", e);
        }
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
