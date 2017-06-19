package pos.jgeraldo.com.openflightsandroidsample.http;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;
import pos.jgeraldo.com.openflightsandroidsample.storage.preferences.OFASPreferences;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AirportsParser {

    static String baseUrl = "https://openflights.org/php/apsearch.php/";

    public static List<Airport> searchAirports(Context context)
        throws IOException {

        String airportName = OFASPreferences.getCurrentSearchAirportName(context);
        String cityName = OFASPreferences.getCurrentSearchCityName(context);
        String countryName = OFASPreferences.getCurrentSearchCountryName(context);

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        AirportsRetrofitInterface api = retrofit.create(AirportsRetrofitInterface.class);

        Call<AirportSearchResult> call = api.searchAirports(airportName, cityName, countryName, "", "N", "airports",
            "false", "SEARCH", OFASPreferences.getCurrentOffsetValue(context) + "");

        AirportSearchResult result = call.execute().body();

        // treat when result == null, throw some exception or something
        OFASPreferences.setCurrentSearchMaxResults(context, result.maxQnty);
        return result.airports;
    }
}
