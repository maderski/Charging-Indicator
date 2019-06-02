package maderski.chargingindicator

import android.app.Application
import maderski.chargingindicator.delegatedproperties.setOnceOf
import maderski.chargingindicator.di.AppComponent
import maderski.chargingindicator.di.AppModule
import maderski.chargingindicator.di.DaggerAppComponent

class CIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
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
        var appComponent by setOnceOf<AppComponent>()
            private set
    }
}