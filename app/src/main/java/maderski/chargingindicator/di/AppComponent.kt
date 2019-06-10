package maderski.chargingindicator.di

import android.app.Application
import dagger.Component
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.services.BatteryService
import maderski.chargingindicator.ui.activities.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: Application)
    fun inject(mainActivity: MainActivity)
    fun inject(batteryReceiver: BatteryReceiver)
    fun inject(batteryService: BatteryService)
}