package com.example.projetmaison.houseUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.example.projetmaison.R
import com.example.projetmaison.toaster.ToasterObj
import com.example.projetmaison.api.Api
class HouseUserActivity : AppCompatActivity() {

    private var token: String? = null
    private var houseId: String? = null
    private val owners: ArrayList<HouseUser> = ArrayList()
    private val members: ArrayList<HouseUser> = ArrayList()
    private lateinit var ownerAdapter: HouseUserAdapter
    private lateinit var memberAdapter: HouseUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_houseuser)

        token = intent.getStringExtra("token")
        houseId = intent.getStringExtra("houseId")
        initListViews()
        loadHouseUser()
    }

    override fun onResume() {
        super.onResume()
        loadHouseUser() // Recharger la liste des utilisateurs
    }

    private fun loadHouseUser() {
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users"

        Api().get<ArrayList<HouseUser>>(
            url,
            ::handleHouseUserResponse,
            token
        )
    }

    private fun handleHouseUserResponse(responseCode: Int, loadedUsers: ArrayList<HouseUser>?) {
        if (responseCode == 200 && loadedUsers != null) {
            owners.clear()
            members.clear()

            loadedUsers.forEach { user ->     //séparer les membres des propriétaires
                if (user.owner.toInt() == 1) {
                    owners.add(user)
                } else {
                    members.add(user)
                }
            }

            runOnUiThread {
                ownerAdapter.notifyDataSetChanged()
                memberAdapter.notifyDataSetChanged()
            }
        } else {
            ToasterObj.ToastResponseCode(this, responseCode)
        }
    }

    private fun initListViews() {
        val ownerListView = findViewById<ListView>(R.id.lstUserHousesOwner)
        val userListView = findViewById<ListView>(R.id.lstUserHouses)

        ownerAdapter = HouseUserAdapter(this, owners)
        memberAdapter = HouseUserAdapter(this, members)

        ownerListView.adapter = ownerAdapter
        userListView.adapter = memberAdapter
    }

    fun AddHouse (view : View){
        val editText = findViewById<EditText>(R.id.editTextText3)
        val userlogin = editText.text.toString()
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users"
        val moveAccesHouse = MoveAccesHouse(
            userLogin = userlogin,
        )
        Api().post<MoveAccesHouse>(url,moveAccesHouse, ::addResponse, token )
        editText.setText(null)
    }

    private fun addResponse(responseCode: Int) {
        if (responseCode == 200) {
            runOnUiThread {
                Toast.makeText(this, "Accès accordé avec succès", Toast.LENGTH_SHORT).show()
                loadHouseUser()
            }
        } else {
            ToasterObj.ToastResponseCode(this, responseCode)
        }
    }

    fun DeleteUser (userlogin : String){
        val url = "https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/users"

        val moveAccesHouse = MoveAccesHouse(
            userLogin = userlogin,
        )
        Api().delete<MoveAccesHouse>( url,moveAccesHouse, ::deleteResponse, token )
    }
    private fun deleteResponse(responseCode: Int) {
        if (responseCode == 200) {
            runOnUiThread {
                Toast.makeText(this, "Accès supprimé avec succès", Toast.LENGTH_SHORT).show()
                loadHouseUser()
            }
        } else {
            ToasterObj.ToastResponseCode(this, responseCode)
        }
    }
    fun back(view: View) {
        finish()
    }
}
