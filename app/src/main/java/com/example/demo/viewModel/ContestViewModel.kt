package com.example.demo.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContestViewModel: ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    fun setTitle(title: String){
        _title.value = title
    }

    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    fun setDescription(description: String){
        _description.value = description
    }

    private val _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    fun setDate(date: String){
        _date.value = date
    }

    private val _image = MutableLiveData<Bitmap>()
    val image: LiveData<Bitmap>
        get() = _image

    fun setImage(image: Bitmap) {
        _image.value = image
    }

    private val _termsAndConditions = MutableLiveData<String>()
    val termsAndConditions: LiveData<String>
        get() = _termsAndConditions

    fun setTermsAndConditions(termsAndConditions: String) {
        _termsAndConditions.value = termsAndConditions
    }
}