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
 *
 * Este DAO define todas las operaciones CRUD que podemos hacer
 * con los hábitos en la base de datos Room.
 *
 * OPERACIONES CRUD REQUERIDAS:
 * - C: Create (Insert) - insertar nuevo hábito
 * - R: Read (Query) - obtener hábitos
 * - U: Update - actualizar hábito existente
 * - D: Delete - eliminar hábito
 *
 * USAMOS Flow PARA OBSERVABILIDAD:
 * - Flow permite que la UI se actualice automáticamente
 * - Cuando cambian los datos, la UI se redibuja sola
 * - Es parte del patrón MVVM
 */

@Dao
interface HabitDao {

    /**
     * INSERTAR HÁBITO (CREATE)
     *
     * Inserta un nuevo hábito en la base de datos
     * Si hay conflicto (mismo ID), reemplaza
     *
     * @param habit El hábito a insertar
     * @return Long El ID generado (útil para saber el ID asignado)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    /**
     * OBTENER TODOS LOS HÁBITOS (READ)
     *
     * Obtiene todos los hábitos ordenados por fecha de creación (más recientes primero)
     * Usa Flow para que sea observable (se actualiza automáticamente)
     *
     * @return Flow<List<Habit>> Lista observable de hábitos
     */
    @Query("SELECT * FROM habit ORDER BY fechaCreacion DESC")
    fun getAllHabits(): Flow<List<Habit>>

    /**
     * OBTENER HÁBITOS POR FRECUENCIA (READ CON FILTRO)
     *
     * Obtiene hábitos filtrados por frecuencia
     * Útil para los filtros: All, Daily, Weekly, Monthly
     *
     * @param frecuencia La frecuencia a filtrar ("DAILY", "WEEKLY", "MONTHLY")
     * @return Flow<List<Habit>> Lista observable filtrada
     */
    @Query("SELECT * FROM habit WHERE frecuencia = :frecuencia ORDER BY fechaCreacion DESC")
    fun getHabitsByFrecuencia(frecuencia: String): Flow<List<Habit>>

    /**
     * OBTENER HÁBITO POR ID (READ ESPECÍFICO)
     *
     * Obtiene un hábito específico por su ID
     * Útil para editar un hábito individual
     *
     * @param habitId El ID del hábito
     * @return Habit? El hábito encontrado o null
     */
    @Query("SELECT * FROM habit WHERE id = :habitId")
    suspend fun getHabitById(habitId: Int): Habit?

    /**
     * ACTUALIZAR HÁBITO (UPDATE)
     *
     * Actualiza un hábito existente
     * Útil para marcar como completado o cambiar propiedades
     *
     * @param habit El hábito con los datos actualizados
     */
    @Update
    suspend fun updateHabit(habit: Habit)

    /**
     * ELIMINAR HÁBITO (DELETE)
     *
     * Elimina un hábito de la base de datos
     *
     * @param habit El hábito a eliminar
     */
    @Delete
    suspend fun deleteHabit(habit: Habit)

    /**
     * ELIMINAR HÁBITO POR ID (DELETE ALTERNATIVO)
     *
     * Elimina un hábito específico por su ID
     * Útil cuando no tienes el objeto completo
     *
     * @param habitId El ID del hábito a eliminar
     */
    @Query("DELETE FROM habit WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: Int)

    /**
     * CONTAR HÁBITOS TOTALES
     *
     * Cuenta cuántos hábitos hay en total
     * Útil para estadísticas
     *
     * @return Int Número total de hábitos
     */
    @Query("SELECT COUNT(*) FROM habit")
    suspend fun getHabitsCount(): Int

    /**
     * CONTAR HÁBITOS COMPLETADOS HOY
     *
     * Cuenta cuántos hábitos están marcados como completados
     * Útil para mostrar estadísticas
     *
     * @return Int Número de hábitos completados
     */
    @Query("SELECT COUNT(*) FROM habit WHERE completado = 1")
    suspend fun getCompletedHabitsCount(): Int

    /**
     * RESET COMPLETADO DIARIO
     *
     * Resetea el estado 'completado' de todos los hábitos a false
     * Útil para resetear al inicio de un nuevo día
     */
    @Query("UPDATE habit SET completado = 0")
    suspend fun resetAllHabitsCompletion()

    /**
     * OBTENER HÁBITOS NO COMPLETADOS
     *
     * Obtiene solo los hábitos que NO están completados
     * Útil para mostrar qué falta por hacer
     *
     * @return Flow<List<Habit>> Lista de hábitos pendientes
     */
    @Query("SELECT * FROM habit WHERE completado = 0 ORDER BY fechaCreacion DESC")
    fun getIncompleteHabits(): Flow<List<Habit>>

    /**
     * BUSCAR HÁBITOS POR NOMBRE
     *
     * Busca hábitos que contengan el texto en el nombre
     * Útil para funcionalidad de búsqueda
     *
     * @param query El texto a buscar
     * @return Flow<List<Habit>> Lista de hábitos que coinciden
     */
    @Query("SELECT * FROM habit WHERE nombre LIKE '%' || :query || '%' ORDER BY fechaCreacion DESC")
    fun searchHabits(query: String): Flow<List<Habit>>
}

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * ¿POR QUÉ ESTAS OPERACIONES?
 *
 * 1. CRUD BÁSICO:
 *    - insertHabit: Crear nuevo hábito
 *    - getAllHabits: Leer todos los hábitos
 *    - updateHabit: Actualizar hábito (ej: marcar completado)
 *    - deleteHabit: Eliminar hábito
 *
 * 2. OBSERVABILIDAD CON FLOW:
 *    - Todas las queries de lectura usan Flow
 *    - Permite que la UI se actualice automáticamente
 *    - Es reactivo: cuando BD cambia, UI cambia
 *
 * 3. QUERIES ESPECÍFICAS:
 *    - getHabitsByFrecuencia: Para filtros (Daily, Weekly, Monthly)
 *    - getHabitById: Para editar un hábito específico
 *    - getCompletedHabitsCount: Para estadísticas
 *    - resetAllHabitsCompletion: Para reset diario
 *
 * 4. SUSPEND FUNCTIONS:
 *    - Todas las operaciones de escritura son suspend
 *    - Se ejecutan en corrutinas (no bloquean UI)
 *    - Room maneja el threading automáticamente
 *
 * ---
 *
 * EJEMPLOS DE USO:
 *
 * // Insertar nuevo hábito
 * val newHabit = Habit(nombre = "Morning Run", frecuencia = "DAILY", ...)
 * val id = dao.insertHabit(newHabit)
 *
 * // Obtener todos los hábitos (observable)
 * val habitsFlow = dao.getAllHabits()
 * habitsFlow.collect { habits -> /* actualizar UI */ }
 *
 * // Marcar como completado
 * val habit = dao.getHabitById(1)
 * val updatedHabit = habit?.copy(completado = true)
 * if (updatedHabit != null) dao.updateHabit(updatedHabit)
 *
 * // Eliminar hábito
 * dao.deleteHabit(habit)
 *
 * ---
 *
 * CONEXIÓN CON LA UI:
 *
 * En HabitsListScreen.kt:
 * - getAllHabits() → LazyColumn con todos los hábitos
 * - getHabitsByFrecuencia("DAILY") → Filtro Daily
 * - updateHabit() → Al presionar botón completar
 * - deleteHabit() → Al hacer swipe o presionar eliminar
 */
