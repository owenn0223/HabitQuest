package com.example.habitquest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contraseña: String,
    val clase: String, // "WARRIOR", "MAGE", "SAGE", "ADVENTURER"
    val nivelActual: Int = 1,
    val xpActual: Int = 0, // XP dentro del nivel actual (0-100)
    val xpTotal: Int = 0, // Total acumulado
    val rachaActual: Int = 0, // Días consecutivos
    val ultimaFecha: String = "", // Última fecha que completó un hábito (para racha)

    // Estadísticas RPG
    val disciplina: Int = 10,
    val fuerza: Int = 10,
    val inteligencia: Int = 10,
    val consistencia: Int = 10
) : Serializable

