package az.mb.gold

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class GoldApplication : Application() {


    companion object {
        lateinit var context: GoldApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        plant(Timber.DebugTree())

    }
}