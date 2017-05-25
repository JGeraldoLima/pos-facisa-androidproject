package pos.jgeraldo.com.openflightsandroidsample.storage.dao;

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
    void insertMovie(Airport airport);

    @Query("SELECT * FROM Airport WHERE isFavorite = 1 ORDER BY shortName")
    List<Airport> listAllFavorites();

    @Query("SELECT COUNT(*) FROM Airport WHERE id = :id")
    boolean isFavorite(String id);

    @Delete
    void deleteMovies(Airport... airports);
}