package com.example.medcare.views.connect_relatives.relative_request_add

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentRelativeRequestAddBinding
import com.example.medcare.extension.confirmEvent
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderRequestStatus
import org.koin.androidx.viewmodel.ext.android.viewModel


class RelativeRequestAddFragment : BaseFragment<FragmentRelativeRequestAddBinding>(FragmentRelativeRequestAddBinding::inflate) {

    private val beReminderAdapter by lazy {
        RelativeRequestAdapter(uid, ::onClickReminder,)
    }
    private val reminderToAdapter by lazy {
        RelativeRequestAdapter(uid, ::onClickReminder)
    }
    override val viewModel by viewModel<RelativeRequestViewModel>()

    override fun initData() {
        viewModel.getData(uid)
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
            viewModel.messageError.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun onClickReminder(reminderRelative: PillReminder){
        val bundle = Bundle().apply {
            putString("reminderID", reminderRelative.id)
        }
        findNavController().navigate(R.id.action_relativeRequestAddFragment_to_relativeReminderDetailFragment, bundle)
    }

    override fun destroy() {

    }
}