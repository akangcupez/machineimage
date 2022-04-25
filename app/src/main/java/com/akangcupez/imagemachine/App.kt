package com.akangcupez.imagemachine

import androidx.multidex.MultiDexApplication
import com.akangcupez.imagemachine.model.AppDatabase
import com.akangcupez.imagemachine.model.repository.MachineRepository

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/22/2022 01:58
 */
class App: MultiDexApplication() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { MachineRepository(database.machineDao()) }

    companion object {
        @get:Synchronized
        lateinit var context: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}