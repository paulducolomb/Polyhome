package com.example.projetmaison.houseDevice

import java.util.ArrayList

data class Device(
    val id: String,
    val type: String, // Peut être "sliding-shutter", "rolling-shutter", "garage-door", ou "light"
    val availableCommands: ArrayList<String>,
    val opening: Number, // Seulement pour les "shutter" et "garage-door"
    val openingMode : Number,  // a été ajouté mais pas forcement utile
    val power: Number    // Seulement pour les "light"
){}

