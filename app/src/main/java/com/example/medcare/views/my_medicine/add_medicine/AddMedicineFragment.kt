package com.example.medcare.views.my_medicine.add_medicine

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentAddMedicineBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.medcare.models.Medicine
import java.io.File
import java.util.UUID

class AddMedicineFragment : BaseFragment<FragmentAddMedicineBinding>(FragmentAddMedicineBinding::inflate) {
    override val viewModel by viewModel<AddMedicineViewModel>()
    private var image = ""
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.cardView2.visibility = View.GONE
            binding.cardview.visibility = View.VISIBLE
            binding.imgMedicine.setImageURI(it)
            image = it.toString()
        }
    }
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
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
                addMedicine()
            }
        }
    }

    private fun addMedicine() {
        val id = UUID.randomUUID().toString()
        val medicineName = binding.tietMedicineName.text?.toString()?.trim() ?: ""
        val expirationDate = binding.tietExpirationDate.text?.toString()?.trim() ?: ""
        val quantity = binding.tietQuantity.text?.toString()?.trim() ?: ""
        val note = binding.tietNote.text?.toString()?.trim() ?: ""

        if (medicineName.isEmpty() || expirationDate.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

//        val quantityInt = quantity.toIntOrNull()
//        if (quantityInt == null || quantityInt <= 0) {
//            Toast.makeText(requireContext(), "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show()
//            return
//        }

        val newMedicine = Medicine(
            id = id,
            name = medicineName,
            image = image,
            expirationDate = expirationDate,
            quantity = 5,
            unit = "",
            note = note
        )

        viewModel.insertMedicine(newMedicine)
    }

    override fun bindData() {
        viewModel.getInsertStatus.observe(viewLifecycleOwner) {
            if (it) {
                val result = Bundle().apply {
                    putBoolean("key_boolean", true)
                }
                parentFragmentManager.setFragmentResult("boolean_result_key", result)
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Đã xảy ra lỗi khi thêm thuốc", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun destroy() {

    }
}
