package pos.jgeraldo.com.openflightsandroidsample.http;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import pos.jgeraldo.com.openflightsandroidsample.storage.models.AirportGson;

public class AirportSearchResult {

    @SerializedName("airports")
    List<AirportGson> airports;
}
