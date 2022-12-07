package com.example.demoproject

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // creating variables for our views.
    val nameTV: TextView
    val emailTV: TextView
    val pic: ImageView

    init {
        // initializing our views with their ids.
        nameTV = itemView.findViewById(R.id.contactname)
        emailTV = itemView.findViewById(R.id.contactemail)
        pic=itemView.findViewById(R.id.profileimg)
    }

}