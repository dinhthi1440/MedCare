package com.example.medcare.views.user_manager.user_list

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentUserListManagerBinding
import com.example.medcare.extension.confirmEvent
import com.example.medcare.models.Account
import com.example.medcare.utils.TimeUtils
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserListManagerFragment : BaseFragment<FragmentUserListManagerBinding>(FragmentUserListManagerBinding::inflate) {
    private val accountAdapter by lazy {
        UserListAdapter(::onView, ::onLock)
    }
    private var searchString = ""
    override val viewModel by viewModel<UserListViewModel>()

    override fun initData() {
        viewModel.getAccountList(searchString)
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tilSearch.setEndIconOnClickListener {
            val searchText = binding.edtSearch.text.toString()
            searchString = searchText
            viewModel.getAccountList(searchString)
            hideKeyboard(it)
        }
        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                val searchText = binding.edtSearch.text.toString()
                searchString = searchText
                viewModel.getAccountList(searchString)
                hideKeyboard(v)
                true
            } else {
                false
            }
        }
    }

    override fun bindData() {
        binding.apply {
            viewModel.getAccounts.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    rcvAccountList.visibility = View.VISIBLE
                    txtUserListEmpty.visibility = View.GONE
                    rcvAccountList.layoutManager = LinearLayoutManager(binding.root.context)
                    accountAdapter.submitList(it)
                    rcvAccountList.adapter = accountAdapter
                } else {
                    rcvAccountList.visibility = View.GONE
                    txtUserListEmpty.visibility = View.VISIBLE
                }
            }
            viewModel.messageError.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                txtUserListEmpty.text = it
            }


        }
        listenBackScreen {
            viewModel.getAccountList(searchString)
        }
        viewModel.getUpdateStatus.observe(viewLifecycleOwner) {
            isBackReset = true
            viewModel.getAccountList(searchString)
        }
    }

    private fun onView(account: Account) {
        val bundle = Bundle().apply {
            putString("accountID", account.id)
            putString("accountName", account.fullName)
        }
        findNavController().navigate(R.id.action_userListManagerFragment_to_userDetailFragment, bundle)
    }

    private fun onLock(account: Account) {
        dialog(requireContext()).confirmEvent("Xác nhận khóa", "Bạn có chắc chắn muốn khóa tài khoản này?") {
            val updates = mapOf(
                "status" to if (account.status=="active") "locked" else "active",
                "updateAt" to TimeUtils.getCurrentCreatedAt()
            )
            viewModel.updateUser(account.id, updates)
        }
    }

    override fun destroy() {

    }
}