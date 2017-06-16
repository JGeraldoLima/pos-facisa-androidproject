package pos.jgeraldo.com.openflightsandroidsample.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.databinding.AirportsListFragmentBinding;
import pos.jgeraldo.com.openflightsandroidsample.http.AirportsParser;
import pos.jgeraldo.com.openflightsandroidsample.storage.dao.AppDatabase;
import pos.jgeraldo.com.openflightsandroidsample.storage.dao.IAirportDao;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;
import pos.jgeraldo.com.openflightsandroidsample.storage.preferences.OFASPreferences;
import pos.jgeraldo.com.openflightsandroidsample.ui.adapters.AirportRecyclerAdapter;
import pos.jgeraldo.com.openflightsandroidsample.ui.listeners.OnAirportClickListener;
import pos.jgeraldo.com.openflightsandroidsample.utils.Util;

import static pos.jgeraldo.com.openflightsandroidsample.ui.activities.MainActivity.airportsListAdapter;
import static pos.jgeraldo.com.openflightsandroidsample.ui.activities.MainActivity.apiAirports;

public class AirportsListFragment extends Fragment {

    private Activity mActivity;

    private Context mContext;

    IAirportDao dao;

    List<Airport> favoriteAirports;

    private MaterialDialog searchFilterDialog;

    private AirportsListFragmentBinding binding;

    public AirportsListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dao = AppDatabase.getInMemoryDatabase(context).airportDao();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        mActivity = getActivity();
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.airports_list_fragment, container, false);

        View root = binding.getRoot();
        ButterKnife.bind(this, root);

        binding.swipeBottomLoadMoreAirports.setOnRefreshListener(onLoadMoreListener);
        binding.swipeBottomLoadMoreAirports.setColorScheme(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);

        new FavoritesAirportsTask().execute();
        return root;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        /* We had returned from AirportDetailFragment -> AirportDetailActivity -> MainActivity -> AirportsListFragment
//         so, maybe there is new data (in this case, an airport marked as favorite)*/
//        if (airportsListAdapter != null) {
//            airportsListAdapter.notifyDataSetChanged();
//        }
//    }

    private class FavoritesAirportsTask extends AsyncTask<String, Void, List<Airport>> {

        @Override
        protected List<Airport> doInBackground(String... params) {
            return dao.listAllFavorites();
        }

        @Override
        protected void onPostExecute(List<Airport> airports) {
            super.onPostExecute(airports);
            favoriteAirports = airports;

            if (apiAirports != null) {
                updateList();
            }
        }
    }

    private class AirportSearchTask extends AsyncTask<String, Void, List<Airport>> {

        private ProgressDialog progressDialog;

        private final String progressMessage = mContext.getString(R.string.searching_airports_message);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setCancelable(true);
            progressDialog.setMessage(progressMessage);
            progressDialog.show();
        }

        @Override
        protected List<Airport> doInBackground(String... params) {
            try {
                return AirportsParser.searchAirports(mContext, params[0], params[1], params[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Airport> restApiAirports) {
            super.onPostExecute(restApiAirports);

            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                Log.e("List", e.getMessage());
            }

            if (OFASPreferences.getCurrentOffsetValue(mContext) > 0) {
                apiAirports.addAll(restApiAirports); //TODO: maybe it causes duplicates?
            } else {
                apiAirports = restApiAirports;
            }
            updateList();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                Log.e("List", e.getMessage());
            }
        }

        @Override
        protected void onCancelled(List<Airport> airports) {
            super.onCancelled(airports);
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                Log.e("List", e.getMessage());
            }
        }
    }

    private void updateList() {
        // Check which searched airports are already registered as favorite
        // on the database and set them
        for (Airport f : favoriteAirports) {
            for (Airport a : apiAirports) {
                if (a.id.equals(f.id)) {
                    a.setFavorite(true);
                    break;
                }
            }
        }

        boolean isEmpty = apiAirports.size() <= 0;

        if (isEmpty) {
            binding.ivAirportListNoData.setVisibility(View.VISIBLE);
            binding.tvAirportsList.setText(R.string.msg_no_airports_found);
            binding.tvAirportsList.setVisibility(View.VISIBLE);
        } else {
            binding.ivAirportListNoData.setVisibility(View.GONE);
            binding.tvAirportsList.setVisibility(View.GONE);
        }

        searchFilterDialog.dismiss();

        OnAirportClickListener listener = null;
        if (mActivity instanceof OnAirportClickListener) {
            listener = (OnAirportClickListener) mActivity;
        }

        airportsListAdapter = new AirportRecyclerAdapter(apiAirports, listener);
        binding.rvAirports.setLayoutManager(new LinearLayoutManager(mActivity));
        binding.rvAirports.setItemAnimator(new DefaultItemAnimator());
        binding.rvAirports.setAdapter(airportsListAdapter);
    }

    private void openSearchFilterDialog() {
        searchFilterDialog = new MaterialDialog.Builder(mContext)
            .title(R.string.filter_dialog_title)
            .customView(R.layout.search_airports_filter_dialog, true)
            .positiveText(R.string.filter_dialog_positive_text)
            .negativeText(R.string.cancel)
            .build();


        View view = searchFilterDialog.getCustomView();
        final EditText edAirportName = (EditText) view.findViewById(R.id.edAirportName);
        final EditText edAirportCity = (EditText) view.findViewById(R.id.edAirportCity);
        final EditText edAirportCountry = (EditText) view.findViewById(R.id.edAirportCountry);

        View positiveButton = searchFilterDialog.getActionButton(DialogAction.POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edAirportName.getText().toString();
                String city = edAirportCity.getText().toString();
                String country = edAirportCountry.getText().toString();
                //TODO: set search filters on preferences
                OFASPreferences.eraseRequestInfo(mContext);
                new AirportSearchTask().execute(name, city, country);
                searchFilterDialog.dismiss();
            }
        });

        View negativeButton = searchFilterDialog.getActionButton(DialogAction.NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFilterDialog.dismiss();
            }
        });

        searchFilterDialog.show();
    }

    SwipyRefreshLayout.OnRefreshListener onLoadMoreListener = new SwipyRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    binding.swipeBottomLoadMoreAirports.setRefreshing(false);

                    if (apiAirports != null) {
                        int currentOffset = OFASPreferences.getCurrentOffsetValue(mContext);
                        int maxResults = OFASPreferences.getCurrentSearchMaxResults(mContext);
                        if (currentOffset != 0 && maxResults - currentOffset < 10) {
                            Util.showSnackBar(mActivity, R.string.no_more_results_to_show, null);
                        } else {
                            currentOffset += 10;
                            OFASPreferences.setCurrentOffsetValue(mContext, currentOffset);
                            new AirportSearchTask().execute("", "", ""); //TODO: change for search filter saved on
                            // preferences
                        }
                    }
                }
            }, 2500);
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.airports_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterAirports:
                openSearchFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
