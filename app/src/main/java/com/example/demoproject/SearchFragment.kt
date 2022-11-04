package com.example.demoproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoproject.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment @Inject constructor(): Fragment() {
    lateinit var dataStoreManager: PreferenceDataStore
    lateinit var Model:ListingViewModel
    lateinit var requestHeaders:RequestHeaders
    private var recyclerViewAdapter: ListingViewAdapter? = null
    lateinit var listRT:RecyclerView
    lateinit var binding: FragmentSearchBinding
    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore
    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        Model=ViewModelProvider(requireActivity())[ListingViewModel::class.java]
        lifecycleScope.launch {
            preferenceDataStore.getFromDataStore().collect {
                requestHeaders = RequestHeaders(
                    (it.uid),
                    (it.access_token),
                    (it.client)
                )
                println(requestHeaders.toString())
            }
        }
        Model.getlist(requestHeaders)

        upload(Model)
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

 private fun upload(Model:ListingViewModel)
 {
     Model.recyclerDataArrayList.observe(viewLifecycleOwner){
         for(i in Model.recyclerDataArrayList.value?.indices!!)
         {
             println("1111111111111111111111")
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