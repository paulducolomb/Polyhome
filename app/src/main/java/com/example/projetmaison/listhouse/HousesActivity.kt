package com.example.projetmaison.listhouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.example.projetmaison.R
import com.example.projetmaison.toaster.ToasterObj
import com.example.projetmaison.api.Api
import com.example.projetmaison.houseDevice.HouseDeviceActivity
import com.example.projetmaison.houseUser.HouseUserActivity
import com.example.projetmaison.login.LoginActivity

class HousesActivity : AppCompatActivity() {

    private var token: String? = null

    private val houses: ArrayList<House> = ArrayList()
    private lateinit var housesAdapter: HouseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_houses)

        token = intent.getStringExtra("token")
        initHousesListView()
        loadHouses()
    }

    override fun onResume() {
        super.onResume()
        loadHouses()
    }

    private fun loadHouses() {
        Api().get<ArrayList<House>>(
            "https://polyhome.lesmoulinsdudev.com/api/houses",
            ::handleHousesResponse,
            token
        )
    }

    private fun handleHousesResponse(responseCode: Int, loadedHouses: ArrayList<House>?) {
        if (responseCode == 200 && loadedHouses != null) {
            houses.clear()
            houses.addAll(loadedHouses)
            runOnUiThread { housesAdapter.notifyDataSetChanged() }
        } else {
            ToasterObj.ToastResponseCode(this, responseCode)
        }
    }

    private fun initHousesListView() {
        val listView = findViewById<ListView>(R.id.lstHouses)
        housesAdapter = HouseAdapter(this, houses)
        listView.adapter = housesAdapter
    }

//    fun movehousepage(view: View) {
//        val intentToMoveHouse = Intent(this, MoveHouseActivity::class.java)
//        intentToMoveHouse.putExtra("token", token)
//        intentToMoveHouse.putExtra("houseId",housesAdapter.intentHouseID)
//        startActivity(intentToMoveHouse)
//    }

    fun logout(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun movetoHouseUser (view: View){
        val intentToHouseUser = Intent(this, HouseUserActivity::class.java)
        intentToHouseUser.putExtra("token", token)
        intentToHouseUser.putExtra("houseId",housesAdapter.intentHouseID)
        startActivity(intentToHouseUser)
    }

    //plus besoin car bouton supprimé et fonction remplacer par celle d'en dessous
//    fun movetoHouseDevice (view: View){
//        val intenttodevice = Intent(this, HouseDeviceActivity::class.java)
//        intenttodevice.putExtra("token", token)
//        intenttodevice.putExtra("houseId",housesAdapter.intentHouseID)
//        startActivity(intenttodevice)
//    }

    fun moveToDevicePage(houseId: Int) {
        val intentToDevice = Intent(this, HouseDeviceActivity::class.java)
        intentToDevice.putExtra("token", token)
        intentToDevice.putExtra("houseId", houseId.toString())
        startActivity(intentToDevice)
    }


}
