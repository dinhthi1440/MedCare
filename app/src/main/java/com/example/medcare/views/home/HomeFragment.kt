package com.example.medcare.views.home

import android.content.Intent
import android.os.Bundle
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

    }

    override fun bindData() {
        val menuItems = MenuItem.getAllItems()

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
            MenuItem.HISTORY -> Toast.makeText(this.requireContext(), "Mở lịch sử", Toast.LENGTH_SHORT).show()
            MenuItem.FAMILY -> Toast.makeText(this.requireContext(), "Mở kết nối người thân", Toast.LENGTH_SHORT).show()
            MenuItem.CONSULT -> Toast.makeText(this.requireContext(), "Mở tư vấn sức khỏe", Toast.LENGTH_SHORT).show()
            MenuItem.APPOINTMENT -> {
                val intent = Intent(requireContext(), AlertActivity::class.java).apply {
                    putExtra("alarm_id", "3500")
                    putExtra("reminder_id", "8441b262-534a-4f1b-8e1c-0d8ee499d5eb")
                    // putExtra("alarm_message", "Uống thuốc buổi sáng")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
//                val result = Bundle().
//                findNavController().navigate(R.id.action_homeFragment_to_reminderFragment)
            }
        }
    }

    override fun destroy() {

    }
}