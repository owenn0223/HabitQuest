package com.example.habitquest.model

// Dificultad
const val DIFICULTAD_EASY = "EASY"
const val DIFICULTAD_MEDIUM = "MEDIUM"
const val DIFICULTAD_HARD = "HARD"

// Frecuencia
const val FRECUENCIA_DAILY = "DAILY"
const val FRECUENCIA_WEEKLY = "WEEKLY"

// Tipo de hábito
const val TIPO_STUDY = "STUDY"
const val TIPO_EXERCISE = "EXERCISE"
const val TIPO_HEALTH = "HEALTH"
const val TIPO_GENERAL = "GENERAL"

// Estado
const val ESTADO_ACTIVE = "ACTIVE"
const val ESTADO_ARCHIVED = "ARCHIVED"

// Condiciones de logros
const val CONDICION_STREAK = "STREAK"
const val CONDICION_LEVEL = "LEVEL"
const val CONDICION_TOTAL_XP = "TOTAL_XP"
const val CONDICION_HABITS_COMPLETED = "HABITS_COMPLETED"

fun getXPPorDificultad(dificultad: String): Int = when (dificultad) {
    DIFICULTAD_EASY -> 10
    DIFICULTAD_MEDIUM -> 20
    DIFICULTAD_HARD -> 40
    else -> 0
}