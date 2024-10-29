package ipp.estg.cmu_09_8220169_8220307_8220337

import android.app.Application
import android.content.Context
import ipp.estg.cmu_09_8220169_8220307_8220337.di.AppModule
import ipp.estg.cmu_09_8220169_8220307_8220337.di.AppModuleImpl

class Hard75Application: Application() {

    // allow us always to access this appModule
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}