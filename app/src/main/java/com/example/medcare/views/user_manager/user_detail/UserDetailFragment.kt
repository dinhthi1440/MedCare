package com.example.medcare.views.user_manager.user_detail

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentUserDetailBinding
import com.example.medcare.extension.changeRule
import com.example.medcare.extension.confirmEvent
import com.example.medcare.extension.confirmPassAdmin
import com.example.medcare.extension.getData
import com.example.medcare.extension.showImage
import com.example.medcare.models.Account
import com.example.medcare.utils.Constants
import com.example.medcare.views.user_manager.user_list.UserListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserDetailFragment :
    BaseFragment<FragmentUserDetailBinding>(FragmentUserDetailBinding::inflate) {
    private lateinit var accountID: String
    private lateinit var accountName: String
    private var initRule = ""
    private var urlImage = ""
    override val viewModel by viewModel<UserListViewModel>()

    override fun initData() {
        accountID = arguments?.getString("accountID") ?: ""
        accountName = arguments?.getString("accountName") ?: ""
        viewModel.getUserByID(accountID)
    }

    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                backScreenReset()
            }
            btnDelete.setOnClickListener {
                if (viewModel.getAccountDetail.value != null) {
                    dialog(requireContext()).confirmPassAdmin { password ->
                        val email = sharedPreferences.getData(Constants.SHARED_EMAIL)
                        viewModel.deleteUser(accountID, email, password)
                    }
                }
            }
            btnLock.setOnClickListener {
                dialog(requireContext()).confirmEvent(
                    "Xác nhận khóa",
                    "Bạn có chắc chắn muốn khóa tài khoản này?"
                ) {
                    val updates = mapOf(
                        "status" to if (viewModel.getAccountDetail.value?.status == "active") "locked" else "active",
                    )
                    viewModel.updateUser(accountID, updates)
                }
            }
            btnEditRule.setOnClickListener {
                dialog(requireContext()).changeRule(initRule) {
                    val updates = mapOf(
                        "rule" to it,
                    )
                    viewModel.updateUser(accountID, updates)
                }
            }
            imgAvtUser.setOnClickListener {
                if (urlImage.isNotBlank()) {
                    dialog(requireContext()).showImage(urlImage)
                }
            }
        }
    }

    override fun bindData() {
        binding.txtLabel.text = accountName
        binding.apply {
            viewModel.getAccountDetail.observe(viewLifecycleOwner) { account ->
                initRule = account.rule
                linearLayout10.visibility = View.VISIBLE
                nestedScrollView3.visibility = View.VISIBLE
                txtUserDetailEmpty.visibility = View.GONE
                txtUserName.text = account.fullName
                if (account.avatar.isNotBlank()) {
                    Glide.with(root.context).load(account.avatar).into(imgAvtUser)
                    urlImage = account.avatar
                }
                txtEmail.text = account.email
                txtRule.text =
                    when (account.rule) {
                        "user" -> "Người dùng"
                        "doctor" -> "Bác sĩ"
                        else -> "Quản trị viên"
                    }
                if (account.status == "active") {
                    txtStatus.text = "Đang hoạt động"
                    imgStatusActive.visibility = View.VISIBLE
                    imgStatusLock.visibility = View.GONE
                    btnLock.text = "Khóa"
                } else {
                    txtStatus.text = "Đã khóa"
                    txtStatus.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ccRedText
                        )
                    )
                    imgStatusActive.visibility = View.GONE
                    imgStatusLock.visibility = View.VISIBLE
                    btnLock.text = "Mở khóa"
                }
            }
            viewModel.getUpdateStatus.observe(viewLifecycleOwner) {
                isBackReset = true
                viewModel.getUserByID(accountID)
            }
            viewModel.getDeleteStatus.observe(viewLifecycleOwner) {
                if (it == "Success") {
                    Toast.makeText(context, "Đã xóa người dùng thành công", Toast.LENGTH_SHORT)
                        .show()
                    isBackReset = true
                    backScreenReset()
                }
            }
            viewModel.messageError.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                //btnDelete.visibility = View.INVISIBLE
                txtUserDetailEmpty.text = it
            }
        }
    }

    override fun destroy() {

    }
}