package pos.jgeraldo.com.openflightsandroidsample.http;

import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AirportsRetrofitInterface {

    @FormUrlEncoded
    @POST("./")
    Call<AirportSearchResult> searchAirports(@Field("name") String airportName,
                                             @Field("city") String cityName,
                                             @Field("country") String countryName,
                                             @Field("iata") String iata,
                                             @Field("dst") String dst,
                                             @Field("db") String db,
                                             @Field("iatafilter") String filter,
                                             @Field("action") String action,
                                             @Field("offset") String offset);
}
