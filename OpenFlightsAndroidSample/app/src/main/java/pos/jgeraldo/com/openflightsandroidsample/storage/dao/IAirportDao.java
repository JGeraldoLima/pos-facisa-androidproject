package pos.jgeraldo.com.openflightsandroidsample.storage.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface IAirportDao {

    @Insert(onConflict = IGNORE)
    void insertAirport(Airport airport);

    @Query("SELECT * FROM Airport WHERE isFavorite = 1 ORDER BY shortName")
    LiveData<List<Airport>> listAllFavorites();

    @Query("SELECT COUNT(*) FROM Airport WHERE isFavorite = :isFavorite")
    boolean isFavorite(boolean isFavorite);

    @Delete
    void deleteAirports(Airport... airports);
}