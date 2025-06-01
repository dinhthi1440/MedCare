package com.example.medcare.extension

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.databinding.DlAddRelativeBinding
import com.example.medcare.databinding.DlAnimationLoadingBinding
import com.example.medcare.databinding.DlChangeRuleBinding
import com.example.medcare.databinding.DlChangeStatusHistoryBinding
import com.example.medcare.databinding.DlConfirmBinding
import com.example.medcare.databinding.DlSelectCustomDateBinding
import com.example.medcare.models.HistoryStatus
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.DateCustom
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.DateCustomAdapter
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import com.example.medcare.databinding.DlChangeStatusFeedbackBinding
import com.example.medcare.databinding.DlConfirmPassAdminBinding
import com.example.medcare.databinding.DlEditHistoryRelativeBinding
import com.example.medcare.databinding.DlInputFeedbackBinding
import com.example.medcare.databinding.DlShowImageBinding
import com.example.medcare.models.Relative

fun Dialog.openDlLoading(stopFlag: Boolean) {
    val binding = DlAnimationLoadingBinding.inflate(layoutInflater)
    setContentView(binding.root)
    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    show()
    setCancelable(stopFlag)
}

fun Dialog.selectCustomDate(
    listInitialSelected: MutableList<DateCustom> = mutableListOf(),
    callback: (listDateSelected: List<DateCustom>?) -> Unit
) {
    val binding = DlSelectCustomDateBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val listDateSelected = listInitialSelected.toMutableList()

    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }

    val days = DateCustom.entries.toMutableList()

    val adapter = DateCustomAdapter(days, listDateSelected) { day, isChecked ->
        if (isChecked) {
            if (listDateSelected.none { it.abbreviation == day.abbreviation }) {
                listDateSelected.add(day)
            }
        } else {
            listDateSelected.removeAll { it.abbreviation == day.abbreviation }
        }
    }

    binding.rcvDays.layoutManager = LinearLayoutManager(context)
    binding.rcvDays.adapter = adapter

    binding.btnCancel.setOnClickListener {
        dismiss()
    }

    binding.btnOk.setOnClickListener {
        val sortedList = listDateSelected.sortedBy { it.ordinal }
        callback(sortedList)
        dismiss()
    }

    show()
}

fun Dialog.confirmEvent(
    title: String,
    content: String,
    onConfirm: () -> Unit,
) {
    val binding = DlConfirmBinding.inflate(layoutInflater)
    setContentView(binding.root)

    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(TRANSPARENT.toDrawable())
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    binding.apply {
        tvTitle.text = title
        tvContent.text = content
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnOk.setOnClickListener {
            onConfirm()
            dismiss()
        }
    }
    show()
}

fun Dialog.confirmFeedbackInput(
    onConfirm: (String) -> Unit,
) {
    val binding = DlInputFeedbackBinding.inflate(layoutInflater)
    setContentView(binding.root)

    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(TRANSPARENT.toDrawable())
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    binding.apply {
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnOk.setOnClickListener {
            val text = textipContent.text.toString()
            if (text.isNotBlank()) {
                onConfirm(text)
                dismiss()
            }
        }
    }
    show()
}

fun Dialog.confirmPassAdmin(
    onConfirm: (String) -> Unit,
) {
    val binding = DlConfirmPassAdminBinding.inflate(layoutInflater)
    setContentView(binding.root)

    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(TRANSPARENT.toDrawable())
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    binding.apply {
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnOk.setOnClickListener {
            val text = textipPassword.text.toString()
            if (text.isNotBlank()) {
                onConfirm(text)
                dismiss()
            }
        }
    }
    show()
}

fun Dialog.showImage(urlImage: String) {
    val binding = DlShowImageBinding.inflate(layoutInflater)
    setContentView(binding.root)

    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(TRANSPARENT.toDrawable())
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    Glide.with(binding.root.context).load(urlImage).into(binding.imageShowImage)
    binding.btnClose.setOnClickListener {
        dismiss()
    }
    show()
}

