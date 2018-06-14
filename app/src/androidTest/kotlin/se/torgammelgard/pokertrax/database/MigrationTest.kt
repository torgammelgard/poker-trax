package se.torgammelgard.pokertrax.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.arch.persistence.room.testing.MigrationTestHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.text.SimpleDateFormat
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
        mMigrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2)
        val gameTypeDao = migratedRoomDatabase.gameTypeDao()
        val gameTypes = gameTypeDao.getAll()
        assertEquals(1, gameTypes.size)
        assertEquals("No limit test", gameTypes[0].type)
    }

    @Test
    @Throws(IOException::class)
    fun migrationFrom1To2_containsCorrectData_Table_GameStructure() {
        SqliteDatabaseTestHelper.insertGameStructure(1, 100, 200, 50, mSqliteTestDbHelper!!)
        mMigrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2)
        val gameStructure = migratedRoomDatabase.gameStructureDao().getAll()[0]
        assertEquals(50, gameStructure.ante)
    }

    @Test
    @Throws(IOException::class)
    fun migrationFrom1To2_containsCorrectData_Table_Session() {
        val date = Date(90_000_000)
        val date2 = Date(180_000_000)
        SqliteDatabaseTestHelper.insertSession(1, 1, "test location", 1, 100, date, 1000, "game notes", mSqliteTestDbHelper!!)
        SqliteDatabaseTestHelper.insertSession(2, 1, "test location 2", 1, 100, date2, 1000, "game notes", mSqliteTestDbHelper!!)
        mMigrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2)
        val sessionDao = migratedRoomDatabase.sessionDao()

        val sessions = sessionDao.getAll()
        if (sessions.size != 2) {
            assertTrue(false)
        }

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        sessions.forEach {
            when (it.id) {
                1L -> {
                    assertEquals("test location", it.location)
                    assertEquals(formatter.format(date), formatter.format(it.date))
                }
                2L -> {
                    assertEquals("test location 2", it.location)
                    assertEquals(formatter.format(date2), formatter.format(it.date))
                }
                else -> assertTrue(false)
            }
        }
    }

    companion object {
        private const val TEST_DB_NAME = "test-db"

        internal val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Table session
                // Backup (rename) the table
                database.execSQL("DROP TABLE IF EXISTS 'session_old'")
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
                database.execSQL("INSERT INTO 'session' (date, duration, result, game_structure, game_type, location, game_info) " +
                        "SELECT strftime('%s000', date), duration, result, game_structure, game_type, location, game_info FROM session_old")
                // Clean up
                database.execSQL("DROP TABLE IF EXISTS 'session_old'")

                // Table game_structure
                database.execSQL("DROP TABLE IF EXISTS 'game_structure_old'")
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
                database.execSQL("DROP TABLE IF EXISTS 'game_type_old'")
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
