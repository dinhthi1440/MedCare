package com.example.medcare.views.add_new_reminder.select_medicines

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSelectMedicineBinding
import com.example.medcare.models.Medicine
import com.example.medcare.views.add_new_reminder.add_frequency.DateCustom
import com.example.medcare.views.add_new_reminder.add_frequency.FrequencyAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectMedicineFragment :
    BaseFragment<FragmentSelectMedicineBinding>(FragmentSelectMedicineBinding::inflate) {
    override val viewModel by viewModel<SelectMedicineViewModel>()
    private val selectMedicineAdapter by lazy {
        SelectMedicineAdapter(
            listInitialSelected,
            ::onSelectMedicine,
            ::onUnSelectMedicine
        )
    }
    private lateinit var listInitialSelected: MutableSet<Medicine>

    override fun initData() {
        val receivedList = arguments?.getParcelableArrayList<Medicine>("selected_medicine") ?: emptyList()
        listInitialSelected = receivedList.toMutableSet()
        viewModel.getMedicineList()
        viewModel.initData(listInitialSelected)
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnConfirm.setOnClickListener {
            val selectedList = ArrayList(viewModel.getSelectedMedicine.value ?: emptyList())
            val result = Bundle().apply {
                putParcelableArrayList("selected_medicine", selectedList)
            }
            parentFragmentManager.setFragmentResult("selected_medicine_back", result)
            findNavController().popBackStack()
        }
    }

    override fun bindData() {
        viewModel.getMedicines.observe(viewLifecycleOwner) {
            binding.rcvSelectMedicine.layoutManager = LinearLayoutManager(binding.root.context)
            selectMedicineAdapter.submitList(it)
            binding.rcvSelectMedicine.adapter = selectMedicineAdapter
        }
    }

    private fun onSelectMedicine(medicine: Medicine) {
        viewModel.selectMedicine(medicine)
    }

    private fun onUnSelectMedicine(medicine: Medicine) {
        viewModel.unSelectMedicine(medicine)
    }

    override fun destroy() {

    }
}