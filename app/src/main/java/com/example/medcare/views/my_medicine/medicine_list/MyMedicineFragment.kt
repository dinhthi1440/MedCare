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
        viewModel.getMedicineList(uid)
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
        listenBackScreen {
            viewModel.getMedicineList(uid)
        }
        viewModel.getMedicines.observe(viewLifecycleOwner) { it ->
            when (it.statusCode) {
                200 -> {
                    val medicines = it.data as? List<Medicine>
                    binding.txtEmptyList.visibility = View.GONE
                    binding.rcvMedicineList.visibility = View.VISIBLE
                    binding.rcvMedicineList.layoutManager = LinearLayoutManager(binding.root.context)
                    medicineAdapter.submitList(medicines)
                    binding.rcvMedicineList.adapter = medicineAdapter
                }
                204, 500 -> {
                    binding.txtEmptyList.visibility = View.VISIBLE
                    binding.txtEmptyList.text = it.message
                    binding.rcvMedicineList.visibility = View.GONE
                }
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