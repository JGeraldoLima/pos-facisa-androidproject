package pos.jgeraldo.com.openflightsandroidsample.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.http.AirportsParser;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.AirportGson;
import pos.jgeraldo.com.openflightsandroidsample.ui.adapters.AirportRecyclerAdapter;

public class AirportsListFragment extends Fragment {

    private Activity mActivity;

    private Context mContext;

    @BindView(R.id.ibFilterSearch)
    ImageButton ibFilterSearch;

    @BindView(R.id.rvAirports)
    RecyclerView rvAirports;

    @BindView(R.id.ivAirportListNoData)
    ImageView ivAirportListNoData;

    @BindView(R.id.tvAirportsList)
    TextView tvAirportsList;

    List<AirportGson> mAirports;

    private MaterialDialog searchFilterDialog;

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
        View view = inflater.inflate(R.layout.fragment_airport_list, container, false);
        ButterKnife.bind(this, view);



        if (mAirports != null) {
            updateList();
        }
        return view;
    }

    private class AirportSearchTask extends AsyncTask<String, Void, List<AirportGson>> {

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
        protected List<AirportGson> doInBackground(String... params) {
            try {
                return AirportsParser.searchAirports(params[0], params[1], params[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AirportGson> airports) {
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
                ivAirportListNoData.setVisibility(View.VISIBLE);
                tvAirportsList.setText(R.string.msg_no_airports_found);
                tvAirportsList.setVisibility(View.VISIBLE);
            } else {
                ivAirportListNoData.setVisibility(View.GONE);
                tvAirportsList.setVisibility(View.GONE);
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
        protected void onCancelled(List<AirportGson> airportGsons) {
            super.onCancelled(airportGsons);
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                Log.e("List", e.getMessage());
            }
        }
    }

    private void updateList() {
        searchFilterDialog.dismiss();
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

    @OnClick(R.id.ibFilterSearch)
    void onClick(){
        openSearchFilterDialog();
    }
}
