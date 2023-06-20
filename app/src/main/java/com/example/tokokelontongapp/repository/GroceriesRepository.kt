package com.example.tokokelontongapp.repository

import com.example.tokokelontongapp.dao.GroceriesDao
import com.example.tokokelontongapp.model.Groceries
import kotlinx.coroutines.flow.Flow

class GroceriesRepository (private val groceriesDao: GroceriesDao){
    val allGrocerieses: Flow<List<Groceries>> = groceriesDao.getAllGroceries()
    suspend fun insertGroceries(groceries: Groceries){
        groceriesDao.insertGroceries(groceries)
    }

    suspend fun deleteGroceries(groceries: Groceries){
        groceriesDao.deleteGroceries(groceries)
    }

    suspend fun updateGroceries(groceries: Groceries){
        groceriesDao.updateGroceries(groceries)
    }
}