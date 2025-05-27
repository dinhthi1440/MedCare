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
                findNavController().popBackStack()
            }
            binding.edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    // Sau khi text đã thay đổi
//                    val text = s.toString()
//                    if (text == "") {
//                        nestedScrollView3.visibility = View.VISIBLE
//                        rcvSearchList.visibility = View.GONE
//                    } else {
//                        nestedScrollView3.visibility = View.GONE
//                        rcvSearchList.visibility = View.VISIBLE
//                        if (isInputting){
//
//                        } else {
//                            viewModel.getSearchRelatives(text)
//                            isInputting = true
//                        }
//                    }
                }
            })
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