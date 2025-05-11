package com.example.medcare.views.connect_relatives.relative_request_add

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentRelativeRequestAddBinding
import com.example.medcare.models.ReminderRelative
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class RelativeRequestAddFragment : BaseFragment<FragmentRelativeRequestAddBinding>(FragmentRelativeRequestAddBinding::inflate) {

    private val beReminderAdapter by lazy {
        RelativeRequestAdapter(::onClickReminder)
    }
    private val reminderToAdapter by lazy {
        RelativeRequestAdapter(::onClickReminder)
    }
    override val viewModel by viewModel<RelativeRequestViewModel>()

    override fun initData() {
        viewModel.getData()
    }

    override fun handleEvent() {
        binding.apply {
            txtTabBeReminder.setOnClickListener {
                rcvBeReminder.visibility = View.VISIBLE
                rcvReminderTo.visibility = View.GONE
                txtTabBeReminder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.ccBlueBtnPrimary))
                txtTabReminderTo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            txtTabReminderTo.setOnClickListener {
                rcvBeReminder.visibility = View.GONE
                rcvReminderTo.visibility = View.VISIBLE
                txtTabBeReminder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                txtTabReminderTo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.ccBlueBtnPrimary))
            }
            btnBack.setOnClickListener { findNavController().popBackStack() }
        }

    }

    override fun bindData() {
        binding.apply {
            rcvBeReminder.visibility = View.VISIBLE
            rcvReminderTo.visibility = View.GONE
            txtTabBeReminder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.ccBlueBtnPrimary))
            txtTabReminderTo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            viewModel.getReminderTo.observe(viewLifecycleOwner){
                binding.rcvReminderTo.layoutManager = LinearLayoutManager(binding.root.context)
                reminderToAdapter.submitList(it)
                binding.rcvReminderTo.adapter = reminderToAdapter
            }
            viewModel.getReminderFrom.observe(viewLifecycleOwner){
                binding.rcvBeReminder.layoutManager = LinearLayoutManager(binding.root.context)
                beReminderAdapter.submitList(it)
                binding.rcvBeReminder.adapter = beReminderAdapter
            }
        }

    }

    private fun onClickReminder(reminderRelative: ReminderRelative){
        findNavController().navigate(R.id.action_relativeRequestAddFragment_to_relativeReminderDetailFragment)
    }

    override fun destroy() {

    }
}