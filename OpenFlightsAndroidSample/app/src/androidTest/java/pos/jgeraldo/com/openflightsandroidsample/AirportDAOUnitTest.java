package pos.jgeraldo.com.openflightsandroidsample;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import pos.jgeraldo.com.openflightsandroidsample.storage.dao.AppDatabase;
import pos.jgeraldo.com.openflightsandroidsample.storage.dao.IAirportDao;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AirportDAOUnitTest {

    private IAirportDao airportDao;

    @Mock
    private Observer<List<Airport>> observer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getTargetContext();
        airportDao = AppDatabase.getInMemoryDatabase(context).airportDao();
    }


//    @Test
//    public void testListAllFavoritesLive() throws Exception {
//        Airport a = new Airport("BSB", "Aeroporto de Brasilia",
//            "Aeroporto Internacional de Brasília", "Brasília", "Brasil", "BSB",
//            "15.8697369", "-47.9172348");
//        airportDao.listAllFavoritesLive().observeForever(observer);
//
//        // when
//        airportDao.insertAirport(a);
//
//        // then
//        verify(observer).onChanged(Collections.singletonList(a)); //NOT INTERACTING
//
//        airportDao.deleteAll();
//    }

    @Test
    public void testInsertAirport() throws Exception {
        Airport a = new Airport("BSB", "Aeroporto de Brasilia",
            "Aeroporto Internacional de Brasília", "Brasília", "Brasil", "BSB",
            "15.8697369", "-47.9172348");

        airportDao.insertAirport(a);

        List<Airport> favorites = airportDao.listAllFavorites();
        Airport favorite = favorites.get(0);

        assertEquals(1, favorites.size());
        assertEquals(a.id, favorite.id);
        assertEquals(a.shortName, favorite.shortName);
        assertEquals(a.completeName, favorite.completeName);
        assertEquals(a.city, favorite.city);
        assertEquals(a.countryName, favorite.countryName);
        assertEquals(a.iataCode, favorite.iataCode);
        assertEquals(a.latitude, favorite.latitude);
        assertEquals(a.longitude, favorite.longitude);

        airportDao.deleteAll();
    }

    @Test
    public void testlistAllFavorites() throws Exception {
        Airport a1 = new Airport("BSB", "Aeroporto de Brasilia",
            "Aeroporto Internacional de Brasília", "Brasília", "Brasil", "BSB",
            "15.8697369", "-47.9172348");

        Airport a2 = new Airport("MCZ", "Aeroporto de Maceió", "Aeroporto Internacional Zumbi dos Palmares", "Maceió",
            "Brasil", "MCZ", "-9.5118606", "-35.7931828");

        airportDao.insertAirport(a1);
        airportDao.insertAirport(a2);

        List<Airport> favorites = airportDao.listAllFavorites();
        
        Airport favorite1 = favorites.get(0);
        Airport favorite2 = favorites.get(1);

        assertEquals(2, favorites.size());
        
        assertEquals(a1.id, favorite1.id);
        assertEquals(a1.shortName, favorite1.shortName);
        assertEquals(a1.completeName, favorite1.completeName);
        assertEquals(a1.city, favorite1.city);
        assertEquals(a1.countryName, favorite1.countryName);
        assertEquals(a1.iataCode, favorite1.iataCode);
        assertEquals(a1.latitude, favorite1.latitude);
        assertEquals(a1.longitude, favorite1.longitude);

        assertEquals(a2.id, favorite2.id);
        assertEquals(a2.shortName, favorite2.shortName);
        assertEquals(a2.completeName, favorite2.completeName);
        assertEquals(a2.city, favorite2.city);
        assertEquals(a2.countryName, favorite2.countryName);
        assertEquals(a2.iataCode, favorite2.iataCode);
        assertEquals(a2.latitude, favorite2.latitude);
        assertEquals(a2.longitude, favorite2.longitude);

        airportDao.deleteAll();
    }

    @Test
    public void testIsFavorite() throws Exception {
        Airport a1 = new Airport("BSB", "Aeroporto de Brasilia",
            "Aeroporto Internacional de Brasília", "Brasília", "Brasil", "BSB",
            "15.8697369", "-47.9172348");

        Airport a2 = new Airport("MCZ", "Aeroporto de Maceió", "Aeroporto Internacional Zumbi dos Palmares", "Maceió",
            "Brasil", "MCZ", "-9.5118606", "-35.7931828");

        airportDao.insertAirport(a1);

        assertTrue(airportDao.isFavorite("BSB"));
        assertFalse(airportDao.isFavorite("MCZ"));

        airportDao.insertAirport(a2);
        assertTrue(airportDao.isFavorite("MCZ"));

        airportDao.deleteAll();
    }

    @Test
    public void testDeleteAirports() throws Exception {
        Airport a1 = new Airport("BSB", "Aeroporto de Brasilia",
            "Aeroporto Internacional de Brasília", "Brasília", "Brasil", "BSB",
            "15.8697369", "-47.9172348");

        Airport a2 = new Airport("MCZ", "Aeroporto de Maceió", "Aeroporto Internacional Zumbi dos Palmares", "Maceió",
            "Brasil", "MCZ", "-9.5118606", "-35.7931828");

        airportDao.insertAirport(a1);
        airportDao.insertAirport(a2);

        List<Airport> favorites = airportDao.listAllFavorites();

        assertEquals(2, favorites.size());

        airportDao.deleteAirports(a1);
        favorites = airportDao.listAllFavorites();

        assertEquals(1, favorites.size());

        airportDao.deleteAll();
    }
}