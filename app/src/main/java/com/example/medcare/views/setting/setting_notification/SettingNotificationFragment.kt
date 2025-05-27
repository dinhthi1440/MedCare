package com.example.medcare.views.setting.setting_notification

import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSettingNotificationBinding
import com.example.medcare.extension.confirmEvent
import com.example.medcare.extension.getDataBoolean
import com.example.medcare.extension.saveDataBoolean
import com.example.medcare.utils.Constants
import com.example.medcare.views.setting.setting_list.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingNotificationFragment : BaseFragment<FragmentSettingNotificationBinding>(
    FragmentSettingNotificationBinding::inflate) {

    override val viewModel by viewModel<SettingViewModel>()

    override fun initData() {

    }


    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAdd.setOnClickListener {
                dialog(requireContext()).confirmEvent(
                    "Xác nhận cập nhật",
                    "Bạn có chắc chắn muốn cập nhật thông tin?"
                ) {
                    handleCheckbox()
                }
            }
        }

    }
    private fun handleCheckbox() {
        val isReminderChecked = binding.checkReminder.isChecked
        val isMessageChecked = binding.checkMessage.isChecked
        val isDosageChecked = binding.checkDosage.isChecked
        val isMusicChecked = binding.checkMusic.isChecked
        val isShakeChecked = binding.checkShake.isChecked

        with(sharedPreferences) {
            saveDataBoolean(isReminderChecked, Constants.SHARED_NOTIFICATION_REMINDER)
            saveDataBoolean(isMessageChecked, Constants.SHARED_NOTIFICATION_MESSAGE)
            saveDataBoolean(isDosageChecked, Constants.SHARED_NOTIFICATION_DOSAGE)
            saveDataBoolean(isMusicChecked, Constants.SHARED_NOTIFICATION_MUSIC)
            saveDataBoolean(isShakeChecked, Constants.SHARED_NOTIFICATION_SHAKE)
        }
        Toast.makeText(context, "Đã cập nhật thành công", Toast.LENGTH_SHORT).show()
    }

    override fun bindData() {
        with(sharedPreferences) {
            binding.checkReminder.isChecked = getDataBoolean(Constants.SHARED_NOTIFICATION_REMINDER)
            binding.checkMessage.isChecked = getDataBoolean(Constants.SHARED_NOTIFICATION_MESSAGE)
            binding.checkDosage.isChecked = getDataBoolean(Constants.SHARED_NOTIFICATION_DOSAGE)
            binding.checkMusic.isChecked = getDataBoolean(Constants.SHARED_NOTIFICATION_MUSIC)
            binding.checkShake.isChecked = getDataBoolean(Constants.SHARED_NOTIFICATION_SHAKE)
        }
    }
    override fun destroy() {

    }
}