package pos.jgeraldo.com.openflightsandroidsample.storage.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ObjectConstructor;

import org.parceler.Parcel;

@Entity
@Parcel
public class Airport {

    @PrimaryKey
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

    @SerializedName("y")
    public String latitude;

    @SerializedName("x")
    public String longitude;

    public boolean isFavorite;

    @Ignore
    public Airport() {
    }

    public Airport(String id, String shortName, String completeName, String city, String countryName,
                   String iataCode, String latitude, String longitude) {
        this.id = id;
        this.shortName = shortName;
        this.completeName = completeName;
        this.city = city;
        this.countryName = countryName;
        this.iataCode = iataCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object airport1) {
        return airport1 instanceof Airport && ((Airport) airport1).id.equals(this.id);
    }
}
