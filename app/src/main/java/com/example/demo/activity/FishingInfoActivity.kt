package com.example.demo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demo.Cordinadora
import com.example.demo.R
import com.example.demo.fragment.info.DescriptionFishingTypeFragment

class FishingInfoActivity : AppCompatActivity(), Cordinadora {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fishing_info)
    }

    override fun onChangeFishingType(index: Int) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_fishing_description2) as DescriptionFishingTypeFragment
        fragment.changeFishingType(index)

    }
}