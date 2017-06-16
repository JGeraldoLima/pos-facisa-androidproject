package pos.jgeraldo.com.openflightsandroidsample.storage.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import pos.jgeraldo.com.openflightsandroidsample.utils.Constants;

@SuppressLint("CommitPrefEdits")
public class OFASPreferences {

    private static SharedPreferences ofasPreferences;

    private static SharedPreferences.Editor ofasPreferenvesEditor;

    private static void initiatePreferencesIfNull(Context context) {
        if (ofasPreferences == null) {
            ofasPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            ofasPreferenvesEditor = ofasPreferences.edit();
        }
    }

    private static void saveChanges() {
        ofasPreferenvesEditor.commit();
        ofasPreferenvesEditor.apply();
    }

    public static void eraseRequestInfo(Context context) {
        setCurrentOffsetValue(context, 0);
        setCurrentSearchMaxResults(context, 0);
    }

    public static int getCurrentOffsetValue(Context context) {
        initiatePreferencesIfNull(context);
        return ofasPreferences.getInt(Constants.PREFS_OFFSET, 0);
    }

    public static int getCurrentSearchMaxResults(Context context) {
        initiatePreferencesIfNull(context);
        return ofasPreferences.getInt(Constants.PREFS_MAX_RESULTS, 0);
    }

    public static void setCurrentOffsetValue(Context context, int offset) {
        initiatePreferencesIfNull(context);
        ofasPreferenvesEditor.putInt(Constants.PREFS_OFFSET, offset);
        saveChanges();
    }

    public static void setCurrentSearchMaxResults(Context context, int maxResults) {
        initiatePreferencesIfNull(context);
        ofasPreferenvesEditor.putInt(Constants.PREFS_MAX_RESULTS, maxResults);
        saveChanges();
    }
}