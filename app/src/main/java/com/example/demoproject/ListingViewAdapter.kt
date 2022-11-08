package com.example.demoproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class ListingViewAdapter(
    private val contactDataArrayList: ListingDataModel
) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Inflate Layout
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val myView = inflater.inflate(R.layout.customlistview, parent, false)
        return ViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Set the data to textview from our modal class.
        holder.nameTV.text = contactDataArrayList.cardContactModels[position].full_name
        holder.emailTV.text = contactDataArrayList.cardContactModels[position].email
        Picasso.get()
            .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlbxdiKTlBWwhGDX-GcYMHOgcR7e1JgRssgw&usqp=CAU")
            .into(holder.pic)
    }

    override fun getItemCount(): Int {
        // this method returns the size of recyclerview
        return contactDataArrayList.cardContactModels.size
    }

}