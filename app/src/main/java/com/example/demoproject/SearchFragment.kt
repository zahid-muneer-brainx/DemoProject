package com.example.demoproject

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoproject.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class SearchFragment @Inject constructor(): Fragment() {


    private val Model: ListingViewModel by viewModels()
    private var recyclerViewAdapter: ListingViewAdapter? = null
    lateinit var listRT:RecyclerView
    lateinit var serachview:SearchView
    lateinit var binding: FragmentSearchBinding
    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore
    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        Model.getlist()

        upload(Model)
        serachview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Model.searchByName(query)
                search(Model)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Model.searchByName(newText)
                search(Model)
                return false
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchBinding.inflate(layoutInflater)
        listRT=binding.RTview
        serachview=binding.sv1
        return binding.root
    }

 private fun upload(Model:ListingViewModel)
 {
     Model.recyclerDataArrayList.observe(viewLifecycleOwner){
         for(i in Model.recyclerDataArrayList.value?.cardContacts?.indices!!)
         {
             recyclerViewAdapter =
                 ListingViewAdapter(Model.recyclerDataArrayList.value!!, requireContext())
             // below line is to set layout manager for our recycler view.
             val manager = LinearLayoutManager(requireContext())
             // setting layout manager for our recycler view.
             listRT.layoutManager = manager
             // below line is to set adapter to our recycler view.
             listRT.adapter = recyclerViewAdapter
         }
     }
 }
    private fun search(Model:ListingViewModel)
    {
        Model.searchDataArrayList.observe(viewLifecycleOwner){
            for(i in Model.recyclerDataArrayList.value?.cardContacts?.indices!!)
            {
                recyclerViewAdapter =
                    ListingViewAdapter(Model.recyclerDataArrayList.value!!, requireContext())
                // below line is to set layout manager for our recycler view.
                val manager = LinearLayoutManager(requireContext())
                // setting layout manager for our recycler view.
                listRT.layoutManager = manager
                // below line is to set adapter to our recycler view.
                listRT.adapter = recyclerViewAdapter
            }
        }
    }
}