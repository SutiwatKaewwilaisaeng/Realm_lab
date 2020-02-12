package com.example.mainapplication.ui

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .name("android.realm")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }
}