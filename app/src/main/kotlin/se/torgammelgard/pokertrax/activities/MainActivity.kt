package se.torgammelgard.pokertrax.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import org.jetbrains.anko.AnkoLogger
import se.torgammelgard.pokertrax.R
import se.torgammelgard.pokertrax.fragments.GraphFragment
import se.torgammelgard.pokertrax.fragments.SessionsFragment
import se.torgammelgard.pokertrax.fragments.SummaryFragment
import javax.inject.Inject

/**
 * Main activity, responsible to be informative and simple to use
 */
class MainActivity : FragmentActivity(), HasSupportFragmentInjector, AnkoLogger {

    companion object {
        const val SESSIONS_FRAG_TAG = "sessions_fragment"
        const val GRAPH_FRAG_TAG = "graph_fragment"
        const val SUMMARY_FRAG_TAG = "summary_fragment"
    }

    private lateinit var addSessionFAB: FloatingActionButton

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        addSessionFAB = findViewById(R.id.floatingActionButton)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val sessionsFragment = supportFragmentManager.findFragmentByTag(SESSIONS_FRAG_TAG) as SessionsFragment
            sessionsFragment.update()
        }
    }
    private fun replaceFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment, tag)
        transaction.commit()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickFabAddSession(view: View) {
        val intent = Intent(this, AddSessionActivity::class.java)
        startActivityForResult(intent, 1)
    }
}
