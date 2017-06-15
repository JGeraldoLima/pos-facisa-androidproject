package pos.jgeraldo.com.openflightsandroidsample.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import pos.jgeraldo.com.openflightsandroidsample.R;

public class Util {

    public static String getString(final Context context, final int id) {
        return context.getResources().getString(id);
    }

    public static void showSnackBar(Activity activity, int msgId, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), getString(activity, msgId),
            Snackbar.LENGTH_LONG).setAction(R.string.undo, listener);

        View sbView = snackbar.getView();

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) sbView.getLayoutParams();
        params.gravity = (Gravity.BOTTOM | Gravity.RIGHT);
        sbView.setLayoutParams(params);

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(activity.getResources().getColor(android.R.color.white));
        textView.setMaxLines(4);

        snackbar.show();
    }

    public static void openAlertDialog(final Activity activity, String msgToShow, final boolean finishActivity) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
            .title(R.string.notice)
            .content(msgToShow)
            .positiveColorRes(R.color.colorPrimaryDark)
            .positiveText("OK")  // TODO: waiting for @afollestad fix on MaterialDialogs lib
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    if (finishActivity) {
                        activity.finish();
                    }
                    dialog.dismiss();
                }
            });

        MaterialDialog alertDialog = builder.build();
        alertDialog.show();
    }

    public static void openQuestionAlertDialog(final Activity activity, int msgId, final boolean
        finishActivity) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
            .title(R.string.notice)
            .content(getString(activity, msgId))
            .positiveColorRes(R.color.colorPrimaryDark)
            .negativeColorRes(R.color.colorPrimaryDark)
            .positiveText("OK") // TODO: waiting for @afollestad fix on MaterialDialogs lib
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    if (finishActivity) {
                        activity.finish();
                    }
                    dialog.dismiss();
                }
            })
            .negativeText(R.string.cancel)
            .onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            });

        MaterialDialog alertDialog = builder.build();
        alertDialog.show();
    }

    public static boolean isConnected(final Context context) {
        boolean connected = false;
        final ConnectivityManager conectivityManager = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            connected = true;
        }
        return connected;
    }
}
