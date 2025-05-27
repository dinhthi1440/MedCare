package com.example.medcare.views.setting.setting_information

import android.app.AlertDialog
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSettingInformationBinding
import com.example.medcare.databinding.FragmentSettingListBinding
import com.example.medcare.extension.confirmEvent
import com.example.medcare.extension.getData
import com.example.medcare.extension.saveData
import com.example.medcare.models.Account
import com.example.medcare.utils.Constants
import com.example.medcare.utils.FileUtils
import com.example.medcare.views.setting.setting_list.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SettingInformationFragment : BaseFragment<FragmentSettingInformationBinding>(
    FragmentSettingInformationBinding::inflate) {

    override val viewModel by viewModel<SettingViewModel>()

    private var account: Account? = null
    private var image = ""
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.imgAvtUser.setImageURI(it)
                image = it.toString()
            }
        }
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && tempImageUri != null) {
                binding.imgAvtUser.setImageURI(tempImageUri)
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
        val json = sharedPreferences.getData(Constants.SHARED_USER)
        account = gson.fromJson(json, Account::class.java)

    }


    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            imgChangeAvatar.setOnClickListener {
                showImagePickerOptions()
            }
            btnAdd.setOnClickListener {
                val newName = edtUserName.text.toString().trim()
                val hasImage = image.isNotBlank()
                val selectedId = radioGroupRole.checkedRadioButtonId
                var rule = when (selectedId) {
                    R.id.radioUser -> "user"
                    R.id.radioDoctor -> "doctor"
                    else -> "unknown"
                }
                if (account?.rule == "admin") {
                    rule = "admin"
                }

                if (newName.isEmpty() && !hasImage && rule == account?.rule) {
                    Toast.makeText(requireContext(), "Vui lòng nhập tên hoặc chọn ảnh mới", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dialog(requireContext()).confirmEvent(
                    "Xác nhận cập nhật",
                    "Bạn có chắc chắn muốn cập nhật thông tin?"
                ) {
                    val file = if (hasImage) FileUtils.uriToFile(requireContext(), image.toUri()) else null

                    account?.let { acc ->
                        val updatedAccount = acc.copy(
                            fullName = newName.ifEmpty { acc.fullName },
                            rule = rule
                        )
                        viewModel.updateInformation(updatedAccount, file)
                    } ?: run {
                        Toast.makeText(requireContext(), "Không tìm thấy tài khoản để cập nhật", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }

    override fun bindData() {
        binding.apply {
            txtvUsername.text = account?.fullName ?: ""
            if (account?.avatar != "") {
                Glide.with(requireContext())
                    .load(account?.avatar)
                    .into(imgAvtUser)
            }
            if (account?.rule == "doctor") {
                radioGroupRole.check(R.id.radioDoctor)
            } else if (account?.rule == "admin") {
                radioGroupRole.visibility = View.GONE
                txtEmailLabel.text = "Bạn là admin nên không thể thay đổi quyền hạn!"
            } else {
                radioGroupRole.check(R.id.radioUser)
            }

        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.getUpdateStatus.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            backScreenReset()
        }
    }
    override fun destroy() {

    }
}