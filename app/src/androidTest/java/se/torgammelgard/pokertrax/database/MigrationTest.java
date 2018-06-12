package se.torgammelgard.pokertrax.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.arch.persistence.room.testing.MigrationTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import se.torgammelgard.pokertrax.entity.GameStructure;
import se.torgammelgard.pokertrax.entity.GameType;

@RunWith(AndroidJUnit4.class)
public class MigrationTest {
    private static final String TEST_DB_NAME = "test-db";

    private SqliteTestDbOpenHelper mSqliteTestDbHelper;

    @Rule
    public MigrationTestHelper mMigrationTestHelper;

    public MigrationTest() {
        mMigrationTestHelper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                AppDatabase.class.getCanonicalName(),
                new FrameworkSQLiteOpenHelperFactory());
    }

    static final Migration MIGRATION_1_2 =  new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Table session
            // Backup (rename) the table
            database.execSQL("ALTER TABLE 'session' RENAME TO 'session_old'");

            // Create the new table
            database.execSQL("CREATE TABLE IF NOT EXISTS 'session' " +
                    "('_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "'date' INTEGER, " +
                    "'duration' INTEGER NOT NULL, " +
                    "'result' INTEGER NOT NULL, " +
                    "'game_structure' INTEGER NOT NULL, " +
                    "'game_type' INTEGER NOT NULL, " +
                    "'location' TEXT, " +
                    "'game_info' TEXT, " +
                    "FOREIGN KEY(`game_structure`) REFERENCES `game_structure`(`_id`) ON UPDATE NO ACTION ON DELETE CASCADE," +
                    "FOREIGN KEY(`game_type`) REFERENCES `game_type`(`_id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
            // TODO copy from old to new table
            // Clean up
            database.execSQL("DROP TABLE IF EXISTS 'session_old'");

            // Table game_structure
            database.execSQL("ALTER TABLE 'game_structure' RENAME TO 'game_structure_old'");
            database.execSQL("CREATE TABLE IF NOT EXISTS 'game_structure'(" +
                    "'_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "'big_blind' INTEGER NOT NULL," +
                    "'small_blind' INTEGER NOT NULL," +
                    "'ante' INTEGER NOT NULL)");
            // TODO copy from old to new table

            // Clean up
            database.execSQL("DROP TABLE IF EXISTS 'game_structure_old'");

            // Table game_type
            database.execSQL("ALTER TABLE 'game_type' RENAME TO 'game_type_old'");
            database.execSQL("CREATE TABLE IF NOT EXISTS 'game_type'(" +
                    "'_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "'name' TEXT NOT NULL)");
            // TODO copy from old to new table
            database.execSQL("INSERT INTO 'game_type' (name) " +
                    "SELECT name FROM game_type_old");
            // Clean up
            database.execSQL("DROP TABLE IF EXISTS 'game_type_old'");
        }
    };

    @Before
    public void setUp() throws Exception {
        mSqliteTestDbHelper = new SqliteTestDbOpenHelper(InstrumentationRegistry.getTargetContext(), TEST_DB_NAME);
        SqliteDatabaseTestHelper.createTable(mSqliteTestDbHelper);
    }

    @After
    public void tearDown() throws Exception {
        SqliteDatabaseTestHelper.clearDatabase(mSqliteTestDbHelper);
    }

    @Test
    public void migrationFrom1To2_containsCorrectData() throws IOException {
        //SqliteDatabaseTestHelper.insertGameStructure(1, 100, 200, 50, mSqliteTestDbHelper);
        SqliteDatabaseTestHelper.insertGameType(1, "No limit test", mSqliteTestDbHelper);
        mMigrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2);
        //GameStructure gameStructure = getMigratedRoomDatabase().gameStructureDao().getAll().get(0);
        GameType gameTypeTest = getMigratedRoomDatabase().gameTypeDao().getAll().get(0);

        //assert gameStructure.getAnte() == 50;
        assert gameTypeTest != null;
    }

    private AppDatabase getMigratedRoomDatabase() {
        AppDatabase database = Room.databaseBuilder(InstrumentationRegistry.getTargetContext(),
                AppDatabase.class, TEST_DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
        // close the database and release any stream resources when the test finishes
        mMigrationTestHelper.closeWhenFinished(database);
        return database;
    }
}
