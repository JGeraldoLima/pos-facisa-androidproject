package pos.jgeraldo.com.openflightsandroidsample.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.ui.fragments.AirportDetailFragment;

public class MainActivity extends AppCompatActivity implements AirportDetailFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        displayView(R.id.nav_map);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}