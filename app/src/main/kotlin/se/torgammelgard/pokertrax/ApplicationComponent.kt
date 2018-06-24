package se.torgammelgard.pokertrax

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import se.torgammelgard.pokertrax.fragments.GameStructureDialogFragment
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBuilder::class
        ))
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun mainApp(mainApp: MainApp): Builder

        fun build(): ApplicationComponent
    }

    fun inject(mainApp: MainApp)
    fun inject(gameStructureDialogFragment: GameStructureDialogFragment)
}