package com.example.tokokelontongapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tokokelontongapp.model.Groceries
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceriesDao {
    @Query("SELECT * FROM `groceries_table` ORDER BY name ASC ")
    fun getAllGroceries(): Flow<List<Groceries>>

    @Insert
    suspend fun insertGroceries(groceries: Groceries)

    @Delete
    suspend fun deleteGroceries(groceries: Groceries)

    @Update fun updateGroceries(groceries: Groceries)
}