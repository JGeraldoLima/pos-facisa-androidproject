package pos.jgeraldo.com.openflightsandroidsample;

import android.content.Context;
import android.support.design.internal.SnackbarContentLayout;
import android.support.test.InstrumentationRegistry;
import android.support.v7.widget.AppCompatTextView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.robotium.solo.Solo;

import pos.jgeraldo.com.openflightsandroidsample.po.AirportListFragmentPO;
import pos.jgeraldo.com.openflightsandroidsample.ui.activities.MainActivity;

public class AirportListFragmentInstrumentedTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    Context context;
    private AirportListFragmentPO po;

    public AirportListFragmentInstrumentedTest() {
        super(MainActivity.class);
    }

    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        solo = new Solo(getInstrumentation(), getActivity());
        po = new AirportListFragmentPO(solo);
    }

    public void testNoSearchExecuted() throws Exception {
        String snackbarText = po.getSnackbarText();

        assertEquals(snackbarText, context.getString(R.string.available_network_connection));
        assertTrue(solo.searchText(context.getString(R.string.airports_list_welcome_msg)));
    }

    public void testSearchWithoutFilter() throws Exception {
        po.clickOnFilterButton();
        po.confirmSearch(context);

        solo.waitForDialogToClose();

        assertTrue(po.rvAirports.getAdapter().getItemCount() == 10); // the API provides a 10-items paginated response
    }

    public void testSearchWithFilter() throws Exception {
        po.clickOnFilterButton();

        po.insertAirportNameField("Zumbi");
        po.confirmSearch(context);

        solo.waitForDialogToClose();

        assertTrue(po.rvAirports.getAdapter().getItemCount() == 1);
    }

    public void testSearchWithFilterNoResults() throws Exception {
        po.clickOnFilterButton();

        po.insertAirportNameField("XPTO");
        po.confirmSearch(context);

        solo.waitForDialogToClose();

        assertTrue(po.rvAirports.getAdapter().getItemCount() == 0);

        assertTrue(po.tvAirportsList.getVisibility() == View.VISIBLE);
        assertEquals(po.tvAirportsList.getText().toString(), context.getString(R.string.msg_no_airports_found));
    }

    public void testLoadMore() throws Exception {
        po.clickOnFilterButton();
        po.confirmSearch(context);

        solo.waitForDialogToClose();

        assertTrue(po.rvAirports.getAdapter().getItemCount() == 10); // the API provides a 10-items paginated response

        solo.scrollToBottom();
        solo.scrollToBottom();

        po.swipeUpToLoadMore();

        solo.waitForDialogToOpen();
        solo.waitForDialogToClose();

        assertTrue(po.rvAirports.getAdapter().getItemCount() == 20); // the API provides a 10-items paginated response
    }

    public void tearDown() {
        solo.finishOpenedActivities();
    }
}
