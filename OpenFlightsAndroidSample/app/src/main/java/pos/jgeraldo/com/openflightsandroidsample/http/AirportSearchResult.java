package pos.jgeraldo.com.openflightsandroidsample.http;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;

public class AirportSearchResult {

    @SerializedName("airports")
    List<Airport> airports;

    @SerializedName("max")
    int maxQnty;

}
