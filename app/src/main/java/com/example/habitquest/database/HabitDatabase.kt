package com.example.habitquest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habitquest.model.Habit
import com.example.habitquest.model.Usuario

/**
 * Base de datos Room para la aplicación HabitQuest
 *
 * Esta clase define la configuración completa de la base de datos:
 * - Entidades incluidas
 * - Versión de la base de datos
 * - DAOs disponibles
 * - Singleton pattern para acceso único
 *
 * CARACTERÍSTICAS ACADÉMICAS:
 * - Singleton: Solo una instancia de BD en toda la app
 * - Migration: Manejo de cambios en esquema (versión 1 por ahora)
 * - Export schema: Para debugging y migraciones futuras
 */

@Database(
    entities = [Usuario::class, Habit::class], // Entidades incluidas en esta BD
    version = 1, // Versión actual de la BD (incrementar en migraciones)
    exportSchema = true // Exporta esquema para debugging
)
abstract class HabitDatabase : RoomDatabase() {

    /**
     * DAO para operaciones con usuarios
     * Room genera automáticamente la implementación
     */
    abstract fun usuarioDao(): UsuarioDao

    /**
     * DAO para operaciones con hábitos
     * Room genera automáticamente la implementación
     */
    abstract fun habitDao(): HabitDao

    companion object {
        /**
         * NOMBRE DE LA BASE DE DATOS
         * Se guarda en: /data/data/com.example.habitquest/databases/
         */
        private const val DATABASE_NAME = "habitquest.db"

        /**
         * INSTANCIA ÚNICA (Singleton Pattern)
         * Volatile asegura que sea visible en todos los threads
         */
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        /**
         * OBTENER INSTANCIA DE LA BASE DE DATOS
         *
         * Si no existe, la crea. Si existe, la retorna.
         * Thread-safe gracias a synchronized.
         *
         * @param context Contexto de la aplicación
         * @return HabitDatabase Instancia única de la BD
         */
        fun getDatabase(context: Context): HabitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration(true) // Para desarrollo: borra y recrea si hay cambios
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * DESTRUIR INSTANCIA (para testing)
         * Solo usar en tests unitarios
         */
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * ¿POR QUÉ ESTA CONFIGURACIÓN?
 *
 * 1. @Database ANNOTATION:
 *    - entities: Lista de tablas (solo Habit por ahora)
 *    - version: Número de versión (1 = primera versión)
 *    - exportSchema: Genera archivo JSON con esquema (útil para migraciones)
 *
 * 2. SINGLETON PATTERN:
 *    - Solo una instancia de BD en toda la app
 *    - Evita problemas de concurrencia
 *    - Ahorra memoria y recursos
 *    - Thread-safe con @Volatile y synchronized
 *
 * 3. ROOM.DATABASEBUILDER:
 *    - context: Necesario para acceder a archivos
 *    - HabitDatabase::class.java: Clase de la BD
 *    - DATABASE_NAME: Nombre del archivo .db
 *
 * 4. FALLBACK TO DESTRUCTIVE MIGRATION:
 *    - Para desarrollo: si cambias esquema, borra y recrea
 *    - En producción: usarías migrations proper
 *
 * ---
 *
 * CICLO DE VIDA:
 *
 * 1. Primera vez que se llama getDatabase():
 *    - Room crea el archivo habitquest.db
 *    - Crea las tablas (habit)
 *    - Retorna la instancia
 *
 * 2. Llamadas posteriores:
 *    - Retorna la misma instancia
 *    - No crea nueva BD
 *
 * 3. App se cierra:
 *    - BD se cierra automáticamente
 *    - Pero instancia permanece en memoria
 *
 * ---
 *
 * ESTRUCTURA DE ARCHIVOS CREADOS:
 *
 * /data/data/com.example.habitquest/databases/
 * ├── habitquest.db          (Base de datos principal)
 * ├── habitquest.db-shm      (Shared memory - optimización)
 * └── habitquest.db-wal      (Write-Ahead Logging - optimización)
 *
 * TABLA CREADA:
 * habit (
 *     id INTEGER PRIMARY KEY AUTOINCREMENT,
 *     nombre TEXT NOT NULL,
 *     frecuencia TEXT NOT NULL,
 *     dificultad TEXT NOT NULL,
 *     xp INTEGER NOT NULL,
 *     completado INTEGER NOT NULL,  -- 0=false, 1=true
 *     fechaCreacion TEXT NOT NULL,
 *     ultimaVezCompletado TEXT NOT NULL
 * )
 *
 * ---
 *
 * CONEXIÓN CON LA APP:
 *
 * En MainActivity o Application class:
 * val db = HabitDatabase.getDatabase(context)
 * val dao = db.habitDao()
 *
 * Luego usar dao para operaciones CRUD.
 *
 * ---
 *
 * MIGRACIONES FUTURAS:
 *
 * Cuando agregues campos o tablas:
 * 1. Incrementa version = 2
 * 2. Agrega .addMigrations(Migration(1, 2) { ... })
 * 3. Quita fallbackToDestructiveMigration()
 *
 * Por ahora, para desarrollo, está bien el fallback.
 */
