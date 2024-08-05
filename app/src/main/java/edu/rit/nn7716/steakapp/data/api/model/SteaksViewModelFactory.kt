package edu.rit.nn7716.steakapp.data.api.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SteaksViewModelFactory(private val dataStoreHelper: DataStoreHelper) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SteaksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SteaksViewModel(dataStoreHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
