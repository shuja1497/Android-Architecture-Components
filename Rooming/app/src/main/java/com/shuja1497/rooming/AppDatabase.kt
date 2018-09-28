package com.shuja1497.rooming

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase?{

            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "user.db")
                    .allowMainThreadQueries() // for allowing usage in main thread
                    .build()

            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
    abstract fun userDao(): UserDao
}