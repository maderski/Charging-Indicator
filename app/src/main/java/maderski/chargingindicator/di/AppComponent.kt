package maderski.chargingindicator.di

import android.app.Application
import dagger.Component
import maderski.chargingindicator.actions.interfaces.PerformActions
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.services.BatteryService
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: Application)
    fun inject(batteryReceiver: BatteryReceiver)
    fun inject(batteryService: BatteryService)
}