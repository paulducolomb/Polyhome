package com.example.projetmaison.houseDevice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetmaison.R
import com.example.projetmaison.toaster.ToasterObj
import com.example.projetmaison.api.Api
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.example.projetmaison.houseUser.HouseUserActivity
import com.google.android.material.tabs.TabLayout


class HouseDeviceActivity : AppCompatActivity() {

    private var token: String? = null
    private var houseId: String? = null

    private val shutters = ArrayList<Device>()
    private val leds = ArrayList<Device>()
    private val garage = ArrayList<Device>()

    private lateinit var shutterAdapter: DeviceAdapter
    private lateinit var garageAdapter: DeviceAdapter
    private lateinit var ledAdapter: DeviceAdapter


    private val handler = Handler(Looper.getMainLooper())
    private val updateTask = object : Runnable {
        override fun run() {
            loadDevices()
            handler.postDelayed(this, 1000) //je mets une seconde pour que l'appui sur mon bouton soit bien pris en compte
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devicehouse2)

        token = intent.getStringExtra("token")
        houseId = intent.getStringExtra("houseId")

        val devicetitle = findViewById<TextView>(R.id.titledevice)
        devicetitle.text = "Devices of house $houseId !"
        shutterAdapter = DeviceAdapter(this@HouseDeviceActivity, shutters)
        garageAdapter = DeviceAdapter(this@HouseDeviceActivity, garage)
        ledAdapter = DeviceAdapter(this@HouseDeviceActivity, leds)

//        initDeviceListViews()
        startAutoUpdate()
        loadDevices()

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val OneListView = findViewById<ListView>(R.id.deviceListView)
        val openbtn = findViewById<Button>(R.id.buttonon)
        val closebtn = findViewById<Button>(R.id.buttonoff)
        OneListView.adapter = shutterAdapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            when (tab.text.toString()) {
                "Rolling Shutters" -> {
                    OneListView.adapter = shutterAdapter
                    openbtn.text="OPEN ALL"
                    closebtn.text="CLOSE ALL"
                    openbtn.visibility = View.VISIBLE
                    closebtn.visibility = View.VISIBLE
                    openbtn.setOnClickListener { openAllShutters() }
                    closebtn.setOnClickListener { closeAllShutters()}
                }
                "Garage" ->{
                    OneListView.adapter = garageAdapter
                    openbtn.visibility = View.GONE
                    closebtn.visibility = View.GONE
                }
                "Lights" ->{
                    OneListView.adapter = ledAdapter
                    openbtn.text="TURN ON ALL"
                    closebtn.text="TURN OFF ALL"
                    openbtn.visibility = View.VISIBLE
                    closebtn.visibility = View.VISIBLE
                    openbtn.setOnClickListener { turnOnAllLeds() }
                    closebtn.setOnClickListener { turnOffAllLeds()}
                }

            }
        }


            override fun onTabUnselected(tab: TabLayout.Tab) {
                      }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Rien à faire ici
            }
        })

        //lancer garage en premier
        val initialTab = tabLayout.getTabAt(1)
        initialTab?.select()

    }
//    private fun initDeviceListViews() {
//        // Initialisation des listes de volets
//        val shutterListView = findViewById<ListView>(R.id.lstDeviceshuttleHouses)
//        shutterAdapter = DeviceAdapter(this, shutters)
//        shutterListView.adapter = shutterAdapter
//
//        // Initialisation des listes de LED
//        val ledListView = findViewById<ListView>(R.id.lstDeviceledHouses)
//        ledAdapter = DeviceAdapter(this, leds)
//        ledListView.adapter = ledAdapter
//    }

    private fun loadDevices() {
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices"

        Api().get<Devices>(
            url,
            ::handleDeviceResponse,
            token
        )
    }



    private fun handleDeviceResponse(responseCode: Int, loadedDevices: Devices?) {
        if (responseCode == 200 && loadedDevices != null) {
            shutters.clear()
            leds.clear()
            garage.clear()

            for (device in loadedDevices.devices) {
                when (device.type) {
                    "rolling shutter"-> shutters.add(device)
                    "garage door" ->garage.add(device)
                    "light" -> leds.add(device)
                }
            }

            runOnUiThread {
                shutterAdapter.notifyDataSetChanged()
                ledAdapter.notifyDataSetChanged()
                garageAdapter.notifyDataSetChanged()

            }
        } else {
            //ToasterObj.ToastResponseCode(this, responseCode)
        }
    }

    private fun startAutoUpdate() {
        handler.post(updateTask)
    }

    private fun stopAutoUpdate() {
        handler.removeCallbacks(updateTask)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoUpdate()
    }

    fun back(view: View) {
        finish()
    }

    fun sendCommand(deviceId: String, command: String) {
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command"
        val body = Command(command)

        Api().post<Command>(
            url,
            body,
            { responseCode -> handleresponse(responseCode, deviceId, command) },
            token
        )
    }
    fun closeAllShutters() {
        for (shutter in shutters) {
            sendCommand(shutter.id, "CLOSE")
        }
        Toast.makeText(this, "Tous les volets sont fermés", Toast.LENGTH_SHORT).show()
    }

    fun turnOffAllLeds() {
        for (led in leds) {
            sendCommand(led.id, "TURN OFF")
        }
        Toast.makeText(this, "Toutes les lumières sont éteintes", Toast.LENGTH_SHORT).show()
    }

    fun openAllShutters() {
        for (shutter in shutters) {
            sendCommand(shutter.id, "OPEN")
        }
        Toast.makeText(this, "Tous les volets sont ouverts", Toast.LENGTH_SHORT).show()
    }

    fun turnOnAllLeds() {
        for (led in leds) {
            sendCommand(led.id, "TURN ON")
        }
        Toast.makeText(this, "Toutes les lumières sont allumées", Toast.LENGTH_SHORT).show()
    }


    private fun handleresponse(responseCode: Int, deviceId: String, command: String) {
        if (responseCode == 200) {
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "Commande $command envoyée avec succès à $deviceId",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            ToasterObj.ToastResponseCode(this, responseCode)
        }
    }
}

