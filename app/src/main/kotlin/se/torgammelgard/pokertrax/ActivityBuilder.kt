package se.torgammelgard.pokertrax

import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.torgammelgard.pokertrax.activities.AddSessionActivity

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(AddSessionActivityModule::class))
    abstract fun bindAddSessionActivity() : AddSessionActivity

}