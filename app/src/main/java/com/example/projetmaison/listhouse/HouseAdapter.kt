package com.example.projetmaison.listhouse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.projetmaison.R

class HouseAdapter(
    private val context: Context,
    private val dataSource: ArrayList<House>,
    var intentHouseID : String? = null
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): House = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.activity_singlehouses2, parent, false)

        val house = getItem(position)

        val houseIdTextView = rowView.findViewById<TextView>(R.id.text21)
        val ownerTextView = rowView.findViewById<TextView>(R.id.text22)
        //val housetitle =rowView.findViewById<TextView>(R.id.text11)
        //val ownertitle = rowView.findViewById<TextView>(R.id.text12)

        //housetitle.text= "Maison"
        //ownertitle.text = "Propriétaire"

        houseIdTextView.text= "House ${house.houseId.toString()}"
        if (house.owner) {intentHouseID = house.houseId.toString()}
        ownerTextView.text = if (house.owner) "Owner" else "Member"


        rowView.setOnClickListener {
            if (context is HousesActivity) {
                context.moveToDevicePage(house.houseId)
            }
        }

        return rowView
    }
}