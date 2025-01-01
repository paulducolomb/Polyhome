package com.example.projetmaison.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projetmaison.api.Api
import com.example.projetmaison.listhouse.HousesActivity
import com.example.projetmaison.R
import com.example.projetmaison.toaster.ToasterObj
import com.example.projetmaison.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun registerNewAccount(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun login(view: View) {
        val login = findViewById<EditText>(R.id.txtLogin).text.toString()
        val password = findViewById<EditText>(R.id.txtPassword).text.toString()

        val loginData = LoginData(
            login = login,
            password = password
        )

        Api().post<LoginData, DataResponseLogin>(
            "https://polyhome.lesmoulinsdudev.com/api/users/auth",
            loginData,
            ::loginSuccess
        )
    }

    // Fonction appelée après la tentative de connexion
    private fun loginSuccess(responseCode: Int, data: DataResponseLogin?) {
        if (responseCode == 200 && data != null) {
            val ordersIntent = Intent(this, HousesActivity::class.java)
            ordersIntent.putExtra("token", data.token)
            startActivity(ordersIntent)
            finish()
        } else {
            ToasterObj.ToastResponseCode(this, responseCode)
        }
    }
}
