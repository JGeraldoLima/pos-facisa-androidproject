package pos.jgeraldo.com.openflightsandroidsample.po;

import android.content.Context;
import android.support.design.internal.SnackbarContentLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.robotium.solo.Solo;

import pos.jgeraldo.com.openflightsandroidsample.R;

public class AirportListFragmentPO {

    private Solo solo;

    public int searchButtonId = R.id.filterAirports;

    public TextView tvAirportsList;

    public SwipyRefreshLayout swipeBottomLoadMoreAirports;
    public RecyclerView rvAirports;

    public AirportListFragmentPO(com.robotium.solo.Solo soloInstance){
        solo = soloInstance;
        loadFields();
    }

    private void loadFields() {
        tvAirportsList = (TextView) solo.getView(R.id.tvAirportsList);

        swipeBottomLoadMoreAirports = (SwipyRefreshLayout) solo.getView(R.id.swipeBottomLoadMoreAirports);
        rvAirports = (RecyclerView) solo.getView(R.id.rvAirports);
    }

    public void clickOnFilterButton() {
        solo.clickOnView(solo.getView(searchButtonId));
    }

    public void confirmSearch(Context context) {
        solo.clickOnText(context.getString(R.string.filter_dialog_positive_text));
    }

    public void insertAirportNameField(String name) {
        solo.typeText(0, name);
    }

    public String getSnackbarText() {
        SnackbarContentLayout snackbarLayout = ((SnackbarContentLayout) solo.getViews().get(30));
        return ((AppCompatTextView) snackbarLayout.getChildAt(0)).getText().toString();
    }

    public void swipeUpToLoadMore() {
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        int fromX = width/2;
        int toX = width/2;
        int fromY = (height * 17)/20; // (17/20) == 85% of the screen height.
        int toY = (height * 7)/25;  // (7/25) == 28% of the screen height.
        solo.drag(fromX, toX, fromY, toY, 50); // stepCount over 50, so we can see the movement.
    }
}
