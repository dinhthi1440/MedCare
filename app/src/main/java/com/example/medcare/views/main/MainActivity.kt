package com.example.medcare.views.main

import androidx.navigation.findNavController
import com.example.medcare.R
import com.example.medcare.base.BaseActivity
import com.example.medcare.databinding.ActivityMainBinding
import androidx.navigation.ui.NavigationUI.setupWithNavController

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val navController by lazy { findNavController(R.id.nav_host_fragment_activity_main) }
    override fun initData() {

    }

    override fun handleData() {

    }

    override fun bindData() {

    }
}