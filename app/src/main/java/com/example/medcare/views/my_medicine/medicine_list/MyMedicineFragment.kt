package com.example.medcare.views.my_medicine.medicine_list

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentMyMedicineBinding
import com.example.medcare.views.my_medicine.medicine_list.model.Medicine
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyMedicineFragment :
    BaseFragment<FragmentMyMedicineBinding>(FragmentMyMedicineBinding::inflate) {
    private val medicineAdapter by lazy { MedicineAdapter(::onclickMedicineItem) }
    override val viewModel by viewModel<MyMedicineViewModel>()

    override fun initData() {
        viewModel.getMedicineList()
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindData() {
        viewModel.getMedicines.observe(viewLifecycleOwner) { medicines ->
            binding.rcvMedicineList.layoutManager = LinearLayoutManager(binding.root.context)
            medicineAdapter.submitList(medicines)
            binding.rcvMedicineList.adapter = medicineAdapter
        }
    }

    private fun onclickMedicineItem(medicine: Medicine) {
        val bundle = Bundle().apply {
            putString("medicine_id", medicine.id)
            putString("medicine_name", medicine.name)
        }
        findNavController().navigate(R.id.action_myMedicineFragment_to_medicineDetailFragment, bundle)
    }

    override fun destroy() {

    }

}