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

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val database = HabitDatabase.getDatabase(application)
    private val usuarioDao = database.usuarioDao()
    private val habitDao = database.habitDao()
    private val sesionManager = SesionManager(application)

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser

    private val _totalXP = MutableStateFlow(0)
    val totalXP: StateFlow<Int> = _totalXP

    private val _currentLevel = MutableStateFlow(1)
    val currentLevel: StateFlow<Int> = _currentLevel

    private val _habitsCreated = MutableStateFlow(0)
    val habitsCreated: StateFlow<Int> = _habitsCreated

    private val _habitsCompleted = MutableStateFlow(0)
    val habitsCompleted: StateFlow<Int> = _habitsCompleted

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val _editName = MutableStateFlow("")
    val editName: StateFlow<String> = _editName

    private val _editEmail = MutableStateFlow("")
    val editEmail: StateFlow<String> = _editEmail

    private val _editClass = MutableStateFlow("")
    val editClass: StateFlow<String> = _editClass

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val nombre = sesionManager.obtenerNombreUsuario() ?: ""
                val correo = sesionManager.obtenerCorreo() ?: ""
                val clase = sesionManager.obtenerClase() ?: "GUERRERO"
                val xp = sesionManager.obtenerXP()
                val nivel = sesionManager.obtenerNivel()

                // Construir usuario desde sesión
                val usuario = Usuario(
                    id = sesionManager.obtenerUsuarioId(),
                    nombre = nombre,
                    correo = correo,
                    contraseña = "",
                    clase = clase,
                    xpTotal = xp,
                    nivelActual = nivel
                )

                _currentUser.value = usuario
                _editName.value = nombre
                _editEmail.value = correo
                _editClass.value = clase
                _totalXP.value = xp
                _currentLevel.value = nivel

                // Cargar hábitos desde Room
                val habits = habitDao.getAllHabitsOnce()
                _habitsCreated.value = habits.size
                _habitsCompleted.value = habits.count { it.completado }

            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar perfil: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun enableEditMode() {
        _isEditMode.value = true
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun cancelEdit() {
        _isEditMode.value = false
        _currentUser.value?.let { user ->
            _editName.value = user.nombre
            _editEmail.value = user.correo
            _editClass.value = user.clase
        }
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun updateEditName(name: String) {
        _editName.value = name
        _errorMessage.value = null
    }

    fun updateEditEmail(email: String) {
        _editEmail.value = email
        _errorMessage.value = null
    }

    fun updateEditClass(userClass: String) {
        _editClass.value = userClass
    }

    fun saveProfileChanges() {
        if (_editName.value.isBlank()) {
            _errorMessage.value = "El nombre no puede estar vacío"
            return
        }
        if (_editName.value.length < 2) {
            _errorMessage.value = "El nombre debe tener al menos 2 caracteres"
            return
        }
        if (_editEmail.value.isBlank()) {
            _errorMessage.value = "El correo no puede estar vacío"
            return
        }
        if (!isValidEmail(_editEmail.value)) {
            _errorMessage.value = "El correo no es válido"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Actualizar sesión local
                sesionManager.guardarSesion(
                    usuarioId = sesionManager.obtenerUsuarioId(),
                    nombre = _editName.value.trim(),
                    correo = _editEmail.value.trim(),
                    clase = _editClass.value
                )

                // Actualizar estado local
                _currentUser.value = _currentUser.value?.copy(
                    nombre = _editName.value.trim(),
                    correo = _editEmail.value.trim(),
                    clase = _editClass.value
                )

                _isEditMode.value = false
                _successMessage.value = "Perfil actualizado exitosamente"

            } catch (e: Exception) {
                _errorMessage.value = "Error al guardar cambios"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        sesionManager.cerrarSesion()
        onLogout()
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun getAvailableClasses(): List<String> {
        return listOf("GUERRERO", "MAGO", "PICARO", "ARQUERO")
    }

    fun getClassIcon(userClass: String): String {
        return when (userClass.uppercase()) {
            "GUERRERO" -> "⚔️"
            "MAGO"     -> "🔮"
            "PICARO"   -> "🗡️"
            "ARQUERO"  -> "🏹"
            else       -> "🛡️"
        }
    }

    fun getClassDisplayName(userClass: String): String {
        return when (userClass.uppercase()) {
            "GUERRERO" -> "Guerrero"
            "MAGO"     -> "Mago"
            "PICARO"   -> "Pícaro"
            "ARQUERO"  -> "Arquero"
            else       -> userClass
        }
    }

    fun getLevelProgressPercentage(): Float {
        val xp = sesionManager.obtenerXP()
        return calcularNivel(xp).porcentaje
    }

    fun getCurrentLevel(): Int {
        return calcularNivel(sesionManager.obtenerXP()).nivel
    }

    fun getXpForNextLevel(): Int {
        return calcularNivel(sesionManager.obtenerXP()).xpParaSiguiente
    }

    fun getXpInLevel(): Int {
        return calcularNivel(sesionManager.obtenerXP()).xpEnNivel
    }

    fun getTotalXPFormatted(): String {
        return "%,d".format(sesionManager.obtenerXP())
    }

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

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}