package com.example.medcare.extension

import android.app.Dialog
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.databinding.DlAnimationLoadingBinding
import com.example.medcare.databinding.DlSelectCustomDateBinding
import com.example.medcare.views.add_new_reminder.add_frequency.DateCustom
import com.example.medcare.views.add_new_reminder.add_frequency.DateCustomAdapter
import com.example.medcare.views.add_new_reminder.add_frequency.FrequencyModel

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