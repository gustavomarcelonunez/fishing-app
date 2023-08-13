package com.example.demo.fragment.topics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.demo.R
import com.example.demo.databinding.FragmentTopicsBinding

class TopicsFragment : Fragment() {
    private var _binding: FragmentTopicsBinding? = null
    private val binding get() = _binding!!

    // mas variables aqui

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopicsBinding.inflate(inflater, container, false)

        _binding!!.goBackButton2.setOnClickListener { goBack() }


        return binding.root
    }

    private fun goBack() {
        findNavController().popBackStack()
    }


}