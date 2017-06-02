package pos.jgeraldo.com.openflightsandroidsample.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.databinding.AirportDetailActivityBinding;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;
import pos.jgeraldo.com.openflightsandroidsample.ui.fragments.AirportDetailFragment;

public class AirportDetailActivity extends AppCompatActivity {

    public static final String EXTRA_AIRPORT_DETAIL = "airport";

    public static final String DETAIL_FRAGMENT = "detailFragment";

    AirportDetailActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
            this, R.layout.airport_detail_activity);

        Airport airport = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_AIRPORT_DETAIL));

        if (savedInstanceState == null) {
            AirportDetailFragment adf = AirportDetailFragment.newInstance(airport);
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_detail, adf, DETAIL_FRAGMENT)
                .commit();
        }
    }
}
