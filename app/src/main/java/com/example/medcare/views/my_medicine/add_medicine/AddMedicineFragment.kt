package com.example.medcare.views.my_medicine.add_medicine

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentAddMedicineBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.medcare.R
import com.example.medcare.models.Medicine
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddMedicineFragment :
    BaseFragment<FragmentAddMedicineBinding>(FragmentAddMedicineBinding::inflate) {
    override val viewModel by viewModel<AddMedicineViewModel>()
    private var image = ""
    private var unit = "viên"
    private var medicine: Medicine? = null
    private lateinit var newMedicine : Medicine
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.cardView2.visibility = View.GONE
                binding.cardview.visibility = View.VISIBLE
                binding.imgMedicine.setImageURI(it)
                image = it.toString()
            }
        }
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && tempImageUri != null) {
                binding.cardView2.visibility = View.GONE
                binding.cardview.visibility = View.VISIBLE
                binding.imgMedicine.setImageURI(tempImageUri)
                image = tempImageUri.toString()
            }
        }

    private fun showImagePickerOptions() {
        val options = arrayOf("Chụp ảnh mới", "Chọn từ thư viện")
        AlertDialog.Builder(context)
            .setTitle("Chọn ảnh")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> launchCamera()
                    1 -> pickImageLauncher.launch("image/*")
                }
            }
            .show()
    }

    private var tempImageUri: Uri? = null

    private fun launchCamera() {
        val imageFile = File.createTempFile("IMG_", ".jpg", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        tempImageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
        takePictureLauncher.launch(tempImageUri)
    }

    override fun initData() {
        medicine = arguments?.getSerializable("medicine") as? Medicine
    }

    override fun handleEvent() {
        binding.apply {
            imgCamera.setOnClickListener {
                showImagePickerOptions()
            }
            txtChangeImage.setOnClickListener {
                showImagePickerOptions()
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAdd.setOnClickListener {
                addMedicine(false)
            }
            btnSuccess.setOnClickListener {
                addMedicine(true)
            }
            spinnerUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @SuppressLint("SetTextI18n")
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedUnit = parent.getItemAtPosition(position).toString()
                    unit = selectedUnit
                    tvOnce.text = "$selectedUnit / lần"
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
            val expirationText = binding.tietExpirationDate.text?.toString()?.trim()
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.isLenient = false

            if (!expirationText.isNullOrEmpty()) {
                try {
                    val date = dateFormat.parse(expirationText)
                    if (date != null) {
                        calendar.time = date
                    }
                } catch (e: ParseException) {}
            }

            imgCalendarIcon.setOnClickListener {
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val selectedDate = "%02d/%02d/%04d".format(
                            selectedDayOfMonth,
                            selectedMonth + 1,
                            selectedYear
                        )
                        tietExpirationDate.setText(selectedDate)
                    },
                    year, month, day
                )

                datePickerDialog.show()
            }
        }
    }

    private fun addMedicine(isEdit: Boolean) {

        val id = if (isEdit) medicine!!.id else UUID.randomUUID().toString()
        val medicineName = binding.tietMedicineName.text?.toString()?.trim().orEmpty()
        val expirationDate = binding.tietExpirationDate.text?.toString()?.trim().orEmpty()
        val note = binding.tietNote.text?.toString()?.trim().orEmpty()

        val quantity = binding.tietQuantity.text?.toString()?.toIntOrNull()
        val dosage = binding.tietDosage.text?.toString()?.toIntOrNull()

        if (medicineName.isEmpty() || expirationDate.isEmpty() || quantity == null || dosage == null) {
            Toast.makeText(
                requireContext(),
                "Vui lòng nhập đầy đủ thông tin hợp lệ",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (quantity <= 0 || dosage <= 0) {
            Toast.makeText(
                requireContext(),
                "Số lượng và liều lượng phải lớn hơn 0",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (quantity < dosage) {
            Toast.makeText(
                requireContext(),
                "Số lượng phải nhiều hơn hoặc bằng liều lượng",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        newMedicine = Medicine(
            id = id,
            name = medicineName,
            image = image,
            expirationDate = expirationDate,
            quantity = quantity,
            dosage = dosage,
            unit = unit,
            note = note
        )
        if (isEdit) {
            viewModel.updateMedicine(uid, newMedicine)
        } else {
            viewModel.insertMedicine(uid, newMedicine)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun bindData() {
        if (medicine != null) {
            binding.apply {
                btnAdd.visibility = View.GONE
                btnSuccess.visibility = View.VISIBLE
                txtLabel.text = "Sửa thuốc"
                tietMedicineName.setText(medicine!!.name)
                tietQuantity.setText(medicine!!.quantity.toString())
                tietDosage.setText(medicine!!.dosage.toString())
                tietNote.setText(medicine!!.note)
                tietExpirationDate.setText(medicine!!.expirationDate)
                val units = resources.getStringArray(R.array.medicine_units)
                val index = units.indexOf(medicine!!.unit)
                if (index >= 0) {
                    binding.spinnerUnit.setSelection(index)
                }
                tvOnce.text = "${medicine!!.unit} / lần"
            }
        }
        viewModel.getInsertStatus.observe(viewLifecycleOwner) {
            when (it.statusCode) {
                200 -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    val result = Bundle().apply {
                        putBoolean("key_boolean", true)
                    }
                    parentFragmentManager.setFragmentResult("boolean_result_key", result)
                    findNavController().popBackStack()
                }
                500 -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun destroy() {

    }
}
