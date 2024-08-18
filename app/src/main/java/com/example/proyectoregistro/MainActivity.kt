package com.example.proyectoregistro

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoregistro.beans.Usuario
import com.example.proyectoregistro.repository.AuthRespository
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.SocketException


class MainActivity : AppCompatActivity() {

    private lateinit var txtNombreRegistro:EditText
    private lateinit var txtEmailRegistro:EditText
    private lateinit var txtPasswordRegistro:EditText
    private lateinit var  txtConfirmarPasswordRegistro:EditText
    private  lateinit var btnRegistrar:Button

    private lateinit var txtMensajeError:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        initComponents()
        initEvents()
    }
    private fun initComponents(){
        txtNombreRegistro = findViewById(R.id.txtNombreRegistro)
        txtEmailRegistro = findViewById(R.id.txtEmailRegistro)
        txtPasswordRegistro = findViewById(R.id.txtPasswordRegistro)
        txtConfirmarPasswordRegistro = findViewById(R.id.txtConfirmarPasswordRegistro)
        txtMensajeError = findViewById(R.id.txtMensajeError)
        btnRegistrar=findViewById(R.id.btnRegistrar)
    }
    private fun initEvents(){
        btnRegistrar.setOnClickListener {
            if (validacion()){
                registrarApi()
            }
        }
    }
    private fun validacion():Boolean{
        var esValido = true
        limpiarCampoMensajeError()
        //validar nombre
        if (txtNombreRegistro.text.toString().isBlank()){
            txtNombreRegistro.error="Debe ingresar su nombre completo"
            esValido = false
        }
        //validar email
        if (txtEmailRegistro.text.toString().isBlank()){
            txtEmailRegistro.error="Debe ingresar un email valido"
            esValido=true
        }
        //validar password
        if (txtPasswordRegistro.text.toString().isBlank()){
            txtPasswordRegistro.error="Debe ingresar una contraseña"
            esValido=false
        }
        //validar password de verificacion
        if (txtConfirmarPasswordRegistro.text.toString().isBlank()){
            txtConfirmarPasswordRegistro.error="Debe repetir la contraseña"
            esValido=false
        }
        //validar que las contraseñas coincidan
        val password = txtPasswordRegistro.text.toString()
        val confimPassword = txtConfirmarPasswordRegistro.text.toString()
        if (password != confimPassword){
            esValido=false
            txtMensajeError.visibility= View.VISIBLE
            txtMensajeError.text="Las contraseñas no coinciden"
            txtPasswordRegistro.setText("")
            txtConfirmarPasswordRegistro.setText("")
        }
        return esValido
    }
    private fun limpiarCampoMensajeError(){
        txtMensajeError.text=""
        txtMensajeError.visibility=View.INVISIBLE

    }
    private fun registrarApi(){
        val autoRepository = AuthRespository()
        val usuario = Usuario(0,txtNombreRegistro.text.toString(),txtEmailRegistro.text.toString(),txtPasswordRegistro.text.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val response = autoRepository.registrarse(usuario)
            try {
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        //100 - 200 -300
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                        Log.i("respuesta del registro", prettyJson)

                    } else {
                        //400 -500
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(JsonParser.parseString(response.errorBody()?.string()))
                        val jsonresponse = JSONObject(prettyJson)
                        val mensaje = jsonresponse.getString("message")
                        txtMensajeError.text=mensaje
                        txtMensajeError.visibility=View.VISIBLE
                        Log.i("respuesta de registro erro", prettyJson)
                    }
                }

            } catch (error: Exception) {
                // Manejo del error
                Log.e("NetworkError", "SocketException: ${error.message}")
            }
        }
    }
}