package com.example.demoproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoproject.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListingFragment @Inject constructor() : Fragment() {


    private val model: ListingViewModel by viewModels()
    private var recyclerViewAdapter: ListingViewAdapter? = null
    lateinit var binding: FragmentSearchBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getlist()
        search(model)
        upload(model)
        binding.sv1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                model.searchByName(query)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                model.searchByName(newText)
                return false
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun upload(model: ListingViewModel) {
        model.recyclerDataArrayList.observe(viewLifecycleOwner) {
            for (i in model.recyclerDataArrayList.value?.cardContactModels?.indices!!) {
                recyclerViewAdapter =
                    ListingViewAdapter(model.recyclerDataArrayList.value!!)
                // below line is to set layout manager for our recycler view.
                val manager = LinearLayoutManager(requireContext())
                // setting layout manager for our recycler view.
                binding.RTview.layoutManager = manager
                // below line is to set adapter to our recycler view.
                binding.RTview.adapter = recyclerViewAdapter
            }
        }
    }

    private fun search(model: ListingViewModel) {
        model.searchDataArrayList.observe(viewLifecycleOwner) {
            for (i in model.searchDataArrayList.value?.cardContactModels?.indices!!) {
                recyclerViewAdapter =
                    ListingViewAdapter(model.searchDataArrayList.value!!)
                // below line is to set layout manager for our recycler view.
                val manager = LinearLayoutManager(requireContext())
                // setting layout manager for our recycler view.
                binding.RTview.layoutManager = manager
                // below line is to set adapter to our recycler view.
                binding.RTview.adapter = recyclerViewAdapter
            }
        }
    }
}