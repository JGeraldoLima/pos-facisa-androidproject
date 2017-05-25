package pos.jgeraldo.com.openflightsandroidsample.storage.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;

/**
 * Code got from Google I/O 2017  - Persistence CodeLab.
 * Font: https://codelabs.developers.google.com/codelabs/android-persistence/#0
 */


@Database(entities = {Airport.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract IAirportDao airportDao();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "dbAirports")
                    // To simplify the codelab, allow queries on the main thread.
                    // Don't do this on a real app! See PersistenceBasicSample for an example.
                    // TODO: put it into a AsyncTask (future: RX)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}