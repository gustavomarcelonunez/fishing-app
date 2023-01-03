package com.example.demo.fragment.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demo.Cordinadora
import com.example.demo.R
import com.example.demo.databinding.FragmentFishingTypeBinding

class FishingTypeFragment : Fragment() {
    private var _binding: FragmentFishingTypeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFishingTypeBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.sportFishingRadioButton.setOnClickListener {
            click_en_radio_button(R.id.sportFishingRadioButton)
        }
        binding.scientistFishingRadioButton.setOnClickListener {
            click_en_radio_button(R.id.scientistFishingRadioButton)
        }
        binding.touristicFishingRadioButton.setOnClickListener {
            click_en_radio_button(R.id.touristicFishingRadioButton)
        }
        binding.comercialFishingRadioButton.setOnClickListener {
            click_en_radio_button(R.id.comercialFishingRadioButton)
        }



        return view
    }

    private fun click_en_radio_button(id_radio_button: Int) {
        val index = when (id_radio_button) {
            R.id.sportFishingRadioButton -> 0
            R.id.scientistFishingRadioButton -> 1
            R.id.touristicFishingRadioButton -> 2
            R.id.comercialFishingRadioButton -> 3
            else -> 0
        }
        val activity = getActivity()
        if (activity is Cordinadora) {
            activity.onChangeFishingType(index)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}