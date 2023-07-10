package com.example.tokokelontongapp.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tokokelontongapp.dao.GroceriesDao
import com.example.tokokelontongapp.model.Groceries

@Database(entities = [Groceries::class], version = 2, exportSchema = false)
abstract class GroceriesDatabase: RoomDatabase() {
    abstract fun groceriesDao(): GroceriesDao

    companion object {
        private var INSTANCE: GroceriesDatabase? = null

        // migrasi database versi 1 ke 2, karena ada perubahan tabel
        private val migration1To2: Migration = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE groceries_table ADD COLUMN latitude Double DEFAULT 0.0")
                database.execSQL("ALTER TABLE groceries_table ADD COLUMN longitude Double DEFAULT 0.0")
            }

        }

        fun getDatabase(context: Context): GroceriesDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroceriesDatabase::class.java,
                    "groceries_database"
                )
                    .addMigrations(migration1To2)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}