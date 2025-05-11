package com.example.medcare.views.health_advice.chat_doctor

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentChatDoctorListBinding
import com.example.medcare.models.Doctor
import com.example.medcare.models.DoctorChat
import com.example.medcare.views.health_advice.contact_doctor.DoctorAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatDoctorListFragment : BaseFragment<FragmentChatDoctorListBinding>(FragmentChatDoctorListBinding::inflate) {
    private val doctorChatAdapter by lazy {
        ChatDoctorListAdapter(::onClickDoctorChat)
    }
    override val viewModel by viewModel<ChatDoctorViewModel>()

    override fun initData() {
        viewModel.getChatList()
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindData() {
        viewModel.getDoctorChats.observe(viewLifecycleOwner){
            if (it.isNullOrEmpty()) {
                binding.txtLabelDoctorChatEmpty.visibility = View.VISIBLE
                binding.rcvDoctorList.visibility = View.GONE
            } else {
                binding.txtLabelDoctorChatEmpty.visibility = View.GONE
                binding.rcvDoctorList.visibility = View.VISIBLE
                binding.rcvDoctorList.layoutManager = LinearLayoutManager(binding.root.context)
                doctorChatAdapter.submitList(it)
                binding.rcvDoctorList.adapter = doctorChatAdapter
            }
        }
    }

    private fun onClickDoctorChat(doctorChat: DoctorChat){
        findNavController().navigate(R.id.action_chatDoctorListFragment_to_chatDoctorDetailFragment)
    }

    override fun destroy() {

    }
}