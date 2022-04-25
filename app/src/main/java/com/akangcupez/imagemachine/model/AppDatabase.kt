package com.akangcupez.imagemachine.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akangcupez.imagemachine.model.dao.MachineDao
import com.akangcupez.imagemachine.model.entity.Image
import com.akangcupez.imagemachine.model.entity.Machine

/**
 * @author Aji Subastian (akangcupez@gmail.com) at 4/24/2022 17:08
 */

@Database(entities = [Machine::class, Image::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun machineDao(): MachineDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db_machine"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }

    }

}