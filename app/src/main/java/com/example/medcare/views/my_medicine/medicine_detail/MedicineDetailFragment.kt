package com.example.medcare.views.my_medicine.medicine_detail

import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentMedicineDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicineDetailFragment :
    BaseFragment<FragmentMedicineDetailBinding>(FragmentMedicineDetailBinding::inflate) {
    override val viewModel by viewModel<MedicineDetailViewModel>()
    private var medicineId = ""
    private var medicineName = ""
    private var previousScreen = ""
    override fun initData() {
        viewModel.getMedicineDetail()
        medicineId = arguments?.getString("medicine_id").toString()
        medicineName = arguments?.getString("medicine_name").toString()
        previousScreen = arguments?.getString("previous_screen").toString()
    }

    override fun handleEvent() {
        if(previousScreen =="detail"){
            binding.btnEdit.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnDelete.setOnClickListener {

        }
        binding.btnEdit.setOnClickListener {

        }
    }

    override fun bindData() {
        binding.txtLabel.text = medicineName
        viewModel.getMedicine.observe(viewLifecycleOwner) {

        }
    }

    override fun destroy() {

    }
}