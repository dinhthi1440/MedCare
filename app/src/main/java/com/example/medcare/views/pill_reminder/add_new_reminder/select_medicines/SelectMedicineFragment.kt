package com.example.medcare.views.pill_reminder.add_new_reminder.select_medicines

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSelectMedicineBinding
import com.example.medcare.models.Medicine
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
    private var relativeID = ""
    private lateinit var listInitialSelected: MutableSet<Medicine>

    override fun initData() {
        val receivedList = arguments?.getParcelableArrayList<Medicine>("selected_medicine") ?: emptyList()
        relativeID = arguments?.getString("relative_id") ?: ""
        listInitialSelected = receivedList.toMutableSet()
        if (relativeID == "") {
            viewModel.getMedicineList(uid)
        } else {
            viewModel.getMedicineList(relativeID)
        }
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
        viewModel.getMedicines.observe(viewLifecycleOwner) { it ->
            when (it.statusCode) {
                200 -> {
                    val medicines = it.data as? List<Medicine>
                    binding.txtEmptyList.visibility = View.GONE
                    binding.rcvSelectMedicine.visibility = View.VISIBLE
                    binding.rcvSelectMedicine.layoutManager = LinearLayoutManager(binding.root.context)
                    selectMedicineAdapter.submitList(medicines)
                    binding.rcvSelectMedicine.adapter = selectMedicineAdapter
                }
                204, 500 -> {
                    binding.txtEmptyList.visibility = View.VISIBLE
                    binding.txtEmptyList.text = it.message
                    binding.rcvSelectMedicine.visibility = View.GONE
                }
            }
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