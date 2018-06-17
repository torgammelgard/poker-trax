package se.torgammelgard.pokertrax

import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.torgammelgard.pokertrax.activities.AddSessionActivity
import se.torgammelgard.pokertrax.activities.TabMainFragmentActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(AddSessionActivityModule::class))
    abstract fun bindAddSessionActivity() : AddSessionActivity

    @ContributesAndroidInjector(modules = arrayOf(TabMainFragmentActivityModule::class))
    abstract fun bindTabMainFragmentActivity() : TabMainFragmentActivity
}