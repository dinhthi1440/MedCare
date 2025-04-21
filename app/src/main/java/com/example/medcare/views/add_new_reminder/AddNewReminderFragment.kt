package com.example.medcare.views.add_new_reminder

import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentAddNewReminderBinding
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.Medicine
import com.example.medcare.views.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.add_new_reminder.model.SelectedTime
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class AddNewReminderFragment :
    BaseFragment<FragmentAddNewReminderBinding>(FragmentAddNewReminderBinding::inflate) {
    override val viewModel by viewModel<NewReminderViewModel>()
    private val selectedTimeAdapter by lazy { SelectedTimeAdapter(::onRemoveTimeSelected) }
    private val selectedMedicineAdapter by lazy { MedicineAdapter(false, ::onClickMedicine) }
    private lateinit var alarmHelper: AlarmHelper
    private var selectedTimes = mutableListOf(
        SelectedTime(id = "1", time = "8:00", amPm = "AM"),
        SelectedTime(id = "2", time = "12:30", amPm = "PM"),
    )
    override fun initData() {
        viewModel.getData()
        alarmHelper = AlarmHelper(this.requireContext())
    }

    override fun handleEvent() {
        viewModel.getFrequencyStr.observe(viewLifecycleOwner){
            binding.txtFrequency.text = it
        }
        binding.apply {

            btnAddTime.setOnClickListener {
                val pickerH = numPickerH.value
                val pickerM = numPickerM.value
                val pickerAm = numPickerAm.value
                val amPm = if (pickerAm == 0) "AM" else "PM"

                val formattedHour = String.format("%02d", pickerH)
                val formattedMinute = String.format("%02d", pickerM)
                val time = "$formattedHour:$formattedMinute"

                val isDuplicate = selectedTimes.any {
                    it.time == time && it.amPm == amPm
                }

                if (isDuplicate) {
                    Toast.makeText(context, "Thời gian này đã được chọn!", Toast.LENGTH_SHORT).show()
                } else {
                    val newTimeSelected = SelectedTime(
                        id = UUID.randomUUID().toString(),
                        time = time,
                        amPm = amPm
                    )
                    selectedTimes.add(newTimeSelected)
                    selectedTimeAdapter.submitList(selectedTimes)
                    binding.rcvListTime.adapter = selectedTimeAdapter
                }
            }

            layoutFrequencySelect.setOnClickListener {
                val result = Bundle().apply {
                    putSerializable("frequency_model_to", viewModel.frequencySelected)
                }
                findNavController().navigate(R.id.action_addNewReminderFragment_to_addFrequencyFragment, result)
            }
            layoutSelectMedicine.setOnClickListener {
                val selectedList = ArrayList(viewModel.listInitialSelected.value ?: emptyList())
                val result = Bundle().apply {
                    putParcelableArrayList("selected_medicine", selectedList)
                }
                findNavController().navigate(
                    R.id.action_addNewReminderFragment_to_selectMedicineFragment,
                    result
                )
            }
            btnConfirm.setOnClickListener {
                val firstTime = selectedTimes.firstOrNull()
                if (firstTime != null) {
                    val (hourStr, minuteStr) = firstTime.time.split(":")
                    var hour = hourStr.toInt()
                    val minute = minuteStr.toInt()
                    if (firstTime.amPm == "PM" && hour != 12) {
                        hour += 12
                    } else if (firstTime.amPm == "AM" && hour == 12) {
                        hour = 0
                    }
                    context?.let { it1 -> alarmHelper.registerAlarm(it1, hour, minute) }
                    Toast.makeText(context, "Đã hẹn giờ thành công ${selectedTimes.first().time}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Chưa có giờ nào được chọn!", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun onClickMedicine(medicine: Medicine){

    }

    private fun insertReminder() {
        val content = binding.tietContent.text.toString()
        val note = binding.tietNote.text.toString()
        val disease = binding.tietDisease.text.toString()
        viewModel.insertReminder(content, note, disease)

    }

    override fun bindData() {
        initAllPicker()
        bindAdapter()
        parentFragmentManager.setFragmentResultListener("frequency_result_key", viewLifecycleOwner) { _, bundle ->
            val model = bundle.getSerializable("frequency_model") as? FrequencyModel
            model?.let {
                viewModel.selectFrequency(it)
                viewModel.getData()
            }
        }
        parentFragmentManager.setFragmentResultListener("selected_medicine_back", viewLifecycleOwner) { _, bundle ->
            val receivedList = bundle.getParcelableArrayList<Medicine>("selected_medicine") ?: emptyList()
            receivedList.let {
                viewModel.setListSelectedMedicine(it.toMutableList())
            }
        }
    }

    override fun destroy() {

    }

    private fun onRemoveTimeSelected(id: String) {
        selectedTimes.removeAll { it.id == id }
        selectedTimeAdapter.submitList(selectedTimes)
        binding.rcvListTime.adapter = selectedTimeAdapter
    }


    private fun bindAdapter() {
        //time
        binding.rcvListTime.layoutManager = LinearLayoutManager(
            binding.root.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        selectedTimeAdapter.submitList(selectedTimes)
        binding.rcvListTime.adapter = selectedTimeAdapter

        //medicine
        viewModel.listInitialSelected.observe(viewLifecycleOwner){
            binding.rcvListSelectedMedicines.layoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            selectedMedicineAdapter.submitList(it)
            binding.rcvListSelectedMedicines.adapter = selectedMedicineAdapter
        }
    }

    private fun initPicker(min: Int, max: Int, p: NumberPicker) {
        p.minValue = min
        p.maxValue = max
        p.setFormatter { i -> String.format("%02d", i) }

    }

    private fun initPickerWithString(min: Int, max: Int, p: NumberPicker, str: Array<String>) {
        p.minValue = min
        p.maxValue = max
        p.displayedValues = str
    }

    private fun initAllPicker() {
        binding.apply {
            val str = arrayOf<String>("AM", "PM")
            initPicker(0, 12, numPickerH)
            initPicker(0, 59, numPickerM)
            numPickerH.value = 12
            numPickerAm.value = 0
            initPickerWithString(0, (str.size - 1), numPickerAm, str)
        }
    }
}