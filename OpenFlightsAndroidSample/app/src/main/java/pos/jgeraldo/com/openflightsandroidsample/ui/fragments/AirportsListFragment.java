package pos.jgeraldo.com.openflightsandroidsample.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.databinding.AirportsListFragmentBinding;
import pos.jgeraldo.com.openflightsandroidsample.http.AirportsParser;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;
import pos.jgeraldo.com.openflightsandroidsample.ui.adapters.AirportRecyclerAdapter;
import pos.jgeraldo.com.openflightsandroidsample.ui.listeners.OnAirportClickListener;

public class AirportsListFragment extends Fragment {

    private Activity mActivity;

    private Context mContext;

    List<Airport> mAirports;

    private MaterialDialog searchFilterDialog;

    private AirportsListFragmentBinding binding;

    public AirportsListFragment() {
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

        if (mAirports != null) {
            updateList();
        }
        return root;
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
                return AirportsParser.searchAirports(params[0], params[1], params[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Airport> airports) {
            super.onPostExecute(airports);

            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                Log.e("List", e.getMessage());
            }

            mAirports = airports;
            boolean isEmpty = mAirports == null || mAirports.size() <= 0;

            if (isEmpty) {
                binding.ivAirportListNoData.setVisibility(View.VISIBLE);
                binding.tvAirportsList.setText(R.string.msg_no_airports_found);
                binding.tvAirportsList.setVisibility(View.VISIBLE);
            } else {
                binding.ivAirportListNoData.setVisibility(View.GONE);
                binding.tvAirportsList.setVisibility(View.GONE);
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
        searchFilterDialog.dismiss();

        OnAirportClickListener listener = null;
        if (mActivity instanceof OnAirportClickListener) {
            listener = (OnAirportClickListener) getActivity();
        }

        AirportRecyclerAdapter adapter = new AirportRecyclerAdapter(mAirports, listener);
        binding.rvAirports.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAirports.setAdapter(adapter);
    }

    private void openSearchFilterDialog() {
        searchFilterDialog = new MaterialDialog.Builder(mContext)
            .title(R.string.filter_dialog_title)
            .customView(R.layout.search_airports_filter_dialog, false)
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
