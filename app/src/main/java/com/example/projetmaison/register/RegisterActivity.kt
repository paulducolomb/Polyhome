package com.example.projetmaison.register

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetmaison.api.Api
import com.example.projetmaison.R
import com.example.projetmaison.toaster.ToasterObj

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    // Fonction de navigation vers la page de connexion
    fun goToLogin(view: View) {
        finish()
    }

    // Fonction appelée lors de la soumission du formulaire
    fun register(view: View) {
        val login = findViewById<EditText>(R.id.txtRegisterLogin).text.toString()
        val password = findViewById<EditText>(R.id.txtRegisterPassword).text.toString()

        // Création de l'objet RegisterData avec les informations saisies
        val registerData = RegisterData(
            login = login,
            password = password
        )

        // Appel à l'API pour envoyer les données
        Api().post<RegisterData>(
            "https://polyhome.lesmoulinsdudev.com/api/users/register",
            registerData,
            ::registerSuccess
        )
    }

    // Méthode de succès appelée après l'appel API
    private fun registerSuccess(responseCode: Int) {
        if (responseCode == 200) {
            runOnUiThread { Toast.makeText(applicationContext, "Compte créé avec succès !", Toast.LENGTH_SHORT).show()
            finish()
            }
        } else {
            ToasterObj.ToastResponseCode(this, responseCode)
        }
    }
}