fun Dialog.addNoteInRelativeAdd(
    onConfirm: (relativeLabel: String) -> Unit,
) {
    val binding = DlAddRelativeBinding.inflate(layoutInflater)
    setContentView(binding.root)
    var unit = "Bạn bè"
    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(TRANSPARENT.toDrawable())
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    binding.apply {
        spinnerUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedUnit = parent.getItemAtPosition(position).toString()
                if (selectedUnit == "Khác") {
                    layoutTietAnother.visibility = View.VISIBLE
                } else {
                    layoutTietAnother.visibility = View.GONE
                    unit = selectedUnit
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnOk.setOnClickListener {
            if (unit == "Khác") {
                unit = tietAnother.text.toString().trim()
            }
            onConfirm(unit)
            dismiss()
        }
    }
    show()
}

fun Dialog.changeStatusHistory(
    onClick: (status: String) -> Unit,
) {
    val binding = DlChangeStatusHistoryBinding.inflate(layoutInflater)
    setContentView(binding.root)

    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(TRANSPARENT.toDrawable())
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    binding.apply {
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnDrink.setOnClickListener {
            onClick(HistoryStatus.DRANK.status)
            dismiss()
        }
        btnMissed.setOnClickListener {
            onClick(HistoryStatus.MISSED.status)
            dismiss()
        }
    }
    show()
}

fun Dialog.changeRule(
    initRule: String,
    onConfirm: (rule: String) -> Unit
) {
    val binding = DlChangeRuleBinding.inflate(layoutInflater)
    setContentView(binding.root)

    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    binding.apply {
        when (initRule) {
            "user" -> rgRole.check(R.id.rbUser)
            "doctor" -> rgRole.check(R.id.rbDoctor)
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnOk.setOnClickListener {
            val selectedRole = when (rgRole.checkedRadioButtonId) {
                R.id.rbUser -> "user"
                R.id.rbDoctor -> "doctor"
                else -> ""
            }
            onConfirm(selectedRole)
            dismiss()
        }
    }
    show()
}

fun Dialog.changeStatusFeedback(
    initRule: String,
    onConfirm: (rule: String) -> Unit
) {
    val binding = DlChangeStatusFeedbackBinding.inflate(layoutInflater)
    setContentView(binding.root)

    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    binding.apply {
        when (initRule) {
            "pending" -> rgRole.check(R.id.rbPending)
            "handled" -> rgRole.check(R.id.rbHandled)
            "processing" -> rgRole.check(R.id.rbProcessing)
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnOk.setOnClickListener {
            val selectedRole = when (rgRole.checkedRadioButtonId) {
                R.id.rbPending -> "pending"
                R.id.rbHandled -> "handled"
                else -> "processing"
            }
            if (selectedRole != initRule) {
                onConfirm(selectedRole)
            }
            dismiss()
        }
    }
    show()
}

fun Dialog.editHistoryRelative(
    initRelative: Relative,
    onConfirm: (relative: Relative) -> Unit
) {
    val binding = DlEditHistoryRelativeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    window?.apply {
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        attributes = attributes.apply {
            gravity = Gravity.CENTER
        }
    }
    binding.apply {
        binding.checkViewHistory.isChecked = initRelative.canSeeReminderHistory ?: true
        binding.checkCreateMedicine.isChecked = initRelative.canInsertMedicine ?: true
        binding.checkCreateReminder.isChecked = initRelative.canInsertReminder ?: true
        binding.textipContent.setText(initRelative.relativeTitle)
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnOk.setOnClickListener {
            val isCheckViewHistory = binding.checkViewHistory.isChecked
            val isCheckCreateMedicine = binding.checkCreateMedicine.isChecked
            val isCheckCreateReminder = binding.checkCreateReminder.isChecked
            val relativeTitle = binding.textipContent.text.toString()
            initRelative.canInsertMedicine = isCheckCreateMedicine
            initRelative.canInsertReminder = isCheckCreateReminder
            initRelative.canSeeReminderHistory = isCheckViewHistory
            if (relativeTitle.isNotBlank()) initRelative.relativeTitle = relativeTitle
            onConfirm(initRelative)
            dismiss()
        }
    }
    show()
}