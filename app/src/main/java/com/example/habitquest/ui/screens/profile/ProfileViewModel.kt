package com.example.habitquest.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitquest.database.HabitDatabase
import com.example.habitquest.manager.SesionManager
import com.example.habitquest.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel para la pantalla de Perfil del Usuario (ProfileScreen)
 *
 * Este ViewModel maneja:
 * - Obtener información del usuario actual desde BD y SesionManager
 * - Modo edición de perfil
 * - Actualización de datos del usuario
 * - Cálculos de estadísticas
 * - Cerrar sesión
 *
 * CARACTERÍSTICAS ACADÉMICAS:
 * - AndroidViewModel: Para acceso a Application context
 * - StateFlow: Para estado observable
 * - SesionManager: Para gestión de sesión
 * - Room Database: Para datos del usuario
 * - viewModelScope: Para corrutinas que sobreviven a cambios de configuración
 */

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia de la base de datos Room
    private val database = HabitDatabase.getDatabase(application)
    private val usuarioDao = database.usuarioDao()

    // SesionManager para obtener datos de sesión
    private val sesionManager = SesionManager(application)

    // Instancia del usuario actual
    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser

    // Estadísticas adicionales
    private val _totalXP = MutableStateFlow(0)
    val totalXP: StateFlow<Int> = _totalXP

    private val _currentLevel = MutableStateFlow(1)
    val currentLevel: StateFlow<Int> = _currentLevel

    private val _habitsCreated = MutableStateFlow(0)
    val habitsCreated: StateFlow<Int> = _habitsCreated

    private val _habitsCompleted = MutableStateFlow(0)
    val habitsCompleted: StateFlow<Int> = _habitsCompleted

    // Estados de la UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    // Campos editables
    private val _editName = MutableStateFlow("")
    val editName: StateFlow<String> = _editName

    private val _editEmail = MutableStateFlow("")
    val editEmail: StateFlow<String> = _editEmail

    private val _editClass = MutableStateFlow("")
    val editClass: StateFlow<String> = _editClass

    init {
        // Cargar datos del usuario al inicializar
        loadUserProfile()
    }

    /**
     * CARGAR PERFIL DEL USUARIO
     *
     * Obtiene la información del usuario actual desde SesionManager y BD
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = sesionManager.obtenerUsuarioId()
                if (userId != -1) {
                    val usuario = usuarioDao.getUsuarioById(userId)
                    _currentUser.value = usuario

                    // Inicializar campos de edición con datos actuales
                    if (usuario != null) {
                        _editName.value = usuario.nombre
                        _editEmail.value = usuario.correo
                        _editClass.value = usuario.clase

                        // Cargar estadísticas adicionales
                        _totalXP.value = usuario.xpTotal
                        _currentLevel.value = usuario.nivelActual
                        // TODO: Implementar conteo de hábitos cuando se agregue usuarioId a Habit
                        _habitsCreated.value = 0
                        _habitsCompleted.value = 0
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar perfil: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * ACTIVAR MODO EDICIÓN
     *
     * Cambia la UI a modo edición
     */
    fun enableEditMode() {
        _isEditMode.value = true
        _errorMessage.value = null
        _successMessage.value = null
    }

    /**
     * CANCELAR EDICIÓN
     *
     * Sale del modo edición sin guardar cambios
     */
    fun cancelEdit() {
        _isEditMode.value = false
        // Restaurar valores originales
        _currentUser.value?.let { user ->
            _editName.value = user.nombre
            _editEmail.value = user.correo
            _editClass.value = user.clase
        }
        _errorMessage.value = null
        _successMessage.value = null
    }

    /**
     * ACTUALIZAR NOMBRE EN EDICIÓN
     */
    fun updateEditName(name: String) {
        _editName.value = name
        _errorMessage.value = null
    }

    /**
     * ACTUALIZAR CORREO EN EDICIÓN
     */
    fun updateEditEmail(email: String) {
        _editEmail.value = email
        _errorMessage.value = null
    }

    /**
     * ACTUALIZAR CLASE EN EDICIÓN
     */
    fun updateEditClass(userClass: String) {
        _editClass.value = userClass
    }

    /**
     * GUARDAR CAMBIOS DEL PERFIL
     *
     * Valida y guarda los cambios realizados en el perfil
     */
    fun saveProfileChanges() {
        val currentUser = _currentUser.value ?: return

        // Validaciones
        if (_editName.value.isBlank()) {
            _errorMessage.value = "El nombre no puede estar vacío"
            return
        }

        if (_editName.value.length < 2) {
            _errorMessage.value = "El nombre debe tener al menos 2 caracteres"
            return
        }

        if (_editName.value.length > 30) {
            _errorMessage.value = "El nombre no puede superar 30 caracteres"
            return
        }

        if (_editEmail.value.isBlank()) {
            _errorMessage.value = "El correo no puede estar vacío"
            return
        }

        if (!isValidEmail(_editEmail.value)) {
            _errorMessage.value = "El correo electrónico no es válido"
            return
        }

        if (_editClass.value.isBlank()) {
            _errorMessage.value = "Debes seleccionar una clase"
            return
        }

        // Verificar si el correo cambió y si ya existe
        if (_editEmail.value != currentUser.correo) {
            viewModelScope.launch {
                val emailExists = usuarioDao.existeCorreo(_editEmail.value)
                if (emailExists) {
                    _errorMessage.value = "Este correo ya está registrado"
                    return@launch
                }
                saveChanges(currentUser)
            }
        } else {
            viewModelScope.launch {
                saveChanges(currentUser)
            }
        }
    }

    /**
     * GUARDAR CAMBIOS (FUNCIÓN PRIVADA)
     */
    private suspend fun saveChanges(currentUser: Usuario) {
        _isLoading.value = true
        try {
            // Crear usuario actualizado
            val updatedUser = currentUser.copy(
                nombre = _editName.value.trim(),
                correo = _editEmail.value.trim(),
                clase = _editClass.value
            )

            // Actualizar en BD
            usuarioDao.updateUsuario(updatedUser)

            // Actualizar en SesionManager si cambió nombre, correo o clase
            if (updatedUser.nombre != currentUser.nombre ||
                updatedUser.correo != currentUser.correo ||
                updatedUser.clase != currentUser.clase) {

                sesionManager.guardarSesion(
                    usuarioId = updatedUser.id,
                    nombre = updatedUser.nombre,
                    correo = updatedUser.correo,
                    clase = updatedUser.clase
                )
            }

            // Actualizar estado local
            _currentUser.value = updatedUser
            _isEditMode.value = false
            _successMessage.value = "Perfil actualizado exitosamente"

        } catch (e: Exception) {
            _errorMessage.value = "Error al guardar cambios: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    /**
     * CERRAR SESIÓN
     *
     * Limpia la sesión y retorna callback para navegación
     */
    fun logout(onLogout: () -> Unit) {
        sesionManager.cerrarSesion()
        onLogout()
    }

    /**
     * LIMPIAR MENSAJES
     */
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    /**
     * OBTENER CLASES DISPONIBLES
     */
    fun getAvailableClasses(): List<String> {
        return listOf("WARRIOR", "MAGE", "SAGE", "ADVENTURER")
    }

    /**
     * OBTENER ICONO DE CLASE
     */
    fun getClassIcon(userClass: String): String {
        return when (userClass.uppercase()) {
            "WARRIOR" -> "⚔️"
            "MAGE" -> "🔮"
            "SAGE" -> "📚"
            "ADVENTURER" -> "🗺️"
            else -> "🎯"
        }
    }

    /**
     * OBTENER NOMBRE LEGIBLE DE CLASE
     */
    fun getClassDisplayName(userClass: String): String {
        return when (userClass.uppercase()) {
            "WARRIOR" -> "Warrior"
            "MAGE" -> "Mage"
            "SAGE" -> "Sage"
            "ADVENTURER" -> "Adventurer"
            else -> userClass
        }
    }

    /**
     * CALCULAR PORCENTAJE DE PROGRESO EN NIVEL
     */
    fun getLevelProgressPercentage(): Float {
        val user = _currentUser.value ?: return 0f
        // Calcular nivel desde XP total
        val nivelInfo = calcularNivel(user.xpTotal)
        return nivelInfo.xpEnNivel.toFloat() / nivelInfo.xpParaSiguiente
    }

    /**
     * OBTENER NIVEL ACTUAL CALCULADO DESDE XP
     */
    fun getCurrentLevel(): Int {
        val user = _currentUser.value ?: return 1
        return calcularNivel(user.xpTotal).nivel
    }

    /**
     * OBTENER XP PARA SIGUIENTE NIVEL
     */
    fun getXpForNextLevel(): Int {
        val user = _currentUser.value ?: return 100
        val nivelInfo = calcularNivel(user.xpTotal)
        return nivelInfo.xpParaSiguiente
    }

    /**
     * OBTENER XP EN NIVEL ACTUAL
     */
    fun getXpInLevel(): Int {
        val user = _currentUser.value ?: return 0
        return calcularNivel(user.xpTotal).xpEnNivel
    }

    /**
     * OBTENER XP TOTAL FORMATEADO
     */
    fun getTotalXPFormatted(): String {
        val user = _currentUser.value ?: return "0"
        return "%,d".format(user.xpTotal)
    }

    /**
     * Calcula el nivel y progreso de XP
     * Fórmula: XP para nivel N = N * 100
     */
    private fun calcularNivel(xpTotal: Int): NivelInfo {
        var nivel = 1
        var xpAcumulado = 0
        while (xpTotal >= xpAcumulado + (nivel * 100)) {
            xpAcumulado += nivel * 100
            nivel++
        }
        val xpEnNivel = xpTotal - xpAcumulado
        val xpParaSiguiente = nivel * 100
        val porcentaje = if (xpParaSiguiente > 0)
            xpEnNivel.toFloat() / xpParaSiguiente else 0f
        return NivelInfo(nivel, xpEnNivel, xpParaSiguiente, porcentaje)
    }

    private data class NivelInfo(
        val nivel: Int,
        val xpEnNivel: Int,
        val xpParaSiguiente: Int,
        val porcentaje: Float
    )

    /**
     * VALIDAR FORMATO DE CORREO
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }

    /**
     * OBTENER FECHA ACTUAL
     */
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}

/**
 * EXPLICACIÓN ACADÉMICA:
 *
 * ¿POR QUÉ ESTE VIEWMODEL?
 *
 * 1. GESTIÓN DE SESIÓN:
 *    - SesionManager: Para obtener datos básicos del usuario logueado
 *    - UsuarioDao: Para obtener datos completos desde BD
 *    - Sincronización entre SharedPreferences y Room
 *
 * 2. MODO EDICIÓN:
 *    - _isEditMode: Controla si mostrar campos editables o solo lectura
 *    - Campos separados (_editName, _editEmail): Para no modificar datos originales hasta guardar
 *    - Validación antes de guardar cambios
 *
 * 3. ESTADO REACTIVO:
 *    - StateFlow para todos los campos editables
 *    - UI se actualiza automáticamente cuando cambian los valores
 *    - Mensajes de error/éxito se muestran y desaparecen
 *
 * 4. FUNCIONES DE UTILIDAD:
 *    - getClassIcon(): Para mostrar emojis según clase
 *    - getLevelProgressPercentage(): Para barras de progreso
 *    - isValidEmail(): Validación de formato de correo
 *
 * 5. OPERACIONES CRUD:
 *    - loadUserProfile(): Leer datos del usuario
 *    - saveProfileChanges(): Actualizar datos del usuario
 *    - logout(): Cerrar sesión
 *
 * ---
 *
 * CONEXIÓN CON LA UI:
 *
 * En ProfileScreen.kt:
 *
 * val viewModel: ProfileViewModel = viewModel()
 * val currentUser by viewModel.currentUser.collectAsState()
 * val isEditMode by viewModel.isEditMode.collectAsState()
 * val editName by viewModel.editName.collectAsState()
 * // ... otros campos
 *
 * // Mostrar datos
 * if (isEditMode) {
 *     OutlinedTextField(value = editName, onValueChange = { viewModel.updateEditName(it) })
 * } else {
 *     Text(currentUser?.nombre ?: "")
 * }
 *
 * // Botones de acción
 * Button(onClick = { viewModel.saveProfileChanges() }) { Text("Guardar") }
 * Button(onClick = { viewModel.cancelEdit() }) { Text("Cancelar") }
 * Button(onClick = { viewModel.logout { navController.navigate("login") } }) { Text("Cerrar Sesión") }
 *
 * ---
 *
 * CICLO DE VIDA:
 *
 * 1. Usuario abre ProfileScreen → init() llama loadUserProfile()
 * 2. ViewModel obtiene userId de SesionManager
 * 3. Consulta UsuarioDao.getUsuarioById(userId)
 * 4. _currentUser.value se actualiza → UI muestra datos
 * 5. Usuario presiona "Editar" → enableEditMode()
 * 6. Campos cambian a modo edición
 * 7. Usuario modifica datos → updateEdit*()
 * 8. Usuario presiona "Guardar" → saveProfileChanges()
 * 9. Validación → Actualización en BD → Actualización en SesionManager
 * 10. _currentUser.value se actualiza → UI muestra datos actualizados
 */
