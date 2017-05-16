package pos.jgeraldo.com.openflightsandroidsample.http;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.AirportGson;

public class AirportsParser {

    static String baseUrl = "https://openflights.org/php/apsearch.php";

    public static List<AirportGson> searchAirports(String airportName, String cityName, String countryName, String
        countryCode) throws IOException {

        RequestBody body = getFormData("", airportName, cityName, countryName, countryCode, "0");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url(baseUrl)
            .post(body)
            .build();

        Response response = client.newCall(request).execute();

        String json = response.body().string();

        Gson gson = new Gson();
        AirportSearchResult result = gson.fromJson(json, AirportSearchResult.class);
        return result.airports;
    }

    private static RequestBody getFormData(String airportId, String airportName, String cityName, String countryName,
                                           String countryCode, String offset) {

        // TODO: use offset property to load more items
        // TODO: figure out how to load items by max qnty instead of page index (offset)
        return new FormBody.Builder()
            .add("apid", airportId)
            .add("name", airportName)
            .add("city", cityName)
            .add("country", countryName)
            .add("code", countryCode)
            .add("iata", "")
            .add("dst", "N")
            .add("db", "airports")
            .add("iatafilter", "false")
            .add("action", "SEARCH")
            .add("offset", offset)
            .build();
    }
}
