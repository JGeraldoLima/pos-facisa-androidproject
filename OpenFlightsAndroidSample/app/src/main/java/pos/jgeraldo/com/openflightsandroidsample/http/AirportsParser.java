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

public class AirportsParser {

    static String baseUrl = "https://openflights.org/php/apsearch.php";

    public static List<Airport> searchAirports(Context context)
        throws IOException {

        String airportName = OFASPreferences.getCurrentSearchAirportName(context);
        String cityName = OFASPreferences.getCurrentSearchCityName(context);
        String countryName = OFASPreferences.getCurrentSearchCountryName(context);

        RequestBody body = getFormData(context, airportName, cityName, countryName);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url(baseUrl)
            .post(body)
            .build();

        Response response = client.newCall(request).execute();

        String json = response.body().string();

        Gson gson = new Gson();
        AirportSearchResult result = gson.fromJson(json, AirportSearchResult.class);
        OFASPreferences.setCurrentSearchMaxResults(context, result.maxQnty);
        return result.airports;
    }

    private static RequestBody getFormData(Context context, String airportName, String cityName, String countryName) {

        return new FormBody.Builder()
            .add("name", airportName)
            .add("city", cityName)
            .add("country", countryName)
            .add("iata", "")
            .add("dst", "N")
            .add("db", "airports")
            .add("iatafilter", "false")
            .add("action", "SEARCH")
            .add("offset", OFASPreferences.getCurrentOffsetValue(context) + "")
            .build();
    }
}
