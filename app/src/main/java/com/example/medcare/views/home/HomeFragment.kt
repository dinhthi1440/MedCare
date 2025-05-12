package com.example.medcare.views.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentHomeBinding
import com.example.medcare.views.home.model.MenuItem
import com.example.medcare.views.main.AlertActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    override val viewModel by viewModel<HomeViewModel>()

    override fun initData() {

    }

    override fun handleEvent() {
        binding.imgUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingListFragment)
        }
    }

    override fun bindData() {
        val isAdmin = true
        var menuItems = listOf<MenuItem>()
        if (isAdmin) {
            menuItems = MenuItem.getAdminItems()
            binding.txtTitle.text = "Xin chào, bạn đang ở chế độ quản trị viên!"
            binding.constraintLayout2.visibility = View.GONE
            binding.txtUserMode.text = "Quản trị viên"
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