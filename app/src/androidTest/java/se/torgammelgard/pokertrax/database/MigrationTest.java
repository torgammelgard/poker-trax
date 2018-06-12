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
        SqliteDatabaseTestHelper.insertGameStructure(1, 100, 200, 50, mSqliteTestDbHelper);
        mMigrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2);
        GameStructure gameStructure = getMigratedRoomDatabase().gameStructureDao().getAll().get(0);
        assert gameStructure.getAnte() == 50;
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
