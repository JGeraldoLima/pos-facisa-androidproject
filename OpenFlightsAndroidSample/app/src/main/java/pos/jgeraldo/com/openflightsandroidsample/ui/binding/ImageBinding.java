package pos.jgeraldo.com.openflightsandroidsample.ui.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class ImageBinding {

    @BindingAdapter({"bind:latitude", "bind:longitude"})
    public static void setImageUrl(ImageView imageView, String latitude, String longitude) {
        String mapUrl = generateMapViewUrl(latitude, longitude);
        Glide.with(imageView.getContext())
            .load(mapUrl)
            .into(imageView);
    }

    @BindingAdapter({"placeHolder", "bind:latitude", "bind:longitude"})
    public static void setImageUrl(ImageView imageView, Drawable placeholder, String latitude, String longitude) {
        String mapUrl = generateMapViewUrl(latitude, longitude);
        Glide.with(imageView.getContext())
            .load(mapUrl)
            .placeholder(placeholder)
            .into(imageView);
    }

    private static String generateMapViewUrl(String latitude, String longitude) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
            .authority("maps.googleapis.com")
            .appendPath("maps")
            .appendPath("api")
            .appendPath("staticmap")
            .appendQueryParameter("center", String.format("%s,%s", latitude, longitude))
            .appendQueryParameter("size", "500x400")
            .appendQueryParameter("zoom", "17")
            .appendQueryParameter("format", "jpg")
            .appendQueryParameter("language", Locale.getDefault().getLanguage())
            .appendQueryParameter("markers", String.format("|%s,%s", latitude, longitude))
            .fragment("section-name");
        return builder.build().toString();
    }
}