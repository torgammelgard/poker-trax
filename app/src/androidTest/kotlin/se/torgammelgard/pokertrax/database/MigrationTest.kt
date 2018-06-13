package se.torgammelgard.pokertrax.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.arch.persistence.room.testing.MigrationTestHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private var mSqliteTestDbHelper: SqliteTestDbOpenHelper? = null

    @get:Rule
    var mMigrationTestHelper: MigrationTestHelper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            AppDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory())

    // close the database and release any stream resources when the test finishes
    private val migratedRoomDatabase: AppDatabase
        get() {
            val database = Room.databaseBuilder(InstrumentationRegistry.getTargetContext(),
                    AppDatabase::class.java, TEST_DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build()
            mMigrationTestHelper.closeWhenFinished(database)
            return database
        }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mSqliteTestDbHelper = SqliteTestDbOpenHelper(InstrumentationRegistry.getTargetContext(), TEST_DB_NAME)
        SqliteDatabaseTestHelper.createTable(mSqliteTestDbHelper!!)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        SqliteDatabaseTestHelper.clearDatabase(mSqliteTestDbHelper!!)
    }

    @Test
    @Throws(IOException::class)
    fun migrationFrom1To2_containsCorrectData_Table_GameType() {
        SqliteDatabaseTestHelper.insertGameType(1, "No limit test", mSqliteTestDbHelper!!)
        val gameTypeDao = migratedRoomDatabase.gameTypeDao()
        val gameTypes = gameTypeDao.getAll()
        assert(gameTypes.size == 1)
        assert(gameTypes[0].type == "No limit test")
    }

    @Test
    @Throws(IOException::class)
    fun migrationFrom1To2_containsCorrectData_Table_GameStructure() {
        SqliteDatabaseTestHelper.insertGameStructure(1, 100, 200, 50, mSqliteTestDbHelper!!)
        mMigrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2)
        val gameStructure = migratedRoomDatabase.gameStructureDao().getAll()[0]
        assert(gameStructure.ante == 50)
    }

    @Test
    @Throws(IOException::class)
    fun migrationFrom1To2_containsCorrectData_Table_Session() {
        val date = Date(1000)
        SqliteDatabaseTestHelper.insertSession(1, 1, "test_location", 1, 100, date, 1000, "game notes", mSqliteTestDbHelper!!)
        mMigrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2)
        val sessionDao = migratedRoomDatabase.sessionDao()
        val sessions = sessionDao.getAll()
        assert(sessions.value.orEmpty().size == 1)
        if (sessions.value.orEmpty().size == 1) {
            val session = sessions.value.orEmpty()[1]
            assert(session.date == date)
            assert(session.location == "test_location")
        }
    }

    companion object {
        private const val TEST_DB_NAME = "test-db"

        internal val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Table session
                // Backup (rename) the table
                database.execSQL("ALTER TABLE 'session' RENAME TO 'session_old'")

                // Create the new table
                database.execSQL("CREATE TABLE IF NOT EXISTS 'session' " +
                        "('_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "'date' INTEGER, " +
                        "'duration' INTEGER NOT NULL, " +
                        "'result' INTEGER NOT NULL, " +
                        "'game_structure' INTEGER NOT NULL, " +
                        "'game_type' INTEGER NOT NULL, " +
                        "'location' TEXT NOT NULL, " +
                        "'game_info' TEXT, " +
                        "FOREIGN KEY(`game_structure`) REFERENCES `game_structure`(`_id`) ON UPDATE NO ACTION ON DELETE CASCADE," +
                        "FOREIGN KEY(`game_type`) REFERENCES `game_type`(`_id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
                // TODO copy from old to new table
                // Clean up
                database.execSQL("DROP TABLE IF EXISTS 'session_old'")

                // Table game_structure
                database.execSQL("ALTER TABLE 'game_structure' RENAME TO 'game_structure_old'")
                database.execSQL("CREATE TABLE IF NOT EXISTS 'game_structure'(" +
                        "'_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "'small_blind' INTEGER NOT NULL," +
                        "'big_blind' INTEGER NOT NULL," +
                        "'ante' INTEGER NOT NULL)")
                // Copy data from old table
                database.execSQL("INSERT INTO 'game_structure' (small_blind, big_blind, ante) " + "SELECT small_blind, big_blind, ante FROM game_structure_old")
                // Clean up
                database.execSQL("DROP TABLE IF EXISTS 'game_structure_old'")

                // Table game_type
                database.execSQL("ALTER TABLE 'game_type' RENAME TO 'game_type_old'")
                database.execSQL("CREATE TABLE IF NOT EXISTS 'game_type'(" +
                        "'_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "'name' TEXT NOT NULL)")
                // Copy data from old table
                database.execSQL("INSERT INTO 'game_type' (name) " + "SELECT name FROM game_type_old")
                // Clean up
                database.execSQL("DROP TABLE IF EXISTS 'game_type_old'")
            }
        }
    }
}
