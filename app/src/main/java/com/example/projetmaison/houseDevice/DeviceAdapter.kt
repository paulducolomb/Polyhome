package com.example.projetmaison.houseDevice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.projetmaison.R
import com.google.android.material.tabs.TabLayout


class DeviceAdapter(
    private val context: Context,
    private val dataSource: ArrayList<Device>

) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): Device = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.activity_test, parent, false)

        val device = getItem(position)
        val idTextView = rowView.findViewById<TextView>(R.id.devicetext)
        val stateImageView = rowView.findViewById<ImageView>(R.id.statedevice)
        //val typeTextView = rowView.findViewById<TextView>(R.id.device2)
        //val stateTextView = rowView.findViewById<TextView>(R.id.device3)
        val btnOpen = rowView.findViewById<Button>(R.id.open_button)
        val btnStop = rowView.findViewById<Button>(R.id.stop_button)
        val btnClose = rowView.findViewById<Button>(R.id.close_button)
        //val progressBarShutter = rowView.findViewById<ProgressBar>(R.id.progressBar)


        idTextView.text = device.id


        //typeTextView.text = device.type


        when (device.type) {
            "light" -> {
                //stateTextView.text = device.power.toString()
                btnOpen.text = "TURN ON"
                btnClose.text = "TURN OFF"
                btnStop.visibility = View.GONE
                //progressBarShutter.visibility=View.GONE
                btnOpen.setOnClickListener {
                    if (device.power.toInt() == 1) {
                        Toast.makeText(context, "La lumière est déjà allumée", Toast.LENGTH_SHORT).show()
                    } else {
                        (context as HouseDeviceActivity).sendCommand(device.id, "TURN ON")
                    }
                }

                btnClose.setOnClickListener {
                    if (device.power.toInt() == 0) {
                        Toast.makeText(context, "La lumière est déjà éteinte", Toast.LENGTH_SHORT).show()
                    } else {
                        (context as HouseDeviceActivity).sendCommand(device.id, "TURN OFF")
                    }
                }


                if (device.power.toInt() == 1) {
                    stateImageView.setImageResource(R.drawable.ledon) // Image pour propriétaire
                    //ownerTextView.text="Propriétaire"

                } else {
                    stateImageView.setImageResource(R.drawable.ledoff) // Image pour propriétaire
                    //ownerTextView.text="Membre"
                }


            }
            "rolling shutter", "garage door" -> {

                //progressBarShutter.max = 1// Valeur maximale de la ProgressBar
                //progressBarShutter.progress = device.opening.toInt() ?: 0

                if (device.opening.toInt() == 1 && device.openingMode.toInt() == 0 ) {
                    stateImageView.setImageResource(R.drawable.shutteropen)
                    //ownerTextView.text="Propriétaire"

                }
                if (device.opening.toInt() == 0 && device.openingMode.toInt()==1) {
                    stateImageView.setImageResource(R.drawable.shutterclose)
                    //ownerTextView.text="Membre"
                }
                else {
                    //pas bouger
                }


                btnOpen.text = "OPEN"
                btnClose.text = "CLOSE"
                btnStop.text = "STOP"
                btnStop.visibility = View.VISIBLE // Ajout sinon plus de bouton stop sur les volets

                //progressBarShutter.progress = device.opening.toInt()

                btnOpen.setOnClickListener {
                    if (device.opening.toInt() == 1 && device.openingMode.toInt() == 0) {
                        Toast.makeText(context, "Le volet est déjà ouvert", Toast.LENGTH_SHORT).show()
                    } else {
                        (context as HouseDeviceActivity).sendCommand(device.id, "OPEN")
                    }
                }

                btnClose.setOnClickListener {
                    if (device.opening.toInt() == 0 && device.openingMode.toInt() == 1) {
                        Toast.makeText(context, "Le volet est déjà fermé", Toast.LENGTH_SHORT).show()
                    } else {
                        (context as HouseDeviceActivity).sendCommand(device.id, "CLOSE")
                    }
                }

                btnStop.setOnClickListener {
                    (context as HouseDeviceActivity).sendCommand(device.id, "STOP")
                }


            }
            else -> {
                btnOpen.visibility = View.GONE
                btnStop.visibility = View.GONE
                btnClose.visibility = View.GONE
            }
        }

        return rowView

    }
}