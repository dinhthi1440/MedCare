package com.example.medcare.views.auth.onboarding_infor

import android.app.AlertDialog
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.medcare.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentOnBoardingInforBinding
import com.example.medcare.extension.getData
import com.example.medcare.extension.saveData
import com.example.medcare.models.Account
import com.example.medcare.utils.Constants
import com.example.medcare.views.auth.AuthViewModel
import java.io.File

class OnBoardingInforFragment : BaseFragment<FragmentOnBoardingInforBinding>(FragmentOnBoardingInforBinding::inflate) {
    private var image = ""
    private var tempImageUri: Uri? = null
    private lateinit var account: Account
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
//                binding.cardView2.visibility = View.GONE
//                binding.cardview.visibility = View.VISIBLE
                binding.imgAvtUser.setImageURI(it)
                image = it.toString()
            }
        }
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && tempImageUri != null) {
//                binding.cardView2.visibility = View.GONE
//                binding.cardview.visibility = View.VISIBLE
                binding.imgAvtUser.setImageURI(tempImageUri)
                image = tempImageUri.toString()
            }
        }
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
    override val viewModel by viewModel<AuthViewModel>()

    override fun initData() {

    }

    override fun handleEvent() {
        binding.apply {
            root.setOnClickListener { hideKeyboard(it) }
            btnContinue.setOnClickListener {
                val selectedId = radioGroupRole.checkedRadioButtonId
                val role = when (selectedId) {
                    R.id.radioUser -> "user"
                    R.id.radioDoctor -> "doctor"
                    else -> "unknown"
                }
                val fullName = edtUserName.text.toString()

                if (fullName.isEmpty()) {
                    edtUserName.error = "Vui lòng nhập họ tên"
                    edtUserName.requestFocus()
                    return@setOnClickListener
                }
                hideKeyboard(it)
                if (role.isEmpty()) {
                    Toast.makeText(requireContext(), "Vui lòng chọn vai trò", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val avatar = ""
                val userID = sharedPreferences.getData(Constants.SHARED_USER_ID)
                val email = sharedPreferences.getData(Constants.SHARED_EMAIL)
                account = Account(userID, fullName, "", email, avatar, role, "active")
                viewModel.createUserData(account)

            }
            imgAvtUser.setOnClickListener {
                showImagePickerOptions()
            }
        }
    }

    override fun bindData() {
        viewModel.getCreateUserStatus.observe(viewLifecycleOwner) {
            if (it.statusCode == 200 ){
                val json = gson.toJson(account)
                sharedPreferences.saveData(json, Constants.SHARED_USER)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_onBoardingInforFragment_to_homeFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build()
                )
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun destroy() {

    }
}