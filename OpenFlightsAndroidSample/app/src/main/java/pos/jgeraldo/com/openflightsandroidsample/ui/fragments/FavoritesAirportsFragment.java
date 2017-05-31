package pos.jgeraldo.com.openflightsandroidsample.ui.fragments;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.databinding.FragmentAirportsListBinding;
import pos.jgeraldo.com.openflightsandroidsample.storage.dao.AppDatabase;
import pos.jgeraldo.com.openflightsandroidsample.storage.dao.IAirportDao;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;
import pos.jgeraldo.com.openflightsandroidsample.ui.adapters.AirportRecyclerAdapter;
import pos.jgeraldo.com.openflightsandroidsample.ui.listeners.OnAirportClickListener;

public class FavoritesAirportsFragment extends LifecycleFragment {

    LiveData<List<Airport>> liveAirports;

    FragmentAirportsListBinding binding;

    IAirportDao dao;

    public FavoritesAirportsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dao = AppDatabase.getInMemoryDatabase(getContext()).airportDao();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_airports_list, container, false);
        binding.tvAirportsList.setText(R.string.no_favorites_at_moment);

        new FavoritesAirportsTask().execute();

        return binding.getRoot();
    }

    private class FavoritesAirportsTask extends AsyncTask<String, Void, LiveData<List<Airport>>> {

        @Override
        protected LiveData<List<Airport>> doInBackground(String... params) {
            return dao.listAllFavorites();
        }

        @Override
        protected void onPostExecute(LiveData<List<Airport>> m) {
            super.onPostExecute(m);
            liveAirports = m;
            liveAirports.observe(FavoritesAirportsFragment.this, new Observer<List<Airport>>() {
                @Override
                public void onChanged(@Nullable List<Airport> airports) {
                    boolean isEmpty = airports == null || airports.size() <= 0;
//                    binding.txtEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                    updateList(airports);
                }
            });

        }
    }

    private void updateList(List<Airport> airports) {
        OnAirportClickListener listener = null;
        if (getActivity() instanceof OnAirportClickListener) {
            listener = (OnAirportClickListener) getActivity();
        }

        AirportRecyclerAdapter adapter = new AirportRecyclerAdapter(airports, listener);
        binding.rvAirports.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAirports.setAdapter(adapter);
    }
}
