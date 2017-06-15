package pos.jgeraldo.com.openflightsandroidsample.ui.listeners;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
//        if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
//            Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar_main);
//            if (status == NetworkUtil.NETWORK_STATUS_WIFI || status == NetworkUtil.NETWORK_STATUS_MOBILE){
//                toolbar.findViewById(R.id.icNetworkUnavailable).setVisibility(View.INVISIBLE);
//                Util.showSnackBar(mActivity, R.string.user_connected);
//                NetworkUtil.HAS_CONNECTION = true;
//            } else {
//                Util.showSnackBar(mActivity,R.string.network_unavailable_status);
//                toolbar.findViewById(R.id.icNetworkUnavailable).setVisibility(View.VISIBLE);
//                NetworkUtil.HAS_CONNECTION = false;
//            }
    }
}