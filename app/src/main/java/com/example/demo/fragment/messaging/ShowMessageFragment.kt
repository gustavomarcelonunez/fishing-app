package com.example.demo.fragment.messaging

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demo.databinding.FragmentShowMessageBinding

class ShowMessageFragment : Fragment() {
    private var _binding: FragmentShowMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val notificationTitle = arguments?.getString("notificationTitle")
        val notificationBody = arguments?.getString("notificationBody")

        _binding = FragmentShowMessageBinding.inflate(inflater, container, false)
        _binding!!.textViewNotificationTitle.text = notificationTitle
        _binding!!.textViewBody1.text = notificationBody

        return binding.root
    }

}