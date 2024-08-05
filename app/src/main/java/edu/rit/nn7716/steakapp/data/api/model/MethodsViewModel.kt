package edu.rit.nn7716.steakapp.data.api.model

import Method
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.rit.nn7716.steakapp.data.api.APIService
import kotlinx.coroutines.launch

class MethodsViewModel : ViewModel() {
    private val _methodsList = mutableStateListOf<Method>()
    var errorMessage: String by mutableStateOf("")
    var loading by mutableStateOf(false)

    val methodsList: List<Method>
        get() = _methodsList

    fun getMethods() {
        viewModelScope.launch {
            val apiService = APIService.getInstance()

            try {
                loading = true
                _methodsList.clear()
                _methodsList.addAll(apiService.getMethods().methods)
                loading = false
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                loading = false
            }
        }
    }
}
