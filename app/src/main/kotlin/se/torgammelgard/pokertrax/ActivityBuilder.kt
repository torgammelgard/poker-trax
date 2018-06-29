package se.torgammelgard.pokertrax

import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.torgammelgard.pokertrax.activities.AddSessionActivity
import se.torgammelgard.pokertrax.activities.MainActivity
import se.torgammelgard.pokertrax.activities.TabMainFragmentActivity
import se.torgammelgard.pokertrax.fragments.GameStructureDialogFragment
import se.torgammelgard.pokertrax.fragments.GraphFragment
import se.torgammelgard.pokertrax.fragments.SessionsFragment
import se.torgammelgard.pokertrax.fragments.SummaryFragment

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(AddSessionActivityModule::class))
    abstract fun bindAddSessionActivity(): AddSessionActivity

    @ContributesAndroidInjector(modules = arrayOf(TabMainFragmentActivityModule::class))
    abstract fun bindTabMainFragmentActivity(): TabMainFragmentActivity

    @ContributesAndroidInjector(modules = arrayOf(GameStructureDialogFragmentModule::class))
    abstract fun bindGameStructureDialogFragment(): GameStructureDialogFragment

    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = arrayOf(SessionsFragmentModule::class))
    abstract fun bindSessionsFragment(): SessionsFragment

    @ContributesAndroidInjector
    abstract fun bindGraphFragment(): GraphFragment

    @ContributesAndroidInjector
    abstract fun bindSummaryFragment(): SummaryFragment
}