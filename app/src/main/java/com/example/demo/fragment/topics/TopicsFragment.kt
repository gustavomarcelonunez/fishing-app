package com.example.demo.fragment.topics

import android.app.AlertDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.demo.R
import com.example.demo.databinding.FragmentTopicsBinding
import com.google.firebase.messaging.FirebaseMessaging

class TopicsFragment : Fragment() {
    private var _binding: FragmentTopicsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTopicsBinding.inflate(inflater, container, false)
        loadTopicSubscriptions()
        _binding!!.goBackButton2.setOnClickListener { goBack() }
        _binding!!.saveTopicsButton.setOnClickListener { saveTopics() }


        return binding.root
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    private fun saveTopics() {
        val selectedTopics = mutableListOf<String>()
        val unselectedTopics = mutableListOf<String>()
        if (binding.contestCheckBox.isChecked) {
            selectedTopics.add("Contest")
        } else {
            unselectedTopics.add("Contest")
        }
        if (binding.regulationCheckBox.isChecked) {
            selectedTopics.add("Regulation")
        } else {
            unselectedTopics.add("Regulation")
        }
        if (binding.newsCheckBox.isChecked) {
            selectedTopics.add("News")
        } else {
            unselectedTopics.add("News")
        }

        sendNotifications(selectedTopics, unselectedTopics)
        Toast.makeText(activity, "Se han guardado sus preferencias", Toast.LENGTH_LONG).show()

        goBack()
    }

    private fun sendNotifications(selectedTopics: List<String>, unselectedTopics: List<String>) {
        for (topic in selectedTopics) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("SelectedTopics", "Suscripción exitosa al tópico: $topic")
                    } else {
                        Log.i("SelectedTopics", "Error en la suscripción al tópico: $topic", task.exception)
                    }
                }
            saveTopicSubscription(topic, true)

        }
        for (topic in unselectedTopics) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("SelectedTopics", "Desuscripción exitosa al tópico: $topic")
                    } else {
                        Log.i("SelectedTopics", "Error en la desuscripción al tópico: $topic", task.exception)
                    }
                }
            saveTopicSubscription(topic, false)

        }
        Log.i("SelectedTopics", selectedTopics.toString())
    }

    private fun saveTopicSubscription(topic: String, isSubscribed: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putBoolean(topic, isSubscribed).apply()
    }

    private fun loadTopicSubscriptions() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        binding.contestCheckBox.isChecked = preferences.getBoolean("Contest", false)
        binding.regulationCheckBox.isChecked = preferences.getBoolean("Regulation", false)
        binding.newsCheckBox.isChecked = preferences.getBoolean("News", false)
    }



}