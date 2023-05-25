package maderski.chargingindicator

import android.app.Application
import maderski.chargingindicator.di.AppComponent
import maderski.chargingindicator.di.AppModule
import maderski.chargingindicator.di.DaggerAppComponent


class CIApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = createAppComponent()
        appComponent.inject(this)
    }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    companion object {
        lateinit var instance: CIApplication
            private set
    }
}