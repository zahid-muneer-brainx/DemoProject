package com.example.demoproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoproject.databinding.FragmentSearchBinding
import javax.inject.Inject

class SearchFragment @Inject constructor(): Fragment() {
   lateinit var Model:ListingViewModel
    private var recyclerViewAdapter: ListingViewAdapter? = null
    lateinit var listRT:RecyclerView
    lateinit var binding: FragmentSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data:ArrayList<ListingData>? =Model.getlist()
        for (i in data!!.indices) {
            recyclerViewAdapter =
                ListingViewAdapter(data, requireContext())
            // below line is to set layout manager for our recycler view.
            val manager = LinearLayoutManager(requireContext())
            // setting layout manager for our recycler view.
            listRT!!.layoutManager = manager
            // below line is to set adapter to our recycler view.
            listRT!!.adapter = recyclerViewAdapter
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchBinding.inflate(layoutInflater)
        listRT=binding.RTview
        Model= ViewModelProvider(requireActivity())[ListingViewModel::class.java]

        return binding.root
    }

}