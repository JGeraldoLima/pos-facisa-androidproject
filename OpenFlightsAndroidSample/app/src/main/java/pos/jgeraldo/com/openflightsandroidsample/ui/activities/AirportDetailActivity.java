package pos.jgeraldo.com.openflightsandroidsample.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.parceler.Parcels;

import java.util.List;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.databinding.AirportDetailActivityBinding;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;
import pos.jgeraldo.com.openflightsandroidsample.ui.fragments.AirportDetailFragment;

import static pos.jgeraldo.com.openflightsandroidsample.ui.activities.MainActivity.DETAIL_ACTIVITY_RESULT_CODE;

public class AirportDetailActivity extends AppCompatActivity {

    public static final String EXTRA_AIRPORT_DETAIL = "airport";

    public static final String EXTRA_AIRPORT_SOURCE_DETAIL = "airport_source";

    public static final String DETAIL_FRAGMENT = "detailFragment";

    AirportDetailActivityBinding binding;

    // FIXME: how the hell can I send back the modified list from @AirportDetailFragment to this activity
    // when I click back button? Would be something like pass it as an argument of a bundle on fragment
    // onDetroy method, so I could catch it and then use on onBackPressed implementation below.
    // Any suggestions would be great!
//    public static List<Airport> airportSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
            this, R.layout.airport_detail_activity);

        Airport airport = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_AIRPORT_DETAIL));
//        airportSource = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_AIRPORT_SOURCE_DETAIL));

        if (savedInstanceState == null) {
            AirportDetailFragment adf = AirportDetailFragment.newInstance(airport);
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_detail, adf, DETAIL_FRAGMENT)
                .commit();
        }
    }

//    private Intent prepareBackIntentData() {
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//
//        bundle.putParcelable(EXTRA_AIRPORT_SOURCE_DETAIL, Parcels.wrap(airportSource));
//        intent.putExtras(bundle);
//
//        return intent;
//    }

//    @Nullable
//    @Override
//    public Intent getParentActivityIntent() {
//        Intent i = super.getParentActivityIntent();
//        if (i != null) {
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Bundle bundle = new Bundle();
//
//            bundle.putParcelable(EXTRA_AIRPORT_SOURCE_DETAIL, Parcels.wrap(airportSource));
//            i.putExtras(bundle);
//        }
//
//        setResult(DETAIL_ACTIVITY_RESULT_CODE, i);
//        return i;
//    }
//
//    @Override
//    public void onBackPressed() {
//        Intent i = prepareBackIntentData();
//
//        Activity parent = getParent();
//        if (parent == null) {
//            setResult(DETAIL_ACTIVITY_RESULT_CODE, i);
//        } else {
//            parent.setResult(DETAIL_ACTIVITY_RESULT_CODE, i); //TODO: needed?
//        }
//        super.onBackPressed();
//    }
}
