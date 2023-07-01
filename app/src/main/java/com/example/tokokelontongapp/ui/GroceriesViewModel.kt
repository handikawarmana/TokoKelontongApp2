package com.example.tokokelontongapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tokokelontongapp.model.Groceries
import com.example.tokokelontongapp.repository.GroceriesRepository
import kotlinx.coroutines.launch

class GroceriesViewModel(private val repository: GroceriesRepository): ViewModel() {
    val allGrocerieses: LiveData<List<Groceries>> = repository.allGrocerieses.asLiveData()

    fun insert(groceries: Groceries) = viewModelScope.launch {
        repository.insertGroceries(groceries)
    }

    fun delete(groceries: Groceries) = viewModelScope.launch {
        repository.deleteGroceries(groceries)
    }

    fun update(groceries: Groceries) = viewModelScope.launch {
        repository.updateGroceries(groceries)
    }
}

class GroceriesViewModelFactory(private val repository: GroceriesRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((GroceriesViewModel::class.java)))  {
            return GroceriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}