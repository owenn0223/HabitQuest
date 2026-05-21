package com.example.habitquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.network.ApiWeeklyStat
import com.example.habitquest.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val sesionManager: SesionManager
) : ViewModel() {

    private val _weeklyStats = MutableStateFlow<List<ApiWeeklyStat>>(emptyList())
    val weeklyStats: StateFlow<List<ApiWeeklyStat>> = _weeklyStats

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchWeeklyStats()
    }

    fun fetchWeeklyStats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = sesionManager.obtenerToken() ?: return@launch
                val response = RetrofitClient.instance.getWeeklyStats("Bearer $token")
                if (response.isSuccessful) {
                    _weeklyStats.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.value = false
            }
        }
    }
}