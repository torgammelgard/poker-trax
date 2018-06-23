package se.torgammelgard.pokertrax.adapters

import android.app.ActionBar
import android.app.FragmentTransaction
import androidx.fragment.app.Fragment
import se.torgammelgard.pokertrax.fragments.GraphFragment
import se.torgammelgard.pokertrax.fragments.SessionsFragment
import se.torgammelgard.pokertrax.fragments.ResultsFragment
import se.torgammelgard.pokertrax.fragments.SummaryFragment

class TabsPagerAdapter(activity: androidx.fragment.app.FragmentActivity, private val mViewPager: androidx.viewpager.widget.ViewPager) : androidx.fragment.app.FragmentPagerAdapter(activity.supportFragmentManager), ActionBar.TabListener, androidx.viewpager.widget.ViewPager.OnPageChangeListener {
    private val mActionBar: ActionBar = activity.actionBar

    init {
        mViewPager.adapter = this
        mViewPager.setOnPageChangeListener(this)
    }

    fun addTab(tab: ActionBar.Tab) {
        tab.setTabListener(this)
        mActionBar.addTab(tab)
        notifyDataSetChanged()
    }

    override fun getItem(item: Int): Fragment? {
        when (item) {
            0 -> return SessionsFragment()

            1 -> return ResultsFragment()

            2 -> return GraphFragment()

            3 -> return SummaryFragment()

            else -> return null
        }
    }

    override fun getCount(): Int {
        return 4
    }

    /* --- impl TabListener */
    override fun onTabSelected(tab: ActionBar.Tab, ft: FragmentTransaction) {
        mViewPager.currentItem = tab.position
    }

    override fun onTabUnselected(tab: ActionBar.Tab, ft: FragmentTransaction) {

    }

    override fun onTabReselected(tab: ActionBar.Tab, ft: FragmentTransaction) {

    }

    /* --- impl OnPageChangeListener */
    override fun onPageScrolled(i: Int, v: Float, i2: Int) {

    }

    override fun onPageSelected(item: Int) {
        mActionBar.setSelectedNavigationItem(item)
    }

    override fun onPageScrollStateChanged(i: Int) {

    }
}
