package se.torgammelgard.pokertrax

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import se.torgammelgard.pokertrax.Adapters.TabsPagerAdapter
import se.torgammelgard.pokertrax.util.IabHelper

class TabMain_FragmentActivity : FragmentActivity() {

    var mMenu: Menu? = null
    private var mTab_names: Array<String>? = null

    internal var isPremiumUser = false

    internal var mHelper: IabHelper? = null
    internal var mPurchaseFinishedListener: IabHelper.OnIabPurchaseFinishedListener = IabHelper.OnIabPurchaseFinishedListener { result, purchase ->
        if (result.isFailure) {
            //TODO: handle error
            Log.d(LOG, "purchase failure")
            return@OnIabPurchaseFinishedListener
        } else if (purchase.sku == SKU_PREMIUM) {
            consumeItem()
        }
    }
    internal var mReceivedInventoryListener: IabHelper.QueryInventoryFinishedListener = IabHelper.QueryInventoryFinishedListener { result, inv ->
        if (result.isFailure) {
            //TODO: handle failure
            return@QueryInventoryFinishedListener
        } else {
            mHelper!!.consumeAsync(inv.getPurchase(SKU_PREMIUM), mConsumeFinishedListener)
        }
    }
    internal var mConsumeFinishedListener: IabHelper.OnConsumeFinishedListener = IabHelper.OnConsumeFinishedListener { purchase, result ->
        if (result.isSuccess) {
            val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putBoolean(PREMIUM_USER, true)
            editor.apply()
            isPremiumUser = true
            updateUI()
            Log.d(LOG, "Great success, purchase successful!")
        } else {
            //TODO: handle failure
            Log.d(LOG, "Purchase not successful")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        /* in app stuff */
        doInAppInits()
        isPremiumUser = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(PREMIUM_USER, false)
        if (!isPremiumUser) {
            //check with store if the user really isn't premium
        }

        mTab_names = resources.getStringArray(R.array.tab_names)

        val viewPager = findViewById<ViewPager>(R.id.pager)
        val actionBar = actionBar
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name)
            actionBar.setSubtitle(R.string.app_subtitle)
            actionBar.navigationMode = ActionBar.NAVIGATION_MODE_TABS
            actionBar.setHomeButtonEnabled(false)

            val tabsPagerAdapter = TabsPagerAdapter(this, viewPager)
            tabsPagerAdapter.addTab(actionBar.newTab().setText(mTab_names!![0]))
            tabsPagerAdapter.addTab(actionBar.newTab().setText(mTab_names!![1]))
            tabsPagerAdapter.addTab(actionBar.newTab().setText(mTab_names!![2]))
            tabsPagerAdapter.addTab(actionBar.newTab().setText(mTab_names!![3]))
        }
    }


    private fun doInAppInits() {
        val base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKnKmAezW5NwddMOyi+YS3shhtdhZn6gYqtpcXSRleu1JVvw3EgLW5Be3IJwu7/BGwq2/AVbrBvQ+SGhEPjKJY/cMjfuzsjHT02HQxTUxPAoLnPrRHdWl5x4yBnD1i3P75GPEgF9MAwGJKPV7+CpaPtRV4qJppDGArh8sgCfETpMUDTatZcsr9qKKQZm7+1xjgOtkZnyRon+7QMpmlGqgHTqz9qTFxoBaf2lfimOQMjybXp34gHdKUN8oUTzvV1VHUnBLBkPFfGDK/gH2nzgTdVCpAPU5aWbsdvVhMufXJOXZym9eneeUpiNVmrfVh0OkGLYTfL0GJrSYpMRp5qwgQIDAQAB"

        mHelper = IabHelper(this, base64EncodedPublicKey)

        mHelper!!.startSetup { result ->
            if (!result.isSuccess) {
                Log.d(LOG, "In-app Billing setup failed: " + result)
            } else {
                Log.d(LOG, "In-app Billing setup is OK")
            }
        }
    }

    fun buyPremium(view: View) {
        mHelper!!.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, "mypurchasetoken")
    }

    private fun consumeItem() {
        mHelper!!.queryInventoryAsync(mReceivedInventoryListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        mMenu = menu
        menuInflater.inflate(R.menu.main, menu)
        menu.findItem(R.id.delete_session).isVisible = false
        return true
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings ->

                return false
            else -> return super.onMenuItemSelected(featureId, item)
        }
    }

    override fun onResume() {
        super.onResume()

        updateUI()
    }

    override fun onPause() {
        super.onPause()

        try {
            (application as MainApp).mDataSource!!.close()
        } catch (e: NullPointerException) {
            Log.d(LOG, "Data source is null")
        }

    }

    /** Starts an activity where the user can add a Session  */
    fun addSessionOnClick(view: View) {
        if (!isPremiumUser) {
            val entries = (application as MainApp).mDataSource!!.entriesCount
            if (entries > 4) {
                Toast.makeText(this, "Buy premium version for unlimited session entries",
                        Toast.LENGTH_LONG).show()
                return
            }
        }
        val intent = Intent(this, AddSessionActivity::class.java)
        startActivityForResult(intent, ADD_SESSION_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!mHelper!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
        // note : onActivityResult is run before onResume
        if (resultCode == Activity.RESULT_CANCELED)
            return
        when (requestCode) {
            ADD_SESSION_REQUEST -> {
            }
            else -> {
            }
        }
    }

    /** Updates the list view in the ResultsFragment  */
    fun updateUI() {
        if (isPremiumUser) {
            val buyPremiumButton = findViewById<Button>(R.id.buy_premium)
            if (buyPremiumButton != null)
                buyPremiumButton.visibility = View.GONE
        }

        val premiumTextView = findViewById<TextView>(R.id.textView_premium)
        if (premiumTextView != null)
            premiumTextView.visibility = if (isPremiumUser) View.VISIBLE else View.GONE

        val resultsFragment: ResultsFragment? = supportFragmentManager.findFragmentById(R.id.list_result) as? ResultsFragment

        resultsFragment?.updateListView()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mHelper != null) {
            mHelper!!.dispose()
            mHelper = null
        }
    }

    companion object {

        private val ADD_SESSION_REQUEST = 1
        private val LOG = "TabMain"
        private val PREFS_NAME = "MyPreferences"
        private val PREMIUM_USER = "Premium User"
        internal val SKU_PREMIUM = "android.test.purchased"
        internal val RC_REQUEST = 10001
    }
}
