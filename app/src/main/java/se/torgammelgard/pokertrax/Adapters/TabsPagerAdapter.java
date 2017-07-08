package se.torgammelgard.pokertrax.Adapters;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import se.torgammelgard.pokertrax.GraphFragment;
import se.torgammelgard.pokertrax.MainFragment;
import se.torgammelgard.pokertrax.ResultsFragment;
import se.torgammelgard.pokertrax.SummaryFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener,
    ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ActionBar mActionBar;

    public TabsPagerAdapter(FragmentActivity activity, ViewPager viewPager) {
        super(activity.getSupportFragmentManager());
        mViewPager = viewPager;
        mActionBar = activity.getActionBar();
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    public void addTab(ActionBar.Tab tab) {
        tab.setTabListener(this);
        mActionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public android.support.v4.app.Fragment getItem(int item) {
        switch (item) {
            case 0: return new MainFragment();

            case 1: return new ResultsFragment();

            case 2: return new GraphFragment();

            case 3: return new SummaryFragment();

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    /* --- impl TabListener */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    /* --- impl OnPageChangeListener */
    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int item) {
        mActionBar.setSelectedNavigationItem(item);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
