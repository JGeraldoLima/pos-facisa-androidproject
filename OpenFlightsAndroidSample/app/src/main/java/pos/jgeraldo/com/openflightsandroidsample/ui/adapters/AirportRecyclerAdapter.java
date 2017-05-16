package pos.jgeraldo.com.openflightsandroidsample.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.AirportGson;

public class AirportRecyclerAdapter extends RecyclerView.Adapter<AirportRecyclerAdapter.ViewHolder> {

    List<AirportGson> mAirports;
    OnAirportClickListener clickListener;

    public AirportRecyclerAdapter(List<AirportGson> airports,
                                  OnAirportClickListener listener) {
        this.mAirports = airports;
        this.clickListener = listener;
    }

    @Override
    public AirportRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.airport_item, parent, false);

        final ViewHolder vh = new ViewHolder(v);
        vh.itemView.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int pos = vh.getAdapterPosition();
                        AirportGson airport = mAirports.get(pos);
                        clickListener.onAirportClick(airport);
                    }
                }
            });
        return vh;
    }

    @Override
    public void onBindViewHolder(AirportRecyclerAdapter.ViewHolder holder, int position) {
        AirportGson airport = mAirports.get(position);
        holder.tvAirportName.setText(airport.shortName);
        holder.tvAirportCountry.setText(String.format("%s - %s", airport.countryName, airport.countryCode));
    }

    @Override
    public int getItemCount() {
        return mAirports != null ? mAirports.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAirportName)
        TextView tvAirportName;
        @BindView(R.id.tvAirportCountry)
        TextView tvAirportCountry;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnAirportClickListener {
        void onAirportClick(AirportGson airport);
    }
}
