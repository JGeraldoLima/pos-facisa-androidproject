package pos.jgeraldo.com.openflightsandroidsample.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;
import pos.jgeraldo.com.openflightsandroidsample.ui.adapters.AirportRecyclerAdapter;
import pos.jgeraldo.com.openflightsandroidsample.ui.fragments.AirportDetailFragment;
import pos.jgeraldo.com.openflightsandroidsample.ui.fragments.FavoritesAirportsFragment;
import pos.jgeraldo.com.openflightsandroidsample.ui.fragments.AirportsListFragment;
import pos.jgeraldo.com.openflightsandroidsample.ui.listeners.OnAirportClickListener;
import pos.jgeraldo.com.openflightsandroidsample.utils.Util;

import static pos.jgeraldo.com.openflightsandroidsample.ui.activities.AirportDetailActivity.EXTRA_AIRPORT_SOURCE_DETAIL;

public class MainActivity extends AppCompatActivity implements OnAirportClickListener {

    public static int DETAIL_ACTIVITY_RESULT_CODE = 0;

    private Activity mActivity;

    private static FragmentManager mFragmentManager;

    // FIXME: check @AirportDetailActivity FIXME comment and help me clean this terrible code :(
    // For understanding purposes, these two objects bellow are beeing used globally on the application,
    // just because the tablet support (it maked things more complex)
    public static List<Airport> apiAirports;

    public static AirportRecyclerAdapter airportsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        mFragmentManager = getSupportFragmentManager();

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
        if (mActivity.getResources().getBoolean(R.bool.smartphone)) {
            Intent it = new Intent(mActivity, AirportDetailActivity.class);
            it.putExtra(AirportDetailActivity.EXTRA_AIRPORT_DETAIL, Parcels.wrap(airport));
//            it.putExtra(AirportDetailActivity.EXTRA_AIRPORT_SOURCE_DETAIL, Parcels.wrap(apiAirports));
            startActivityForResult(it, DETAIL_ACTIVITY_RESULT_CODE);
        } else {
            //TODO: REPLICATE THE BEHAVIOR ABOVE
            AirportDetailFragment adf = AirportDetailFragment.newInstance(airport);
            mFragmentManager
                .beginTransaction()
                .replace(R.id.content_detail, adf, AirportDetailActivity.DETAIL_FRAGMENT)
                .commit();
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (resultCode == DETAIL_ACTIVITY_RESULT_CODE) {
//            apiAirports.clear();
//            apiAirports.addAll((List<Airport>) Parcels.unwrap(intent.getParcelableExtra
//                (EXTRA_AIRPORT_SOURCE_DETAIL)));
//        }
//    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() <= 1) {
            Util.openQuestionAlertDialog(mActivity, R.string.dialog_question_exit, true);
        } else {
            mFragmentManager.popBackStackImmediate();
        }
    }

}