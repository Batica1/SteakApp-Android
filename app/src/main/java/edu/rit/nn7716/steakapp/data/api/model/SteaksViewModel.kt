package edu.rit.nn7716.steakapp.data.api.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.rit.nn7716.steakapp.data.api.APIService
import kotlinx.coroutines.launch

class SteaksViewModel(private val dataStoreHelper: DataStoreHelper) : ViewModel() {

    private val _steakList = mutableStateListOf<Steak>()
    var errorMessage: String by mutableStateOf("")
    var loading by mutableStateOf(false)

    val steakList: List<Steak>
        get() = _steakList

    private val _favoriteSteaks = mutableStateListOf<Steak>()
    val favoriteSteaks: List<Steak>
        get() = _favoriteSteaks

    fun toggleFavorite(steak: Steak) {
        viewModelScope.launch {
            steak.isFavourite = !steak.isFavourite
            dataStoreHelper.saveFavorite(steak.id, steak.isFavourite)
            if (steak.isFavourite) {
                _favoriteSteaks.add(steak)
            } else {
                _favoriteSteaks.remove(steak)
            }
        }
    }


    fun loadFavorites() {
        viewModelScope.launch {
            val favorites = dataStoreHelper.getFavorites()
            _favoriteSteaks.clear()
            _favoriteSteaks.addAll(steakList.filter { favorites[it.id] == true })
        }
    }

    fun getSteaks() {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                loading = true
                _steakList.clear()
                _steakList.addAll(apiService.getSteaks().steaks)
                val favorites = dataStoreHelper.getFavorites()
                _steakList.forEach { steak ->
                    steak.isFavourite = favorites[steak.id] ?: false
                    if (steak.isFavourite) {
                        _favoriteSteaks.add(steak)
                    }
                }
                loadFavorites()
                loading = false
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                loading = false
            }
        }
    }


}
