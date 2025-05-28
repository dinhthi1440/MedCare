package com.example.medcare.views.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentHomeBinding
import com.example.medcare.extension.getData
import com.example.medcare.models.Account
import com.example.medcare.utils.Constants
import com.example.medcare.views.home.model.MenuItem
import com.example.medcare.views.main.AlertActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.coroutineContext

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override val viewModel by viewModel<HomeViewModel>()
    private var account: Account? = null
    override fun initData() {

    }

    override fun handleEvent() {
        binding.imgUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingListFragment)
        }
        binding.iconLogoApp.setOnClickListener {

        }
    }
    private fun bindUserData() {
        val json = sharedPreferences.getData(Constants.SHARED_USER)
        account = gson.fromJson(json, Account::class.java)
        account?.let {
            binding.txtUserName.text = it.fullName ?: ""
            binding.txtUserMode.text = it.rule ?: ""
        }
        if (account?.avatar != "") {
            Glide.with(requireContext())
                .load(account?.avatar)
                .into(binding.imgUser)
        }
    }


    override fun bindData() {
        bindUserData()
        var menuItems = listOf<MenuItem>()
        if (account?.rule == "admin") {
            menuItems = MenuItem.getAdminItems()
            binding.txtTitle.text = "Xin chào, bạn đang ở chế độ quản trị viên!"
            binding.constraintLayout2.visibility = View.GONE
            binding.txtUserMode.text = "Quản trị viên"
        } else if (account?.rule == "doctor") {
            menuItems = MenuItem.getUserItems()
            binding.txtUserMode.text = "Bác sĩ"
        } else {
            menuItems = MenuItem.getUserItems()
            binding.txtUserMode.text = "Người dùng"
        }


        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        recyclerView.adapter = MenuAdapter(menuItems) {
            navigateScreen(it)
        }
    }

    private fun navigateScreen(item: MenuItem){

        when (item) {
            MenuItem.MEDICINE -> {
                findNavController().navigate(R.id.action_homeFragment_to_myMedicineFragment)
            }
            MenuItem.REMINDER -> {
                findNavController().navigate(R.id.action_homeFragment_to_medicationReminderFragment)
            }
            MenuItem.HISTORY -> {
                findNavController().navigate(R.id.action_homeFragment_to_reminderHistoryFragment)
            }
            MenuItem.FAMILY -> {
                findNavController().navigate(R.id.action_homeFragment_to_connectRelativesFragment)
            }
            MenuItem.CONSULT -> {
                findNavController().navigate(R.id.action_homeFragment_to_contactDoctorFragment)
            }
            MenuItem.APPOINTMENT -> Toast.makeText(this.requireContext(), "Chức năng này chưa được phát triển", Toast.LENGTH_SHORT).show()
            MenuItem.USER_MANAGEMENT -> {
                findNavController().navigate(R.id.action_homeFragment_to_userListManagerFragment)
            }
            MenuItem.FEEDBACK_MANAGEMENT -> {
                findNavController().navigate(R.id.action_homeFragment_to_feedbackListFragment)
            }
        }
    }


    override fun destroy() {

    }
}