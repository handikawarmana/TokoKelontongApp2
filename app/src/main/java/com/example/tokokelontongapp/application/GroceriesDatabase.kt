package com.example.tokokelontongapp.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tokokelontongapp.dao.GroceriesDao
import com.example.tokokelontongapp.model.Groceries

@Database(entities = [Groceries::class], version = 1, exportSchema = false)
abstract class GroceriesDatabase: RoomDatabase() {
    abstract fun groceriesDao(): GroceriesDao

    companion object {
        private var INSTANCE: GroceriesDatabase? = null

        fun getDatabase(context: Context): GroceriesDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroceriesDatabase::class.java,
                    "groceries_database"
                )
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}