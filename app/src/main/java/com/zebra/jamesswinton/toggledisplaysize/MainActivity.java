package com.zebra.jamesswinton.toggledisplaysize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKManager.FEATURE_TYPE;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.EMDKResults.STATUS_CODE;
import com.symbol.emdk.ProfileManager;

import static com.zebra.jamesswinton.toggledisplaysize.App.CUSTOM_PROFILE;
import static com.zebra.jamesswinton.toggledisplaysize.App.CUSTOM_PROFILE_XML;
import static com.zebra.jamesswinton.toggledisplaysize.App.DEFAULT_PROFILE;
import static com.zebra.jamesswinton.toggledisplaysize.App.DEFAULT_PROFILE_XML;

public class MainActivity extends AppCompatActivity implements EMDKListener {

    // Debugging
    private static final String TAG = "MainActivity";

    // Constants
    private static final String MAPPING_STATE = "mapping-state";
    private static final String IS_CUSTOM_STATE = "is-custom-state";

    // Static Variables
    private EMDKManager mEmdkManager = null;
    private ProfileManager mProfileManager = null;

    // Non-Static Variables
    private Boolean mIsCustomState = null;
    private SharedPreferences mSharedPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Validate First Run
        if (savedInstanceState == null) {
            // Init Shared Prefs
            mSharedPrefs = getSharedPreferences(MAPPING_STATE, 0);
            mIsCustomState = mSharedPrefs.getBoolean(IS_CUSTOM_STATE, false);

            // Init EMDK
            EMDKResults emdkManagerResults = EMDKManager.getEMDKManager(this, this);

            // Verify EMDK Manager
            if (emdkManagerResults == null || emdkManagerResults.statusCode != STATUS_CODE.SUCCESS) {
                // Log Error
                Log.e(TAG, "onCreate: Failed to get EMDK Manager -> " +
                        (emdkManagerResults == null ? "No Results Returned" : emdkManagerResults.statusCode));
                Toast.makeText(this, "Failed to get EMDK Manager!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        // Assign EMDK Reference
        mEmdkManager = emdkManager;

        // Get Profile & Version Manager Instances
        mProfileManager = (ProfileManager) mEmdkManager.getInstance(FEATURE_TYPE.PROFILE);

        // Apply Profile
        if (mProfileManager != null) {
            applyKeyMappingProfile();
        } else {
            Log.e(TAG, "Error Obtaining ProfileManager!");
            Toast.makeText(this, "Error Obtaining ProfileManager!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onClosed() {
        // Release EMDK Manager Instance
        if (mEmdkManager != null) {
            mEmdkManager.release();
            mEmdkManager = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release EMDK Manager Instance
        if (mEmdkManager != null) {
            mEmdkManager.release();
            mEmdkManager = null;
        }
    }

    private void applyKeyMappingProfile() {
        String[] params = new String[1];
        if (mIsCustomState) {
            params[0] = DEFAULT_PROFILE_XML;
            new ProcessProfile(DEFAULT_PROFILE, mProfileManager, onProfileApplied).execute(params);
        } else {
            params[0] = CUSTOM_PROFILE_XML;
            new ProcessProfile(CUSTOM_PROFILE, mProfileManager, onProfileApplied).execute(params);
        }
    }

    private OnProfileApplied onProfileApplied = new OnProfileApplied() {
        @Override
        public void profileApplied() {
            // Notify User
            Toast.makeText(MainActivity.this,
                    (mIsCustomState ? "Default Font Size Applied" : "Small Font Size Applied"),
                    Toast.LENGTH_SHORT).show();

            // Update Pref
            SharedPreferences.Editor editor = mSharedPrefs.edit();
            editor.putBoolean(IS_CUSTOM_STATE, !mIsCustomState);

            // Store Pref
            if (!editor.commit()) {
                Log.e(TAG, "Couldn't save state to shared prefs");
            }

            // Exit App
            finish();
        }

        @Override
        public void profileError() {
            Log.e(TAG, "Error Processing Profile!");
            Toast.makeText(MainActivity.this, "Error Applying Profile!", Toast.LENGTH_SHORT)
                    .show();

            // Applied -> Exit App
            finish();
        }
    };
}
