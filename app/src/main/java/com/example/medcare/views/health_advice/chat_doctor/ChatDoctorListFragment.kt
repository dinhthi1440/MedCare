package com.example.medcare.views.health_advice.chat_doctor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentChatDoctorListBinding
import com.example.medcare.models.Doctor
import com.example.medcare.models.DoctorChat
import com.example.medcare.views.health_advice.contact_doctor.ContactDoctorViewModel
import com.example.medcare.views.health_advice.contact_doctor.DoctorAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatDoctorListFragment : BaseFragment<FragmentChatDoctorListBinding>(FragmentChatDoctorListBinding::inflate) {
    private val doctorAdapter by lazy {
        DoctorAdapter(::onClickDoctor)
    }
    override val viewModel by viewModel<ChatDoctorViewModel>()

    override fun initData() {
        viewModel.getAllDoctorList()
    }


    override fun handleEvent() {
        binding.apply {
            binding.btnBack.setOnClickListener {
                backScreenReset()
            }
            tilSearch.setEndIconOnClickListener {
                val searchText = edtSearch.text.toString()
                if (searchText != "") {
                    viewModel.getSearchDoctorChatList(searchText)
                }
            }
        }
    }

    override fun bindData() {
        viewModel.getDoctors.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.rcvDoctorList.visibility = View.GONE
                binding.txtLabelDoctorChatEmpty.visibility = View.VISIBLE
            } else {
                binding.rcvDoctorList.visibility = View.VISIBLE
                binding.txtLabelDoctorChatEmpty.visibility = View.GONE
                binding.rcvDoctorList.layoutManager = LinearLayoutManager(binding.root.context)
                doctorAdapter.submitList(it)
                binding.rcvDoctorList.adapter = doctorAdapter
            }
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.txtLabelDoctorChatEmpty.text = it
        }
        listenBackScreen {
            isBackReset = true
        }
    }

    private fun onClickDoctor(doctor: Doctor){
        val bundle = Bundle().apply {
            putString("doctorID", doctor.id)
            putString("doctorName", doctor.fullName)
            putString("doctorAvatar", doctor.avatar)
        }
        findNavController().navigate(R.id.action_chatDoctorListFragment_to_chatDoctorDetailFragment, bundle)
    }

    override fun destroy() {

    }
}