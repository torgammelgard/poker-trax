package se.torgammelgard.pokertrax;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import se.torgammelgard.pokertrax.Adapters.TabsPagerAdapter;
import se.torgammelgard.pokertrax.util.IabHelper;
import se.torgammelgard.pokertrax.util.IabResult;
import se.torgammelgard.pokertrax.util.Inventory;
import se.torgammelgard.pokertrax.util.Purchase;

public class TabMain_FragmentActivity extends FragmentActivity {

    protected Menu mMenu;
    private String[] mTab_names;

    private static final int ADD_SESSION_REQUEST = 1;
    private static final String LOG = "TabMain";
    private static final String PREFS_NAME = "MyPreferences";
    private final static String PREMIUM_USER = "Premium User";

    boolean isPremiumUser = false;

    IabHelper mHelper;
    static final String SKU_PREMIUM = "android.test.purchased";
    static final int RC_REQUEST = 10001;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener =
            new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                    if (result.isFailure()) {
                        //TODO: handle error
                        Log.d(LOG, "purchase failure");
                        return;
                    } else if (purchase.getSku().equals(SKU_PREMIUM)) {
                        consumeItem();
                    }
                }
            };
    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener =
            new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (result.isFailure()) {
                        //TODO: handle failure
                        return;
                    } else {
                        mHelper.consumeAsync(inv.getPurchase(SKU_PREMIUM), mConsumeFinishedListener);
                    }
                }
            };
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                @Override
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(PREMIUM_USER, true);
                        editor.apply();
                        isPremiumUser = true;
                        updateUI();
                        Log.d(LOG, "Great success, purchase successful!");
                    } else {
                        //TODO: handle failure
                        Log.d(LOG, "Purchase not successful");
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* in app stuff */
        doInAppInits();
        isPremiumUser = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean(PREMIUM_USER, false);
        if (!isPremiumUser) {
            //check with store if the user really isn't premium
        }

        mTab_names = getResources().getStringArray(R.array.tab_names);

        ViewPager viewPager = findViewById(R.id.pager);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
            actionBar.setSubtitle(R.string.app_subtitle);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setHomeButtonEnabled(false);

            TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, viewPager);
            tabsPagerAdapter.addTab(actionBar.newTab().setText(mTab_names[0]));
            tabsPagerAdapter.addTab(actionBar.newTab().setText(mTab_names[1]));
            tabsPagerAdapter.addTab(actionBar.newTab().setText(mTab_names[2]));
            tabsPagerAdapter.addTab(actionBar.newTab().setText(mTab_names[3]));
        }

    }


    private void doInAppInits() {
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKnKmAezW5NwddMOyi+YS3shhtdhZn6gYqtpcXSRleu1JVvw3EgLW5Be3IJwu7/BGwq2/AVbrBvQ+SGhEPjKJY/cMjfuzsjHT02HQxTUxPAoLnPrRHdWl5x4yBnD1i3P75GPEgF9MAwGJKPV7+CpaPtRV4qJppDGArh8sgCfETpMUDTatZcsr9qKKQZm7+1xjgOtkZnyRon+7QMpmlGqgHTqz9qTFxoBaf2lfimOQMjybXp34gHdKUN8oUTzvV1VHUnBLBkPFfGDK/gH2nzgTdVCpAPU5aWbsdvVhMufXJOXZym9eneeUpiNVmrfVh0OkGLYTfL0GJrSYpMRp5qwgQIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(LOG, "In-app Billing setup failed: " + result);
                } else {
                    Log.d(LOG, "In-app Billing setup is OK");
                }
            }
        });
    }

    public void buyPremium(View view) {
        mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, "mypurchasetoken");
    }
    private void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.delete_session).setVisible(false);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings :

                return false;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            ((MainApp) getApplication()).mDataSource.close();
        } catch (NullPointerException e) {
            Log.d(LOG, "Data source is null");
        }
    }

   /** Starts an activity where the user can add a Session */
    public void addSessionOnClick(View view) {
        if (!isPremiumUser) {
            int entries = ((MainApp) getApplication()).mDataSource.getEntriesCount();
            if (entries > 4) {
                Toast.makeText(this, "Buy premium version for unlimited session entries",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
        Intent intent = new Intent(this, AddSessionActivity.class);
        startActivityForResult(intent, ADD_SESSION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // note : onActivityResult is run before onResume
        if (resultCode == RESULT_CANCELED)
            return;
        switch (requestCode) {
            case ADD_SESSION_REQUEST :
                break;
            default: break;
        }
    }

    /** Updates the listView in the ResultsFragment */
    public void updateUI() {
        if (isPremiumUser) {
            final Button buyPremiumButton = findViewById(R.id.buy_premium);
            if (buyPremiumButton != null)
                buyPremiumButton.setVisibility(View.GONE);
        }

        TextView premiumTextView = findViewById(R.id.textView_premium);
        if (premiumTextView != null)
            premiumTextView.setVisibility(isPremiumUser ? View.VISIBLE : View.GONE);

        ResultsFragment resultsFragment = (ResultsFragment) getSupportFragmentManager().findFragmentById(R.id.list_result);

        if (resultsFragment != null) {
            resultsFragment.updateListView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }
}
