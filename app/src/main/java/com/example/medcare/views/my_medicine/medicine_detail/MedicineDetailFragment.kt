package com.example.medcare.views.my_medicine.medicine_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentMedicineDetailBinding
import com.example.medcare.extension.confirmEvent
import com.example.medcare.models.Medicine
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
    private var creator = ""
    private var ownerID = ""
    private var medicineReminder: Medicine? = null
    override fun initData() {

        medicineId = arguments?.getString("medicine_id").toString()
        medicineName = arguments?.getString("medicine_name").toString()
        previousScreen = arguments?.getString("previous_screen").toString()
        ownerID = arguments?.getString("ownerID").toString()
        creator = arguments?.getString("creator").toString()
        arguments?.getSerializable("reminder")?.let {
            medicineReminder = it as Medicine
        }
        if (previousScreen ==  "" || previousScreen == "null") {
            viewModel.getMedicineDetail(uid, medicineId)
        } else if (medicineReminder != null) {
            viewModel.setMedicine(medicineReminder!!)
        } else {
            if (ownerID != "" && ownerID != "null") {
                viewModel.getMedicineDetail(ownerID, medicineId)
            } else {
                viewModel.getMedicineDetail(uid, medicineId)
            }
        }
    }

    override fun handleEvent() {
        listenBackScreen {
            viewModel.getMedicineDetail(uid, medicineId)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnDelete.setOnClickListener {
            dialog(requireContext()).confirmEvent("Xác nhận xoá", "Bạn có chắc chắn muốn xoá?") {
                viewModel.deleteMedicineRemote(uid, medicineId)
            }
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
                if(previousScreen =="detail"){
                    btnEdit.visibility = View.GONE
                    btnDelete.visibility = View.GONE
                } else {
                    btnDelete.visibility = View.VISIBLE
                    btnEdit.visibility = View.VISIBLE
                }
                if(creator != "" && creator != "null"){
                    txtCreator.text = creator
                    txtCreator.setTextColor(ContextCompat.getColor(requireContext(), R.color.ccRedText))
                }
                nestedScrollView.visibility = View.VISIBLE
                txtError.visibility = View.GONE
                txtMedicineName.text = it.name
                txtExpirationDate.text = it.expirationDate
                txtNote.text = it.note
                txtQuantity.text = "${it.quantity} ${it.unit}"
                txtDosage.text = "${it.dosage} ${it.unit} / lần"
                txtRealQuantity.text = "${it.realQuantity} ${it.unit}"
                if (it.image != "") {
                    imgMedicineNoData.visibility = View.GONE
                    imgMedicine.visibility = View.VISIBLE
                    Glide.with(requireContext())
                        .load(it.image)
                        .into(imgMedicine)
                } else {
                    imgMedicineNoData.visibility = View.VISIBLE
                    imgMedicine.visibility = View.GONE
                }

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
        viewModel.messageError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.txtError.text = it
        }
        viewModel.getDeleteStatus.observe(viewLifecycleOwner) {
            if (it.statusCode == 200) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                isBackReset = true
                backScreenReset()
            }
        }
        listenBackScreen {
            isBackReset = true
            viewModel.getMedicineDetail(uid, medicineId)
        }
    }

    override fun destroy() {

    }
}