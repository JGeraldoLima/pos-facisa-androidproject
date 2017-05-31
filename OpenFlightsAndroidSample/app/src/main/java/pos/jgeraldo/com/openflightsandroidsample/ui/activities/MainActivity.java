package pos.jgeraldo.com.openflightsandroidsample.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;
import pos.jgeraldo.com.openflightsandroidsample.ui.fragments.AirportDetailFragment;
import pos.jgeraldo.com.openflightsandroidsample.ui.fragments.FavoritesAirportsFragment;
import pos.jgeraldo.com.openflightsandroidsample.ui.fragments.AirportsListFragment;
import pos.jgeraldo.com.openflightsandroidsample.ui.listeners.OnAirportClickListener;

public class MainActivity extends AppCompatActivity implements OnAirportClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AirportsPagerAdapter pagerAdapter = new AirportsPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.vpMain);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsMain);
        tabLayout.setupWithViewPager(viewPager);
    }


    private class AirportsPagerAdapter extends FragmentPagerAdapter {

        public AirportsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new AirportsListFragment();
            }
            return new FavoritesAirportsFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? getString(R.string.tab_title_search) : getString(R.string.tab_title_favorites);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onAirportClick(Airport airport) {
        if (getResources().getBoolean(R.bool.smartphone)) {
            Intent it = new Intent(this, AirportDetailActivity.class);
            it.putExtra(AirportDetailActivity.EXTRA_AIRPORT_DETAIL, Parcels.wrap(airport));
            startActivity(it);
        } else {
            AirportDetailFragment mdf = AirportDetailFragment.newInstance(airport);
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_detail, mdf, AirportDetailActivity.DETAIL_FRAGMENT)
                .commit();
        }
    }

}