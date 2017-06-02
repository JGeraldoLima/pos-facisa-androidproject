package pos.jgeraldo.com.openflightsandroidsample.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.databinding.AirportDetailFragmentBinding;
import pos.jgeraldo.com.openflightsandroidsample.storage.dao.AppDatabase;
import pos.jgeraldo.com.openflightsandroidsample.storage.dao.IAirportDao;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;

import static pos.jgeraldo.com.openflightsandroidsample.ui.activities.AirportDetailActivity.EXTRA_AIRPORT_DETAIL;

public class AirportDetailFragment extends Fragment implements OnMapReadyCallback {

    private Context mContext;

    private Activity mActivity;

    private static FragmentManager mFragmentManager;

    AirportDetailFragmentBinding binding;

    IAirportDao airportDao;

    Airport mAirport;

    GoogleMap mMap;

    public AirportDetailFragment() {
        super();
    }

    public static AirportDetailFragment newInstance(Airport airport) {
        Bundle params = new Bundle();
        params.putParcelable(EXTRA_AIRPORT_DETAIL, Parcels.wrap(airport));

        AirportDetailFragment f = new AirportDetailFragment();
        f.setArguments(params);

        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        airportDao = AppDatabase.getInMemoryDatabase(getContext()).airportDao();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mActivity = getActivity();
        mContext = getContext();

        mFragmentManager = getChildFragmentManager();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getResources().getBoolean(R.bool.smartphone)) {
            ((AppCompatActivity) mActivity).setSupportActionBar(binding.toolbar);
            ActionBar toolbar = ((AppCompatActivity) mActivity).getSupportActionBar();
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle(mAirport.shortName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAirport = Parcels.unwrap(getArguments().getParcelable(EXTRA_AIRPORT_DETAIL));
        binding = DataBindingUtil.inflate(inflater, R.layout.airport_detail_fragment, container, false);
        binding.setAirport(mAirport);

        updateFabIcon(airportDao.isFavorite(mAirport.isFavorite));
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });
        binding.tvDetailFragmentAirportFrom.setText(String.format("%s - %s", mAirport.city, mAirport.countryName));

        if (binding.appBar != null) {
            binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (verticalOffset == 0 || verticalOffset <= binding.toolbar.getHeight()) {
//                    int dpValue = 8; // margin in dips
//                    float d = mContext.getResources().getDisplayMetrics().density;
//                    int margin = (int) (dpValue * d); // margin in pixels
//
//                    ConstraintLayout.LayoutParams llp = new ConstraintLayout.LayoutParams(0, ConstraintLayout
//                        .LayoutParams.WRAP_CONTENT);
//                    llp.setMargins(margin, margin, margin, 0); // llp.setMargins(left, top, right, bottom);
//                    binding.tvDetailFragmentAirportName.setLayoutParams(llp);
                    }
                }
            });
        }

        setUpMap();

        return binding.getRoot();
    }

    private void updateFabIcon(boolean isFavorite) {
        binding.fab.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_not_favorite);
    }

    public void toggleFavorite() {
        final Airport airport = binding.getAirport();
        boolean isFavorite = airportDao.isFavorite(airport.isFavorite);
        if (isFavorite) {
            airportDao.deleteAirports(airport);
//            Toast.makeText(getActivity(), R.string.msg_favorite_removed, Toast.LENGTH_SHORT).show();
        } else {
            airportDao.insertAirport(airport);
//            Toast.makeText(getActivity(), R.string.msg_favorite_added, Toast.LENGTH_SHORT).show();
        }

        // Animate
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
            binding.fab, View.SCALE_X, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
            binding.fab, View.SCALE_Y, 0f);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                updateFabIcon(airportDao.isFavorite(airport.isFavorite));
            }
        });
        scaleY.setRepeatMode(ValueAnimator.REVERSE);
        scaleX.setRepeatCount(1);
        scaleY.setRepeatCount(1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.start();
    }

    private void setUpMap() {
        //TODO try to use databinding here
        SupportMapFragment mapFragment = (SupportMapFragment) mFragmentManager.findFragmentById(R.id.mapAirportLocation);
        mapFragment.getMapAsync(this);

        binding.mapHandlerImage.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        binding.airportDetailNestedScroll.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        binding.airportDetailNestedScroll.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        binding.airportDetailNestedScroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);

        double latitude = Double.parseDouble(mAirport.latitude);
        double longitude = Double.parseDouble(mAirport.longitude);

        LatLng airportMarkerLocation = new LatLng(latitude, longitude);
        BitmapDescriptor myAirportMarkerIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_airport_map_marker);

        MarkerOptions currentMarkerOptions = new MarkerOptions()
            .position(airportMarkerLocation)
            .title(mAirport.getCompleteName())
            .icon(myAirportMarkerIcon);
        mMap.addMarker(currentMarkerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(airportMarkerLocation, 15.5f));
    }
}