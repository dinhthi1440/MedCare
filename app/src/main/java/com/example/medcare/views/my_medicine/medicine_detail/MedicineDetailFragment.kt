package com.example.medcare.views.my_medicine.medicine_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentMedicineDetailBinding
import com.example.medcare.extension.confirmEvent
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MedicineDetailFragment :
    BaseFragment<FragmentMedicineDetailBinding>(FragmentMedicineDetailBinding::inflate) {
    override val viewModel by viewModel<MedicineDetailViewModel>()
    private var medicineId = ""
    private var medicineName = ""
    private var previousScreen = ""
    override fun initData() {

        medicineId = arguments?.getString("medicine_id").toString()
        medicineName = arguments?.getString("medicine_name").toString()
        previousScreen = arguments?.getString("previous_screen").toString()
        viewModel.getMedicineDetail(medicineId)
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
            val bundle = Bundle().apply {
                putSerializable("medicine", viewModel.getMedicine.value)
            }
            findNavController().navigate(R.id.action_medicineDetailFragment_to_addMedicineFragment, bundle)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindData() {
        binding.txtLabel.text = medicineName
        viewModel.getMedicine.observe(viewLifecycleOwner) {
            binding.apply {
                txtMedicineName.text = it.name
                txtExpirationDate.text = it.expirationDate
                txtNote.text = it.note
                txtQuantity.text = "${it.quantity} ${it.unit}"
                txtDosage.text = "${it.dosage} ${it.unit} / lần"
                txtRealQuantity.text = "${it.realQuantity} ${it.unit}"
                Glide.with(requireContext())
                    .load(it.image)
                    .into(imgMedicine)
                if (it.quantity < 5 || it.quantity < it.dosage) {
                    txtQuantity.setTextColor(ContextCompat.getColor(requireContext(), R.color.ccRedText))
                }

                if (it.realQuantity < 5 || it.realQuantity < it.dosage) {
                    txtRealQuantity.setTextColor(ContextCompat.getColor(requireContext(), R.color.ccRedText))
                }
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                try {
                    val expiryDate = LocalDate.parse(it.expirationDate, formatter)
                    val today = LocalDate.now()
                    if (!expiryDate.isBefore(today) && ChronoUnit.DAYS.between(today, expiryDate) <= 5) {
                        txtExpirationDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.ccRedText))
                    }
                } catch (e: Exception) {
                    txtExpirationDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.ccRedText))
                    txtExpirationDate.text = "Ngày không hợp lệ"
                }
            }
        }
    }

    override fun destroy() {

    }
}