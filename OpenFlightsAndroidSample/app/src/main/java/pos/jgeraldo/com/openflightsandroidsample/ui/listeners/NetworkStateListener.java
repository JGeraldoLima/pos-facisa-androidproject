package pos.jgeraldo.com.openflightsandroidsample.ui.listeners;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import pos.jgeraldo.com.openflightsandroidsample.R;
import pos.jgeraldo.com.openflightsandroidsample.utils.NetworkUtil;
import pos.jgeraldo.com.openflightsandroidsample.utils.Util;

public class NetworkStateListener extends BroadcastReceiver {

    private Activity mActivity;

    public NetworkStateListener(final Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);
        if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
//            MenuItem searchFilterIcon = (MenuItem) mActivity.findViewById(R.id.filterAirports);
//            ImageView ivAirportListNoData = (ImageView) mActivity.findViewById(R.id.ivAirportListNoData);
            if (status == NetworkUtil.NETWORK_STATUS_WIFI || status == NetworkUtil.NETWORK_STATUS_MOBILE) {
//                searchFilterIcon.setIcon(R.drawable.ic_filter_search);
//                ivAirportListNoData.setVisibility(View.GONE);
                Util.showSnackBar(mActivity, R.string.available_network_connection, null);
                NetworkUtil.HAS_CONNECTION = true;
            } else {
                Util.showSnackBar(mActivity, R.string.unavailable_network_connection, null);
//                ivAirportListNoData.setVisibility(View.VISIBLE);
//                ivAirportListNoData.setImageResource(R.drawable.ic_no_connection);
//                searchFilterIcon.setIcon(R.drawable.ic_filter_disabled);
//                searchFilterIcon.setEnabled(false);
                NetworkUtil.HAS_CONNECTION = false;
            }
        }
    }
}