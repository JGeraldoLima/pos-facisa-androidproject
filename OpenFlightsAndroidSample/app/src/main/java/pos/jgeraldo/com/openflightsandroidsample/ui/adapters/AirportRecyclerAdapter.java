package pos.jgeraldo.com.openflightsandroidsample.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.databinding.AirportListItemBinding;
import pos.jgeraldo.com.openflightsandroidsample.storage.models.Airport;

public class AirportRecyclerAdapter extends RecyclerView.Adapter<AirportRecyclerAdapter.ViewHolder> {

    List<Airport> mAirports;
    OnAirportClickListener clickListener;

    public AirportRecyclerAdapter(List<Airport> airports,
                                  OnAirportClickListener listener) {
        this.mAirports = airports;
        this.clickListener = listener;
    }

    @Override
    public AirportRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AirportListItemBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.airport_list_item, parent, false);

        final ViewHolder vh = new ViewHolder(binding);
        vh.itemView.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int pos = vh.getAdapterPosition();
                        Airport airport = mAirports.get(pos);
                        clickListener.onAirportClick(airport);
                    }
                }
            });
        return vh;
    }

    @Override
    public void onBindViewHolder(AirportRecyclerAdapter.ViewHolder holder, int position) {
        Airport airport = mAirports.get(position);
        holder.binding.setAirport(airport);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mAirports != null ? mAirports.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AirportListItemBinding binding;

        ViewHolder(AirportListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnAirportClickListener {
        void onAirportClick(Airport airport);
    }
}
