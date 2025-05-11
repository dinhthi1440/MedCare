package com.example.medcare.views.health_advice.chat_doctor_detail

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentChatDoctorDetailBinding
import com.example.medcare.views.health_advice.chat_doctor.ChatDoctorListAdapter
import com.example.medcare.views.health_advice.chat_doctor.ChatDoctorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatDoctorDetailFragment : BaseFragment<FragmentChatDoctorDetailBinding>(FragmentChatDoctorDetailBinding::inflate) {
    private val chatMessageAdapter by lazy {
        ChatListAdapter("userA")
    }
    override val viewModel by viewModel<ChatDoctorViewModel>()

    override fun initData() {
        viewModel.getChatDetail()
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindData() {
        viewModel.getChatDetail.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.txtLabelDoctorChatEmpty.visibility = View.VISIBLE
                binding.rcvDoctorList.visibility = View.GONE
            } else {
                binding.txtLabelDoctorChatEmpty.visibility = View.GONE
                binding.rcvDoctorList.visibility = View.VISIBLE
                binding.rcvDoctorList.layoutManager = LinearLayoutManager(binding.root.context)
                chatMessageAdapter.submitList(it)
                binding.rcvDoctorList.adapter = chatMessageAdapter
            }
        }
    }

    override fun destroy() {

    }
}