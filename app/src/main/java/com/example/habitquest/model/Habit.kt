package com.example.habitquest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String = "",
    val frecuencia: String,       // "DAILY", "WEEKLY", "MONTHLY"
    val dificultad: String,       // "EASY", "MED", "HARD"
    val tipo: String = "GENERAL", // "Strength", "Intelligence", "Agility", "Charisma"
    val emoji: String = "⚔️",
    val xp: Int,
    val completado: Boolean = false,
    val fechaCreacion: String,
    val ultimaVezCompletado: String = ""
) : Serializable