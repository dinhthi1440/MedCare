package com.example.medcare.views.home

import android.content.Intent
import android.view.View
import android.widget.Toast
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

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override val viewModel by viewModel<HomeViewModel>()
    private var account: Account? = null
    override fun initData() {
        viewModel.dataSynchronization(uid, requireContext())
    }

    override fun handleEvent() {
        binding.imgUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingListFragment)
        }
        binding.iconLogoApp.setOnClickListener {
            val alarmID = "386837"
            val reminderID = "8e15f3b1-1f46-497f-9e08-70b8d5e8db90"
            val alarmMessage = "Đến giờ uống thuốc!"
            val i = Intent(context, AlertActivity::class.java).apply {
                putExtra("alarm_id", alarmID)
                putExtra("reminder_id", reminderID)
                putExtra("alarm_message", alarmMessage)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            context?.startActivity(i)
        }
    }
    private fun bindUserData() {
        val json = sharedPreferences.getData(Constants.SHARED_USER)
        account = gson.fromJson(json, Account::class.java)
        account?.let {
            binding.txtUserName.text = it.fullName
            binding.txtUserMode.text = it.rule
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