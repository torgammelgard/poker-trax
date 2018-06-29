package se.torgammelgard.pokertrax.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import org.jetbrains.anko.*
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.fragments.GraphFragment
import se.torgammelgard.pokertrax.fragments.SessionsFragment
import se.torgammelgard.pokertrax.fragments.SummaryFragment
import se.torgammelgard.pokertrax.model.repositories.SessionRepository
import se.torgammelgard.pokertrax.util.IabHelper
import javax.inject.Inject

/**
 * Main activity, responsible to be informative and simple to use
 */
class MainActivity : FragmentActivity(), HasSupportFragmentInjector, AnkoLogger {

    companion object {
        const val SESSIONS_FRAG_TAG = "sessions_fragment"
        const val GRAPH_FRAG_TAG = "graph_fragment"
        const val SUMMARY_FRAG_TAG = "summary_fragment"

        private const val ADD_SESSION_REQUEST = 1
        private const val PREFS_NAME = "MyPreferences"
        private const val PREMIUM_USER = "Premium User"
        internal const val SKU_PREMIUM = "android.test.purchased"
        internal const val RC_REQUEST = 10001
    }

    @Inject
    lateinit var sessionRepository: SessionRepository

    private var isPremiumUser = false

    private var mHelper: IabHelper? = null
    private var mPurchaseFinishedListener = IabHelper.OnIabPurchaseFinishedListener { result, purchase ->
        if (result.isFailure) {
            //TODO: handle error
            error {
                "Error purchasing: $result"
            }
            return@OnIabPurchaseFinishedListener
        } else if (purchase?.sku == TabMainFragmentActivity.SKU_PREMIUM) {
            consumeItem()
        }
    }

    private fun consumeItem() {
        mHelper!!.queryInventoryAsync(mReceivedInventoryListener)
    }

    @Suppress("UNUSED_PARAMETER")
    fun buyPremium(view: View) {
        mHelper!!.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, "mypurchasetoken")
    }

    private var mReceivedInventoryListener = IabHelper.QueryInventoryFinishedListener { result, inv ->
        if (result.isFailure) {
            //TODO: handle failure
            return@QueryInventoryFinishedListener
        } else {
            mHelper!!.consumeAsync(inv.getPurchase(TabMainFragmentActivity.SKU_PREMIUM), mConsumeFinishedListener)
        }
    }
    private var mConsumeFinishedListener = IabHelper.OnConsumeFinishedListener { _, result ->
        if (result.isSuccess) {
            val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putBoolean(PREMIUM_USER, true)
            editor.apply()
            isPremiumUser = true
            // TODO update UI here
            info {
                "Great success, purchase successful!"
            }
        } else {
            //TODO: handle failure
            info {
                "Purchase not successful"
            }
        }
    }

    private lateinit var addSessionFAB: FloatingActionButton
    private lateinit var buyPremiumButton: Button

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        /* in app stuff */
        doInAppInit()
        isPremiumUser = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(PREMIUM_USER, false)
        if (!isPremiumUser) {
            //check with store if the user really isn't premium
        }

        addSessionFAB = findViewById(R.id.floatingActionButton)
        buyPremiumButton = findViewById(R.id.buy_premium_button)

        // BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)

        // init sessions fragment
        val sessionFragment = SessionsFragment()
        supportFragmentManager.beginTransaction().add(sessionFragment, "sessions").commit()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_item_sessions -> {
                    replaceFragment(SessionsFragment(), SESSIONS_FRAG_TAG)
                    addSessionFAB.show()
                }
                R.id.bottom_nav_item_graph -> {
                    replaceFragment(GraphFragment(), GRAPH_FRAG_TAG)
                    addSessionFAB.hide()
                }
                R.id.bottom_nav_item_summary -> {
                    replaceFragment(SummaryFragment(), SUMMARY_FRAG_TAG)
                    addSessionFAB.hide()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        bottomNavigationView.selectedItemId = R.id.bottom_nav_item_sessions
    }

    private fun doInAppInit() {
        val base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKnKmAezW5NwddMOyi+YS3shhtdhZn6gYqtpcXSRleu1JVvw3EgLW5Be3IJwu7/BGwq2/AVbrBvQ+SGhEPjKJY/cMjfuzsjHT02HQxTUxPAoLnPrRHdWl5x4yBnD1i3P75GPEgF9MAwGJKPV7+CpaPtRV4qJppDGArh8sgCfETpMUDTatZcsr9qKKQZm7+1xjgOtkZnyRon+7QMpmlGqgHTqz9qTFxoBaf2lfimOQMjybXp34gHdKUN8oUTzvV1VHUnBLBkPFfGDK/gH2nzgTdVCpAPU5aWbsdvVhMufXJOXZym9eneeUpiNVmrfVh0OkGLYTfL0GJrSYpMRp5qwgQIDAQAB"

        mHelper = IabHelper(this, base64EncodedPublicKey)

        mHelper!!.startSetup { result ->
            when (result.isSuccess) {
                true -> debug { "In-app Billing setup is OK" }
                false -> debug { "In-app Billing setup failed: $result" }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_SESSION_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val sessionsFragment = supportFragmentManager.findFragmentByTag(SESSIONS_FRAG_TAG) as SessionsFragment
                sessionsFragment.update()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment, tag)
        transaction.commit()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickFabAddSession(view: View) {
        if (!isPremiumUser) {
            doAsync {
                val numberOfSessions = sessionRepository.numberOfSessions()

                onComplete {
                    if (numberOfSessions > 4) {
                        this.activityUiThreadWithContext {
                            Toast.makeText(this, "Buy premium version for unlimited session entries",
                                    Toast.LENGTH_LONG).show()
                        }
                        return@onComplete
                    } else {
                        this.activityUiThreadWithContext {
                            val intent = Intent(this, AddSessionActivity::class.java)
                            startActivityForResult(intent, ADD_SESSION_REQUEST)
                        }

                    }
                }
            }
        }
    }
}
