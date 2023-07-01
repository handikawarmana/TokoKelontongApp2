package com.example.tokokelontongapp.application

import android.app.Application
import com.example.tokokelontongapp.repository.GroceriesRepository

class GroceriesApp: Application() {
    val database by lazy { GroceriesDatabase.getDatabase(this) }
    val repository by lazy { GroceriesRepository(database.groceriesDao()) }
}