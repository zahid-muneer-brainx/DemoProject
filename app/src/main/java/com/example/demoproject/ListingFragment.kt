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
    lateinit var recyclerViewAdapter: ListingViewAdapter
    private lateinit var binding: FragmentSearchBinding
    private var page = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getList(page)
        model.recyclerDataArrayList.observe(viewLifecycleOwner) {
            upload(it)
        }
        model.searchDataArrayList.observe(viewLifecycleOwner) {
            search(it)
        }
        model.failedResponse.observe(viewLifecycleOwner) {
            requestFailedMessage(it)
        }
        binding.sv1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                model.searchByName(query, page)
                println(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                model.searchByName(newText, page)
                return false
            }
        })
        binding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                page++
                if (page < (model.recyclerDataArrayList.value?.metaModel?.total_pages?.toInt()
                        ?: 1)
                ) {
                    model.getList(page)
                } else {
                    Toast.makeText(requireContext(), "No More Contacts", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun requestFailedMessage(it: String?) =
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun upload(listingDataModel: ListingDataModel) {


        for (i in listingDataModel.cardContactModels.indices) {
            recyclerViewAdapter = ListingViewAdapter(listingDataModel)
            val manager = LinearLayoutManager(requireContext())
            binding.RTview.layoutManager = manager
            binding.RTview.adapter = recyclerViewAdapter
        }

    }

    private fun search(listingDataModel: ListingDataModel) {
        for (i in listingDataModel.cardContactModels.indices) {
            recyclerViewAdapter =
                ListingViewAdapter(listingDataModel)
            val manager = LinearLayoutManager(requireContext())
            binding.RTview.layoutManager = manager
            binding.RTview.adapter = recyclerViewAdapter
        }

    }
}