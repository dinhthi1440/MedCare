package com.example.medcare.views.user_manager.user_list

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentUserListManagerBinding
import com.example.medcare.models.Account
import com.example.medcare.views.health_advice.contact_doctor.DoctorAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserListManagerFragment : BaseFragment<FragmentUserListManagerBinding>(FragmentUserListManagerBinding::inflate) {
    private val accountAdapter by lazy {
        UserListAdapter(::onView, ::onLock)
    }
    override val viewModel by viewModel<UserListViewModel>()

    override fun initData() {
        viewModel.getAccountList()
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindData() {
        viewModel.getAccounts.observe(viewLifecycleOwner) {
            binding.rcvAccountList.layoutManager = LinearLayoutManager(binding.root.context)
            accountAdapter.submitList(it)
            binding.rcvAccountList.adapter = accountAdapter
        }
    }

    private fun onView(account: Account) {
        findNavController().navigate(R.id.action_userListManagerFragment_to_userDetailFragment)
    }

    private fun onLock(account: Account) {

    }

    override fun destroy() {

    }
}