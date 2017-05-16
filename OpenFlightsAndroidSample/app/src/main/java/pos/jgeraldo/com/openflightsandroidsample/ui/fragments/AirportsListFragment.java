package pos.jgeraldo.com.openflightsandroidsample.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.http.AirportsParser;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.AirportGson;
import pos.jgeraldo.com.openflightsandroidsample.ui.adapters.AirportRecyclerAdapter;

public class AirportsListFragment extends Fragment implements SearchView.OnQueryTextListener {

    @BindView(R.id.rvAirports)
    RecyclerView rvAirports;

    @BindView(R.id.tvNoAirportsFound)
    TextView tvNoAirportsFound;

    List<AirportGson> mAirports;

    public AirportsListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airport_list, container, false);
        ButterKnife.bind(this, view);

        if (mAirports != null) {
            updateList();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.airports_searchbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO: find a proper way to implement query with multiple filters
        new AirportSearchTask().execute(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class AirportSearchTask extends AsyncTask<String, Void, List<AirportGson>> {

        @Override
        protected List<AirportGson> doInBackground(String... params) {
            try {
                return AirportsParser.searchAirports("", "", params[0], "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AirportGson> airports) {
            super.onPostExecute(airports);
            mAirports = airports;
            boolean isEmpty = mAirports == null || mAirports.size() <= 0;
            tvNoAirportsFound.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            updateList();
        }
    }

    private void updateList() {
        AirportRecyclerAdapter adapter = new AirportRecyclerAdapter(mAirports, new AirportRecyclerAdapter
            .OnAirportClickListener() {
            @Override
            public void onAirportClick(AirportGson airport) {
                // Call detail activity
                // use x and y airports fields to load GoogleMaps position marker
            }
        });
        rvAirports.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAirports.setAdapter(adapter);
    }
}
