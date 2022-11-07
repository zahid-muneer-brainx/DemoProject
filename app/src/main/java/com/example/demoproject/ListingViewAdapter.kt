package com.example.demoproject

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel


class ListingViewAdapter(
    private val contactDataArrayList: ListingData, private val mcontext: Context
) :
    RecyclerView.Adapter<ListingViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListingViewAdapter.ViewHolder {
        // Inflate Layout
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        var myView = inflater.inflate(R.layout.customlistview, parent, false)
        return ViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Set the data to textview from our modal class.
        holder.NameTV.setText(contactDataArrayList.cardContacts[position].full_name)
        holder.emailTV.setText(contactDataArrayList.cardContacts[position].email)
        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlbxdiKTlBWwhGDX-GcYMHOgcR7e1JgRssgw&usqp=CAU").into(holder.pic)
    }

    override fun getItemCount(): Int {
        // this method returns the size of recyclerview
        return contactDataArrayList.cardContacts.size
    }

    // View Holder Class to handle Recycler View.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating variables for our views.
        val NameTV: TextView
        val emailTV: TextView
        val pic:ImageView

        init {
            // initializing our views with their ids.
            NameTV = itemView.findViewById(R.id.contactname)
            emailTV = itemView.findViewById(R.id.contactemail)
            pic=itemView.findViewById(R.id.profileimg)
        }

    }


}