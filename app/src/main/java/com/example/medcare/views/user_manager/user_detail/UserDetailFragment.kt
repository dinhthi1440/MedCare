package com.example.medcare.views.user_manager.user_detail

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentUserDetailBinding
import com.example.medcare.extension.changeRule
import com.example.medcare.extension.confirmEvent
import com.example.medcare.models.Account
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserDetailFragment : BaseFragment<FragmentUserDetailBinding>(FragmentUserDetailBinding::inflate) {
    val account = Account(
        id = "acc001",
        fullName = "Nguyễn Văn A",
        userName = "nguyenvana",
        email = "vana@example.com",
        avatar = "https://example.com/avatar/a.jpg",
        password = "hashed_pw_001",
        rule = "admin",
        status = "locked"
    )
    override val viewModel by viewModel<UserDetailViewModel>()

    override fun initData() {

    }

    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            btnDelete.setOnClickListener {
                dialog(requireContext()).confirmEvent("Xác nhận xóa", "Bạn có chắc chắn muốn xóa tài khoản này?") {

                }
            }
            btnLock.setOnClickListener {

            }
            btnEditRule.setOnClickListener {
                val initRule = if (true) "doctor" else "user"
                dialog(requireContext()).changeRule(initRule) {

                }
            }
        }
    }

    override fun bindData() {
        binding.apply {
            txtLabel.text = account.fullName
            txtUserName.text = account.userName
            txtEmail.text = account.email
            txtRule.text =
                if (account.rule == "user") "Người dùng"
                else if (account.rule == "doctor") "Bác sĩ"
                else "Quản trị viên"
            if (account.status == "active") {
                txtStatus.text = "Đang hoạt động"
                imgStatusActive.visibility = View.VISIBLE
                imgStatusLock.visibility = View.GONE
                btnLock.text = "Khóa"
            } else {
                txtStatus.text = "Đã khóa"
                txtStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.ccRedText))
                imgStatusActive.visibility = View.GONE
                imgStatusLock.visibility = View.VISIBLE
                btnLock.text = "Mở khóa"
            }
        }
    }

    override fun destroy() {

    }
}