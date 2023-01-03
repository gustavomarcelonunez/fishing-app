package com.example.demo.fragment.list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.model.Regulation
import com.example.demo.adapter.RegulationListAdapter
import com.example.demo.databinding.FragmentRegulationListBinding
import java.util.*
import kotlin.collections.ArrayList

class RegulationListFragment : Fragment() {

    private lateinit var regulationList: RecyclerView
    private var regulationListAdapter = RegulationListAdapter()

    private lateinit var newArrayList: ArrayList<Regulation>
    private lateinit var tempArrayList: ArrayList<Regulation>
    private var _binding: FragmentRegulationListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentRegulationListBinding.inflate(inflater, container, false)
        _binding!!.homeActionButton.setOnClickListener { goHome() }


        if (findNavController().previousBackStackEntry?.destination?.displayName!! == "com.example.demo:id/main_fragment") {
            _binding!!.homeActionButton.hide()
        }
        newArrayList = arrayListOf<Regulation>()
        tempArrayList = arrayListOf<Regulation>()

        regulationList = binding.list
        regulationList.adapter = regulationListAdapter
        regulationListAdapter.regulations = Regulation.data


        newArrayList.addAll(regulationListAdapter.regulations)
        tempArrayList.addAll(newArrayList)

        val view = binding.root
        return view
    }

    private fun goHome() {
        findNavController().navigate(R.id.home_fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.regulation_filter_menu, menu)
        val item = menu.findItem(R.id.regulation_action_filter)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    newArrayList.forEach {
                        if (it.zone.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            tempArrayList.add(it)
                            Log.i("AAAA", it.toString())
                        }
                    }
                    regulationListAdapter.regulations = tempArrayList
                } else {
                    tempArrayList.clear()
                    tempArrayList.addAll(newArrayList)
                    regulationListAdapter.regulations = tempArrayList
                }
                return false
            }
        })
    }
}