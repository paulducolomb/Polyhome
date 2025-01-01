package com.example.projetmaison.toaster

import android.content.Context
import android.widget.Toast

object ToasterObj {
    fun ToastResponseCode(context: Context, responseCode: Int) {

        val message = when (responseCode) {
            403 -> "Accès interdit : vérifiez votre token"
            404 -> "Aucun utilisateur ne correspond aux identifiants donnés"
            409 -> "Le login est déjà utilisé par un autre compte"
            500 -> "Erreur serveur, veuillez réessayer plus tard"
            else -> "Erreur inconnue : $responseCode"
        }

        // Assurez-vous d'exécuter sur le thread principal
        (context as? android.app.Activity)?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}