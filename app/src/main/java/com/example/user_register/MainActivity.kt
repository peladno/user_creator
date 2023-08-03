package com.example.user_register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.user_register.Model.User
import com.example.user_register.Model.UserDataBase
import com.example.user_register.Model.UserRepository
import com.example.user_register.ViewModel.UserViewModel
import com.example.user_register.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        val addButton: Button = binding.addButton
        val deleteButton: Button = binding.deleteButton
        val showButton: Button = binding.showButton
        val gameUsernameEditText: EditText = binding.gameUsernameEditText
        val fullNameEditText: EditText = binding.fullNameEditText
        val ageEditText: EditText = binding.ageEditText

        addButton.setOnClickListener {
            val gameUsername = gameUsernameEditText.text.toString().trim()
            val fullName = fullNameEditText.text.toString().trim()
            val ageText = ageEditText.text.toString().trim()

            // Validaciones
            if (gameUsername.isEmpty() || fullName.isEmpty() || ageText.isEmpty()) {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!gameUsername.matches(Regex("[a-zA-Z]+"))) {
                Toast.makeText(this, "Nombre de usuario en el juego inválido", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!fullName.matches(Regex("[a-zA-Z ]+"))) {
                Toast.makeText(this, "Nombre completo inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!ageText.matches(Regex("[0-9]+"))) {
                Toast.makeText(this, "Edad inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toInt()

            val newUser = User(0, gameUsername, fullName, age)
            userViewModel.insertUser(newUser)

            // Limpiar los campos de entrada después de agregar un usuario
            gameUsernameEditText.text.clear()
            fullNameEditText.text.clear()
            ageEditText.text.clear()

            Toast.makeText(this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show()
        }


        deleteButton.setOnClickListener {
            // Observa los cambios en la lista de usuarios
            userViewModel.allUser.observe(this) { userList ->
                if (userList.isNullOrEmpty()) {
                    Toast.makeText(
                        this,
                        "No hay usuarios registrados para eliminar",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val userNames = userList.map { it.fullName }.toTypedArray()

                    AlertDialog.Builder(this)
                        .setTitle("Eliminar Usuario")
                        .setItems(userNames) { _, position ->
                            val selectedUser = userList[position]
                            // Eliminar el usuario seleccionado utilizando el UserViewModel
                            userViewModel.deleteUser(selectedUser)
                            Toast.makeText(
                                this,
                                "Usuario eliminado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
            }
        }


        showButton.setOnClickListener {
            // Observa los cambios en la lista de usuarios ya que no se pueden acceder directamente
            // en el View model
            userViewModel.allUser.observe(this) { userList ->
                if (userList.isNullOrEmpty()) {
                    Toast.makeText(this, "No hay usuarios registrados", Toast.LENGTH_SHORT).show()
                } else {
                    val userNames = userList.map { it.fullName }
                    AlertDialog.Builder(this)
                        .setTitle("Usuarios Registrados")
                        .setMessage(userNames.joinToString("\n"))
                        .setPositiveButton("Aceptar", null)
                        .show()
                }
            }
        }


    }

    override fun onStop() {
        super.onStop()
        // Detener la observación de cambios en allUsers para evitar pérdidas de memoria
        userViewModel.allUser.removeObservers(this)
    }
}
