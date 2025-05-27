package com.example.medcare.views.health_advice.contact_doctor

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentContactDoctorBinding
import com.example.medcare.models.DoctorChat
import com.example.medcare.views.health_advice.chat_doctor.ChatDoctorListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactDoctorFragment : BaseFragment<FragmentContactDoctorBinding>(FragmentContactDoctorBinding::inflate) {

    private val doctorChatAdapter by lazy {
        ChatDoctorListAdapter(uid, ::onClickDoctorChat)
    }
    override val viewModel by viewModel<ContactDoctorViewModel>()

    override fun initData() {
        viewModel.getChatList(uid)
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.edtSearch.setOnClickListener {
            findNavController().navigate(R.id.action_contactDoctorFragment_to_chatDoctorListFragment)
        }
    }

    override fun bindData() {
        viewModel.getDoctorChats.observe(viewLifecycleOwner){
            binding.txtLabelDoctorChatEmpty.visibility = View.GONE
            binding.rcvDoctorList.visibility = View.VISIBLE
            binding.rcvDoctorList.layoutManager = LinearLayoutManager(binding.root.context)
            doctorChatAdapter.submitList(it)
            binding.rcvDoctorList.adapter = doctorChatAdapter
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.txtLabelDoctorChatEmpty.text = it
        }

    }

    private fun onClickDoctorChat(doctorChat: DoctorChat) {
        val isDoctor = doctorChat.doctorID == uid

        val bundle = Bundle().apply {
            putString("doctorID", doctorChat.id)
            putString("doctorName", if (isDoctor) doctorChat.patientName else doctorChat.doctorName)
            putString("doctorAvatar", if (isDoctor) doctorChat.patientAvatar else doctorChat.doctorAvatar)
        }

        findNavController().navigate(
            R.id.action_contactDoctorFragment_to_chatDoctorDetailFragment,
            bundle
        )
    }

    override fun destroy() {

    }
}