package com.example.demoproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoproject.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListingFragment @Inject constructor() : Fragment() {


    private val model: ListingViewModel by viewModels()
    private var recyclerViewAdapter: ListingViewAdapter? = null
    private lateinit var binding: FragmentSearchBinding
    private var page=1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.recyclerDataArrayList.observe(viewLifecycleOwner) {
            upload()
        }
        model.searchDataArrayList.observe(viewLifecycleOwner) {
            search()
        }
        binding.sv1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                model.searchByName(query,page)
                println(query)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                model.searchByName(newText,page)
                return false
            }
        })
    binding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
            page++
            if(page < (model.recyclerDataArrayList.value?.metaModel?.total_pages?.toInt() ?: 1)) {
                model.getList(page)
                upload()
            }
            else{
                Toast.makeText(requireContext(),"No More Contacts",Toast.LENGTH_SHORT).show()
            }
        }
    })
        println("when is printed")
        model.getList(page)
        println("After function")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun upload() {


            for (i in model.recyclerDataArrayList.value?.cardContactModels?.indices!!) {
                recyclerViewAdapter = ListingViewAdapter(model.recyclerDataArrayList.value!!)
                val manager = LinearLayoutManager(requireContext())
                binding.RTview.layoutManager = manager
                binding.RTview.adapter = recyclerViewAdapter
            }

    }

    private fun search() {
            for (i in model.searchDataArrayList.value?.cardContactModels?.indices!!) {
                recyclerViewAdapter =
                    ListingViewAdapter(model.searchDataArrayList.value!!)
                val manager = LinearLayoutManager(requireContext())
                binding.RTview.layoutManager = manager
                binding.RTview.adapter = recyclerViewAdapter
            }

    }
}