package com.example.habitquest.network

import com.google.gson.annotations.SerializedName

// ─── AUTH ─────────────────────────────────────────────────────────

// ─── AUTH ─────────────────────────────────────────────────────────

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val playerClass: String = "GUERRERO"
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: AuthData
)

data class AuthData(
    val user: ApiUser,
    val token: String
)

// ─── USUARIO ──────────────────────────────────────────────────────

data class ApiUser(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String?,
    val playerClass: String?,
    val level: Int,
    val xp: Int
)

// ─── HÁBITOS ──────────────────────────────────────────────────────

data class ApiHabit(
    val id: Int,
    val name: String,
    val description: String?,
    val type: String,
    val difficulty: String,
    val isActive: Boolean,
    val createdAt: String?
)

data class CreateHabitRequest(
    val name: String,
    val description: String = "",
    val type: String = "OTRO",
    val difficulty: String = "FACIL"
)

// ─── PROGRESO ─────────────────────────────────────────────────────

data class ApiProgress(
    val level: Int,
    val xp: Int,
    val habitsCompleted: Int
)

// ─── ESTADÍSTICAS ─────────────────────────────────────────────────

data class ApiStats(
    val totalHabits: Int,
    val completedHabits: Int,
    val xp: Int
)

data class ApiWeeklyStat(
    val date: String,
    val completed: Int
)

// ─── LOGROS ───────────────────────────────────────────────────────

data class ApiAchievement(
    val id: Int,
    val name: String,
    val description: String,
    val unlocked: Boolean,
    val unlockedAt: String?
)

// ─── RESPUESTA GENÉRICA ───────────────────────────────────────────

data class MessageResponse(
    val message: String
)
