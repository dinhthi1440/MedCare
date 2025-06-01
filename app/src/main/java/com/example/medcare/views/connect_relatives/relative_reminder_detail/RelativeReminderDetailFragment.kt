package com.example.medcare.views.connect_relatives.relative_reminder_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentRelativeReminderDetailBinding
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.extension.confirmEvent
import com.example.medcare.models.Medicine
import com.example.medcare.models.ReminderRequestStatus
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class RelativeReminderDetailFragment :
    BaseFragment<FragmentRelativeReminderDetailBinding>(FragmentRelativeReminderDetailBinding::inflate) {
    private val medicineAdapter by lazy { MedicineAdapter(true, ::onclickMedicineItem) }
    private lateinit var reminderID: String
    private lateinit var alarmHelper: AlarmHelper
    override val viewModel by viewModel<RelativeReminderDetailViewModel>()

    override fun initData() {
        reminderID = arguments?.getString("reminderID").toString()
        viewModel.getReminderById(uid, reminderID)
        alarmHelper = AlarmHelper(this.requireContext())
    }

    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnCancel.setOnClickListener {
                dialog(requireContext()).confirmEvent(
                    "Xác nhận hủy",
                    "Bạn có chắc chắn muốn hủy nhắc nhở này?"
                ) {
                    alarmHelper.removeAlarmByReminder(requireContext(), viewModel.getRelativeReminder.value!!)
                    viewModel.deleteReminder(viewModel.getRelativeReminder.value!!)
                }
            }
            btnConfirm.setOnClickListener {
                dialog(requireContext()).confirmEvent(
                    "Xác nhận",
                    "Bạn có muốn chấp nhận lời nhắc này? Thuốc chưa có sẽ được tự động thêm vào danh sách của bạn"
                ) {
                    var reminder = viewModel.getRelativeReminder.value
                    reminder?.statusRequest = ReminderRequestStatus.ACCEPTED.status
                    viewModel.updateReminder(reminder!!)
                }
            }
            btnDelete.setOnClickListener {
                dialog(requireContext()).confirmEvent(
                    "Xác nhận xoá",
                    "Bạn có chắc chắn muốn xoá?"
                ) {
                    alarmHelper.removeAlarmByReminder(requireContext(), viewModel.getRelativeReminder.value!!)
                    var reminder = viewModel.getRelativeReminder.value
                    reminder?.statusRequest = ReminderRequestStatus.DELETED.status
                    viewModel.deleteReminder(reminder!!)
                }
            }
            btnDefuse.setOnClickListener {
                var reminder = viewModel.getRelativeReminder.value
                alarmHelper.removeAlarmByReminder(requireContext(), viewModel.getRelativeReminder.value!!)
                reminder?.statusRequest = ReminderRequestStatus.CANCEL.status
                viewModel.updateReminder(reminder!!)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindData() {
        viewModel.getRelativeReminder.observe(viewLifecycleOwner) { reminder ->
            binding.apply {
                nestedScrollView.visibility = View.VISIBLE
                if (reminder.senderID == uid) {
                    txtFromOrTo.text = "Tới: "
                    txtRelativeName.text = reminder.receiverName
                    txtDescription.text = reminder.receiverDescription
                    btnCancel.visibility = View.VISIBLE
                } else {
                    txtFromOrTo.text = "Tạo bởi: "
                    txtRelativeName.text = reminder.senderName
                    txtDescription.text = reminder.senderDescription
                    if (reminder.statusRequest == ReminderRequestStatus.ACCEPTED.status) {
                        btnDelete.visibility = View.VISIBLE
                        btnDefuse.visibility = View.VISIBLE
                    } else {
                        btnConfirm.visibility = View.VISIBLE
                        btnDefuse.visibility = View.VISIBLE
                    }
                }
                bindStatus(reminder.statusRequest ?: "")
                txtReminderLabel.text = "\uD83D\uDCCC ${reminder.label}"
                txtReminderFrequency.text = "\uD83D\uDD04 Tần suất: ${reminder.frequency.label}"
                val timeString = reminder.times.joinToString(", ") { it.time }
                txtReminderTime.text = "⏰ Thời gian: $timeString"
                txtDisease.text = "\uD83D\uDC89 Bệnh điều trị: ${reminder.disease}"
                txtNote.text = reminder.note
                rcvMedicineList.layoutManager = LinearLayoutManager(context)
                medicineAdapter.submitList(reminder.medicines)
                rcvMedicineList.adapter = medicineAdapter
            }
        }
        viewModel.getStatusUpdate.observe(viewLifecycleOwner) {
            bindStatus(it)
            if (it == ReminderRequestStatus.ACCEPTED.status) {
                alarmHelper.registerAlarm(requireContext(), viewModel.getRelativeReminder.value!!)
            }
        }
        viewModel.getDeleteStatus.observe(viewLifecycleOwner) {
            if (it) {
                isBackReset = true
                backScreenReset()
            }
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.txtError.text = it
        }
    }

    private fun onclickMedicineItem(medicine: Medicine) {
        val bundle = Bundle().apply {
            putString("medicine_id", medicine.id)
            putString("medicine_name", medicine.name)
            putString("previous_screen", "detail")
            if(viewModel.getRelativeReminder.value?.statusRequest == ReminderRequestStatus.REQUESTING.status && medicine.creatorID != "") {
                putSerializable("reminder", medicine)
                putString("creator", viewModel.getRelativeReminder.value?.senderName!!)
            } else if (medicine.creatorID != uid || medicine.creatorID != "") {
                putString("creator", viewModel.getRelativeReminder.value?.senderName!!)
                putString("ownerID", viewModel.getRelativeReminder.value?.senderName!!)
            }
        }
        findNavController().navigate(
            R.id.action_relativeReminderDetailFragment_to_medicineDetailFragment,
            bundle
        )
    }

    private fun bindStatus(status: String) {
        binding.apply {
            when (status) {
                ReminderRequestStatus.REQUESTING.status -> {
                    txtStatus.text = "Chờ xác nhận"
                    txtStatus.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.ccOrangeText
                        )
                    )
                }

                ReminderRequestStatus.ACCEPTED.status -> {
                    txtStatus.text = "Đã chấp nhận"
                    txtStatus.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.ccGreenText
                        )
                    )
                    btnConfirm.visibility = View.GONE
                    btnDefuse.visibility = View.VISIBLE
                    btnCancel.visibility = View.GONE
                    btnDelete.visibility = View.VISIBLE
                }

                ReminderRequestStatus.DELETED.status -> {
                    txtStatus.text = "Đã bị xóa"
                    btnConfirm.visibility = View.GONE
                    btnDefuse.visibility = View.GONE
                    btnCancel.visibility = View.GONE
                    btnDelete.visibility = View.GONE
                    txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccRedText))
                }

                ReminderRequestStatus.CANCEL.status -> {
                    txtStatus.text = "Đã bị từ chối"
                    txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccBlueText))
                    btnConfirm.visibility = View.GONE
                    btnDefuse.visibility = View.GONE
                    btnCancel.visibility = View.GONE
                    btnDelete.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun destroy() {

    }
}