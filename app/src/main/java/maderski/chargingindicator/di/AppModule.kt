package maderski.chargingindicator.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import maderski.chargingindicator.actions.CIPerformActions
import maderski.chargingindicator.actions.interfaces.PerformActions
import maderski.chargingindicator.helpers.battery.BatteryHelper
import maderski.chargingindicator.helpers.battery.CIBatteryHelper
import maderski.chargingindicator.helpers.bubbles.BubblesHelper
import maderski.chargingindicator.helpers.bubbles.CIBubblesHelper
import maderski.chargingindicator.helpers.sound.CISoundHelper
import maderski.chargingindicator.helpers.sound.SoundHelper
import maderski.chargingindicator.helpers.vibration.CIVibrationHelper
import maderski.chargingindicator.helpers.vibration.VibrationHelper
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Singleton
    @Provides
    fun provideApplicationContext(): Context = application

    @Singleton
    @Provides
    fun provideVibrationHelper(context: Context): VibrationHelper = CIVibrationHelper(context)

    @Singleton
    @Provides
    fun provideSoundHelper(context: Context): SoundHelper = CISoundHelper(context)

    @Singleton
    @Provides
    fun provideBubblesHelper(context: Context): BubblesHelper = CIBubblesHelper(context)

    @Singleton
    @Provides
    fun provideBatteryHelper(): BatteryHelper = CIBatteryHelper()

    @Singleton
    @Provides
    fun providePerformActions(context: Context,
                              vibrationHelper: VibrationHelper,
                              playSoundHelper: SoundHelper,
                              bubblesHelper: BubblesHelper,
                              batteryHelper: BatteryHelper
    ): PerformActions = CIPerformActions(
            context,
            vibrationHelper,
            playSoundHelper,
            bubblesHelper,
            batteryHelper
    )
}