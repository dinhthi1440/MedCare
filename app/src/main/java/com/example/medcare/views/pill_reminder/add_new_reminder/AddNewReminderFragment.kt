package com.example.medcare.views.pill_reminder.add_new_reminder

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentAddNewReminderBinding
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.extension.RandomUtil
import com.example.medcare.extension.getData
import com.example.medcare.models.Account
import com.example.medcare.models.Medicine
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter
import com.example.medcare.models.PillReminder
import com.example.medcare.models.Relative
import com.example.medcare.utils.Constants
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddNewReminderFragment :
    BaseFragment<FragmentAddNewReminderBinding>(FragmentAddNewReminderBinding::inflate) {
    override val viewModel by viewModel<NewReminderViewModel>()
    private val selectedTimeAdapter by lazy { SelectedTimeAdapter(::onRemoveTimeSelected) }
    private val selectedMedicineAdapter by lazy { MedicineAdapter(false, ::onClickMedicine) }
    private lateinit var alarmHelper: AlarmHelper
    private var selectedTimes = mutableListOf(
        SelectedTime(id = RandomUtil.randomIDInt(), time = "8:00"),
        SelectedTime(id = RandomUtil.randomIDInt(), time = "12:30"),
    )
    private var reminderId = ""
    private var firstIn = true
    private var relative: Relative? = null
    private lateinit var account: Account
    override fun initData() {
        reminderId = arguments?.getString("reminder_id") ?: ""
        alarmHelper = AlarmHelper(this.requireContext())
        val jsonAccount = sharedPreferences.getData(Constants.SHARED_USER)
        account = gson.fromJson(jsonAccount, Account::class.java)
        val json = arguments?.getString("relative") ?: ""
        if (json != "") {
            relative = Gson().fromJson(json, Relative::class.java)
            viewModel.getData()
        } else {
            if (reminderId != "") {
                viewModel.getReminderByIdRemote(uid, reminderId)
            } else {
                Log.e("TAG", "initData: selected_medicine jhjeere")
                viewModel.getData()
            }
        }

    }

    override fun handleEvent() {

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAddTime.setOnClickListener {
                val pickerH = numPickerH.value
                val pickerM = numPickerM.value

                val formattedHour = String.format("%02d", pickerH)
                val formattedMinute = String.format("%02d", pickerM)
                val time = "$formattedHour:$formattedMinute"

                val isDuplicate = selectedTimes.any {
                    it.time == time
                }

                if (isDuplicate) {
                    Toast.makeText(context, "Thời gian này đã được chọn!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val newTimeSelected = SelectedTime(
                        id = RandomUtil.randomIDInt(),
                        time = time
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
                findNavController().navigate(
                    R.id.action_addNewReminderFragment_to_addFrequencyFragment,
                    result
                )
            }
            layoutSelectMedicine.setOnClickListener {
                val selectedList = ArrayList(viewModel.listInitialSelected.value ?: emptyList())
                val result = Bundle().apply {
                    putParcelableArrayList("selected_medicine", selectedList)
                    putString("relative_id", relative?.id ?: "")
                }
                findNavController().navigate(
                    R.id.action_addNewReminderFragment_to_selectMedicineFragment,
                    result
                )
            }
            btnConfirm.setOnClickListener {
                insertReminder()
            }
        }

    }

    private fun onClickMedicine(medicine: Medicine) {

    }

    private fun insertReminder() {
        val content = binding.tietContent.text.toString()
        val note = binding.tietNote.text.toString()
        val disease = binding.tietDisease.text.toString()

        if (content.isEmpty() || note.isEmpty() || disease.isEmpty() || selectedTimes.isEmpty() ||
            viewModel.listInitialSelected.value.isNullOrEmpty()
        ) {
            Toast.makeText(
                requireContext(),
                "Vui lòng điền đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (reminderId != "") {
            val pillReminder = viewModel.getPillReminder.value!!
            val newPillReminder = PillReminder(
                pillReminder.id,
                content,
                selectedTimes,
                viewModel.frequencySelected,
                viewModel.listInitialSelected.value?.toList() ?: listOf(),
                true, note, disease, account.id, account.fullName,
            )
            viewModel.updateReminder(uid, newPillReminder, alarmHelper, requireContext())
        } else {
            if (relative != null) {

                viewModel.insertReminderRelativeRemote(
                    selectedTimes, content, note, disease,
                    account.id,
                    account.fullName,
                    account.avatar,
                    "",
                    relative?.id ?: "",
                    relative?.fullName ?: "",
                    relative?.avatar ?: "",
                    relative?.relativeTitle ?: ""
                )
            } else {
                viewModel.insertReminder(
                    uid,
                    selectedTimes,
                    content,
                    note,
                    disease,
                    account.id,
                    account.fullName,
                    account.avatar
                )
            }
        }
    }

    override fun bindData() {
        if (reminderId == "") {
            binding.txtError.visibility = View.GONE
            binding.nestedScrollView2.visibility = View.VISIBLE
            if (relative != null) {
                binding.layoutCreateTo.visibility = View.VISIBLE
                binding.txtRelativeName.text = relative?.fullName ?: ""
                binding.txtDescription.text = relative?.relativeTitle ?: ""
            }
        }
        initAllPicker()
        bindAdapter()
        parentFragmentManager.setFragmentResultListener(
            "frequency_result_key",
            viewLifecycleOwner
        ) { _, bundle ->
            val model = bundle.getSerializable("frequency_model") as? FrequencyModel
            model?.let {
                viewModel.selectFrequency(it)
                viewModel.getData()
            }
        }
        parentFragmentManager.setFragmentResultListener(
            "selected_medicine_back",
            viewLifecycleOwner
        ) { _, bundle ->
            val receivedList =
                bundle.getParcelableArrayList<Medicine>("selected_medicine") ?: emptyList()
            receivedList.let {
                viewModel.setListSelectedMedicine(it.toMutableList())
            }
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.getUpdateStatus.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Đã sửa thành công", Toast.LENGTH_SHORT).show()
                backScreenReset()
            } else {
                Toast.makeText(context, "Đã có lỗi khi sửa, hãy thử lại", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getPillReminder.observe(viewLifecycleOwner) { reminder ->
            if (reminderId != "") {
                if (firstIn) {
                    binding.nestedScrollView2.visibility = View.VISIBLE
                    binding.txtError.visibility = View.GONE
                    selectedTimes = reminder.times.toMutableList()
                    binding.tietContent.setText(reminder.label)
                    binding.tietNote.setText(reminder.note)
                    binding.tietDisease.setText(reminder.disease)
                    viewModel.selectFrequency(reminder.frequency)
                    viewModel.setListSelectedMedicine(reminder.medicines.toMutableList())
                    firstIn = false
                }
            } else {
                if (relative != null) {
                    Toast.makeText(
                        context,
                        "Đã gửi yêu cầu nhắc nhở thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    context?.let { it1 -> alarmHelper.registerAlarm(it1, reminder) }
                    Toast.makeText(context, "Đã hẹn giờ thành công", Toast.LENGTH_SHORT).show()
                }
                backScreenReset()

            }
        }
        viewModel.getFrequencyStr.observe(viewLifecycleOwner) {
            binding.txtFrequency.text = it
        }
    }

    override fun destroy() {

    }

    private fun onRemoveTimeSelected(id: Int) {
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
        viewModel.listInitialSelected.observe(viewLifecycleOwner) {
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


    private fun initAllPicker() {
        binding.apply {
            initPicker(0, 23, numPickerH)
            initPicker(0, 59, numPickerM)
            numPickerH.value = 23
        }
    }
}