package com.example.medcare.views.health_advice.contact_doctor

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentContactDoctorBinding
import com.example.medcare.models.Doctor
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactDoctorFragment : BaseFragment<FragmentContactDoctorBinding>(FragmentContactDoctorBinding::inflate) {
    private val doctorAdapter by lazy {
        DoctorAdapter(::onClickDoctor)
    }
    override val viewModel by viewModel<ContactDoctorViewModel>()

    override fun initData() {
        viewModel.getDoctorList()
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.cvDoctorChatLiist.setOnClickListener {
            findNavController().navigate(R.id.action_contactDoctorFragment_to_chatDoctorListFragment)
        }
    }

    override fun bindData() {
        viewModel.getDoctors.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {

            } else {
                binding.rcvDoctorList.layoutManager = LinearLayoutManager(binding.root.context)
                doctorAdapter.submitList(it)
                binding.rcvDoctorList.adapter = doctorAdapter
            }
        }
    }

    private fun onClickDoctor(doctor: Doctor){
        findNavController().navigate(R.id.action_contactDoctorFragment_to_chatDoctorDetailFragment)
    }
    override fun destroy() {

    }
}