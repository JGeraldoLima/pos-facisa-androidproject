package pos.jgeraldo.com.openflightsandroidsample.storage.models;

import com.google.gson.annotations.SerializedName;

public class AirportGson {

    @SerializedName("apid")
    public String id;

    @SerializedName("name")
    public String shortName;

    @SerializedName("ap_name")
    public String completeName;

    @SerializedName("city")
    public String city;

    @SerializedName("country")
    public String countryName;

    @SerializedName("iata")
    public String iataCode;

    @SerializedName("x")
    public String latitude;

    @SerializedName("y")
    public String longitude;
}
