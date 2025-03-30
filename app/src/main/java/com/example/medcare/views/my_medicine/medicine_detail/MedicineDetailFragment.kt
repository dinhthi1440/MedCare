package com.example.medcare.views.my_medicine.medicine_detail

import com.example.medcare.base.BaseFragment
import com.example.medcare.base.BaseViewModel
import com.example.medcare.databinding.FragmentMedicineDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicineDetailFragment :
    BaseFragment<FragmentMedicineDetailBinding>(FragmentMedicineDetailBinding::inflate) {
    override val viewModel by viewModel<MedicineDetailViewModel>()
    private var medicineId = ""
    private var medicineName = ""
    override fun initData() {
        viewModel.getMedicineDetail()
        medicineId = arguments?.getString("medicine_id").toString()
        medicineName = arguments?.getString("medicine_name").toString()
    }

    override fun handleEvent() {

    }

    override fun bindData() {
        binding.txtLabel.text = medicineName
        viewModel.getMedicine.observe(viewLifecycleOwner) {

        }
    }

    override fun destroy() {

    }
}