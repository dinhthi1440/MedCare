package com.example.medcare.views.my_medicine.medicine_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentMyMedicineBinding
import com.example.medcare.models.Medicine
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyMedicineFragment :
    BaseFragment<FragmentMyMedicineBinding>(FragmentMyMedicineBinding::inflate) {
    private val medicineAdapter by lazy { MedicineAdapter(true, ::onclickMedicineItem) }
    override val viewModel by viewModel<MyMedicineViewModel>()

    override fun initData() {
        viewModel.getMedicineList()
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.acbAddMedicine.setOnClickListener {
            findNavController().navigate(R.id.action_myMedicineFragment_to_addMedicineFragment)
        }
    }

    override fun bindData() {
        listenBackScreen("boolean_result_key", "key_boolean") {
            viewModel.getMedicineList()
        }
        viewModel.getMedicines.observe(viewLifecycleOwner) { medicines ->
            if (medicines.isNotEmpty()){
                binding.txtEmptyList.visibility = View.GONE
                binding.rcvMedicineList.visibility = View.VISIBLE
                Log.e("TAG", "bindData: 1111 data ${medicines.first()}", )
                binding.rcvMedicineList.layoutManager = LinearLayoutManager(binding.root.context)
                medicineAdapter.submitList(medicines)
                binding.rcvMedicineList.adapter = medicineAdapter
            } else {
                binding.txtEmptyList.visibility = View.VISIBLE
                binding.rcvMedicineList.visibility = View.GONE
            }

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