package test.nicolasjafelle.kithub

import android.app.Application
import test.nicolasjafelle.kithub.module.module
import org.koin.android.ext.android.startKoin

class KithubApplication : Application() {

    companion object {
        lateinit var instance: KithubApplication

    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin(this, getModules())

    }

    private fun getModules() = listOf(module)
}