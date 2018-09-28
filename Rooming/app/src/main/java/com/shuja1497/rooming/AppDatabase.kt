package com.shuja1497.rooming

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE : AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2){

            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE User ADD COLUMN location TEXT not null DEFAULT '' ")
            }
        }

        fun getInstance(context: Context): AppDatabase?{

            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "user.db")
                    .allowMainThreadQueries() // for allowing usage in main thread
                    .addMigrations(MIGRATION_1_2)
                    .build()

            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
    abstract fun userDao(): UserDao
}