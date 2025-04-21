package com.example.medcare.views.add_new_reminder.add_frequency

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentAddFrequencyBinding
import com.example.medcare.extension.selectCustomDate
import com.example.medcare.views.add_new_reminder.NewReminderViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFrequencyFragment() : BaseFragment<FragmentAddFrequencyBinding>(FragmentAddFrequencyBinding::inflate) {
    override val viewModel by viewModel<NewReminderViewModel>()
    private val frequencyAdapter by lazy { FrequencyAdapter(dayItems, ::selectFrequency, ::selectCustom) }
    private var listInitialSelected: MutableList<DateCustom> = mutableListOf(DateCustom.Monday)
    private lateinit var frequencyModel: FrequencyModel
    private lateinit var dayItems: List<FrequencyModel>

    override fun initData() {
        frequencyModel = arguments?.getSerializable("frequency_model_to") as? FrequencyModel
            ?: FrequencyModel(1, "Hôm nay", true)
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            val result = Bundle().apply {
                putSerializable("frequency_model", viewModel.frequencySelected)
            }
            parentFragmentManager.setFragmentResult("frequency_result_key", result)
            findNavController().popBackStack()
        }
    }

    override fun bindData() {
        viewModel.selectFrequency(frequencyModel)
        listInitialSelected = frequencyModel.listDateSelected?.toMutableList() ?: mutableListOf(DateCustom.Monday)
        dayItems = listOf(
            FrequencyModel(1, "Hôm nay", false),
            FrequencyModel(2, "Mỗi ngày", false),
            FrequencyModel(3, "Cách ngày", false),
            FrequencyModel(4, "Tuỳ chỉnh", false),
        ).map {
            if (it.id == frequencyModel.id || it.label == frequencyModel.label) {
                it.copy(isSelected = true, listDateSelected =  frequencyModel.listDateSelected)
            } else {
                it.copy(isSelected = false)
            }
        }
        binding.rcvListFrequency.layoutManager = LinearLayoutManager(binding.root.context)
        frequencyAdapter.submitList(dayItems)
        binding.rcvListFrequency.adapter = frequencyAdapter
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val result = Bundle().apply {
                        putSerializable("frequency_model", viewModel.frequencySelected)
                    }
                    parentFragmentManager.setFragmentResult("frequency_result_key", result)
                    findNavController().popBackStack()
                }
            }
        )
    }

    override fun destroy() {

    }

    private fun selectFrequency(frequencyModel: FrequencyModel){
        if(frequencyModel.label == "Tuỳ chỉnh"){
            frequencyModel.listDateSelected = listInitialSelected
        }
        viewModel.selectFrequency(frequencyModel)
    }
    private fun selectCustom(){
        dialog(requireContext()).selectCustomDate(listInitialSelected){
            listInitialSelected = it?.toMutableList() ?: mutableListOf()
            val frequencyModel: FrequencyModel = viewModel.frequencySelected
            frequencyModel.listDateSelected = it
            viewModel.selectFrequency(frequencyModel)
        }
    }

}