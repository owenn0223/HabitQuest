package com.example.habitquest.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habitquest.model.Habit
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para la entidad Habit
 */
@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Query("SELECT * FROM habit ORDER BY fechaCreacion DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habit ORDER BY fechaCreacion DESC")
    suspend fun getAllHabitsOnce(): List<Habit>

    @Query("SELECT * FROM habit WHERE frecuencia = :frecuencia ORDER BY fechaCreacion DESC")
    fun getHabitsByFrecuencia(frecuencia: String): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE id = :habitId")
    suspend fun getHabitById(habitId: Int): Habit?

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("DELETE FROM habit WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: Int)

    @Query("DELETE FROM habit")
    suspend fun deleteAllHabits()

    @Query("SELECT COUNT(*) FROM habit")
    suspend fun getHabitsCount(): Int

    @Query("SELECT COUNT(*) FROM habit WHERE completado = 1")
    suspend fun getCompletedHabitsCount(): Int

    @Query("UPDATE habit SET completado = 0")
    suspend fun resetAllHabitsCompletion()

    @Query("SELECT * FROM habit WHERE completado = 0 ORDER BY fechaCreacion DESC")
    fun getIncompleteHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE nombre LIKE '%' || :query || '%' ORDER BY fechaCreacion DESC")
    fun searchHabits(query: String): Flow<List<Habit>>
}
