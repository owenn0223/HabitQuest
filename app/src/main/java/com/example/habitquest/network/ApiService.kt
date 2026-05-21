package com.example.habitquest.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // ─── AUTH ─────────────────────────────────────────────────────

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    // ─── HÁBITOS ──────────────────────────────────────────────────

    @GET("api/habits")
    suspend fun getHabits(
        @Header("Authorization") token: String
    ): Response<List<ApiHabit>>

    @POST("api/habits")
    suspend fun createHabit(
        @Header("Authorization") token: String,
        @Body request: CreateHabitRequest
    ): Response<ApiHabit>

    @PUT("api/habits/{id}")
    suspend fun updateHabit(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: CreateHabitRequest
    ): Response<ApiHabit>

    @DELETE("api/habits/{id}")
    suspend fun deleteHabit(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponse>

    // ─── PROGRESO ─────────────────────────────────────────────────

    @POST("api/progress/habits/{habitId}/complete")
    suspend fun completeHabit(
        @Header("Authorization") token: String,
        @Path("habitId") habitId: Int
    ): Response<MessageResponse>

    @GET("api/progress")
    suspend fun getProgress(
        @Header("Authorization") token: String
    ): Response<ApiProgress>

    // ─── ESTADÍSTICAS ─────────────────────────────────────────────

    @GET("api/stats")
    suspend fun getStats(
        @Header("Authorization") token: String
    ): Response<ApiStats>

    @GET("api/stats/weekly")
    suspend fun getWeeklyStats(
        @Header("Authorization") token: String
    ): Response<List<ApiWeeklyStat>>

    // ─── LOGROS ───────────────────────────────────────────────────

    @GET("api/achievements")
    suspend fun getAchievements(
        @Header("Authorization") token: String
    ): Response<List<ApiAchievement>>

    // ─── PERFIL ───────────────────────────────────────────────────

    @GET("api/users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ApiUser>
}
