package com.example.medcare.views.connect_relatives.connect_relatives_list

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentConnectRelativesBinding
import com.example.medcare.extension.addNoteInRelativeAdd
import com.example.medcare.extension.confirmEvent
import com.example.medcare.extension.selectCustomDate
import com.example.medcare.models.Relative
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel

class ConnectRelativesFragment : BaseFragment<FragmentConnectRelativesBinding>(FragmentConnectRelativesBinding::inflate) {
    override val viewModel by viewModel<ConnectRelativesViewModel>()
    private val relativesAdapter by lazy {
        RelativeAdapter(::onView, ::onCreateReminder, ::onRemove)
    }
    private val addRelativeAdapter by lazy {
        AddRelativeAdapter(::onClickRequest, ::onAddRelative)
    }
    override fun initData() {
        viewModel.getRelatives()
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.cvRequestReminder.setOnClickListener {
            findNavController().navigate(R.id.action_connectRelativesFragment_to_relativeRequestAddFragment)
        }
    }

    override fun bindData() {
        viewModel.getRelativeList.observe(viewLifecycleOwner) {
            binding.rcvRelativeList.layoutManager = LinearLayoutManager(binding.root.context)
            relativesAdapter.submitList(it)
            binding.rcvRelativeList.adapter = relativesAdapter
        }

        viewModel.getAddRequestList.observe(viewLifecycleOwner) {
            binding.rcvRequestAddFriend.layoutManager = LinearLayoutManager(binding.root.context)
            addRelativeAdapter.submitList(it)
            binding.rcvRequestAddFriend.adapter = addRelativeAdapter
        }
    }
    private fun onView(relative: Relative) {
        findNavController().navigate(R.id.action_connectRelativesFragment_to_relativeReminderHistoryFragment)
    }

    private fun onCreateReminder(idRelative: String) {

    }

    private fun onRemove(relative: Relative) {
        dialog(requireContext()).confirmEvent("Xác nhận xoá", "Bạn có chắc chắn muốn xoá người này khỏi kết nối?") {
//            if (viewModel.getReminder.value != null) {
//                viewModel.deleteReminder(viewModel.getReminder.value!!, requireContext())
//            }
        }
    }

    private fun onClickRequest(relative: Relative) {

    }

    private fun onAddRelative(userID: String) {
        dialog(requireContext()).addNoteInRelativeAdd{

        }
    }

    override fun destroy() {

    }
}