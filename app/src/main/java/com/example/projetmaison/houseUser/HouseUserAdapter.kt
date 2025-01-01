package com.example.projetmaison.houseUser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.projetmaison.R


class HouseUserAdapter(
    private val context: Context,
    private val dataSource: ArrayList<HouseUser>
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): HouseUser = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.activity_singlehouses, parent, false)

        val houseUser = getItem(position)
        //val usertitle =rowView.findViewById<TextView>(R.id.text11)
        //val ownertitle = rowView.findViewById<TextView>(R.id.text12)
        val userloginTextView = rowView.findViewById<TextView>(R.id.text21)
        //val ownerTextView = rowView.findViewById<TextView>(R.id.text22)
        val userImageView = rowView.findViewById<ImageView>(R.id.imageView3)
        val deleteBtn = rowView.findViewById<ImageView>(R.id.deleters)


        //usertitle.text= "Utilisateur"
        //ownertitle.text = "Propriétaire"
        userloginTextView.text= houseUser.userLogin
        //ownerTextView.text = if (houseUser.owner.toInt() == 1) "Propriétaire" else "Membre"

        if (houseUser.owner.toInt() == 1) {
            userImageView.setImageResource(R.drawable.proprietaireuserlogo) // Image pour propriétaire
            //ownerTextView.text="Propriétaire"
            deleteBtn.visibility = View.GONE

        } else {
            userImageView.setImageResource(R.drawable.memberuserlogo) // Image pour membre
            //ownerTextView.text="Membre"
        }

        userloginTextView.text = houseUser.userLogin

        // Gestion du clic sur l'image de suppression
        deleteBtn.setOnClickListener {
            (context as? HouseUserActivity)?.DeleteUser(houseUser.userLogin)
        }

        return rowView
    }
}