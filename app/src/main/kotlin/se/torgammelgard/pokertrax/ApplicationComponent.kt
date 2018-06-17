package se.torgammelgard.pokertrax

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
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
}