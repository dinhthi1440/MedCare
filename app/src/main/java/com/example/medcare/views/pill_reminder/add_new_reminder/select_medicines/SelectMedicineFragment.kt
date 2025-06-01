package com.example.medcare.views.pill_reminder.add_new_reminder.select_medicines

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSelectMedicineBinding
import com.example.medcare.models.Medicine
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.medcare.R

class SelectMedicineFragment :
    BaseFragment<FragmentSelectMedicineBinding>(FragmentSelectMedicineBinding::inflate) {
    override val viewModel by viewModel<SelectMedicineViewModel>()
    private val selectMedicineAdapter by lazy {
        SelectMedicineAdapter(
            viewModel.getSelectedMedicine.value?.toMutableSet() ?: mutableSetOf(),
            ::onSelectMedicine,
            ::onUnSelectMedicine
        )
    }
    private var relativeID = ""
    private lateinit var listInitialSelected: MutableSet<Medicine>

    override fun initData() {
        val receivedList = arguments?.getParcelableArrayList<Medicine>("selected_medicine") ?: emptyList()
        relativeID = arguments?.getString("relative_id") ?: ""
        listInitialSelected = receivedList.toMutableSet()
        if (relativeID == "") {
            viewModel.getMedicineList(uid)
        } else {
            viewModel.getMedicineList(relativeID)
        }
        viewModel.initData(listInitialSelected)
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnConfirm.setOnClickListener {
            val selectedList = ArrayList(viewModel.getSelectedMedicine.value ?: emptyList())
            val result = Bundle().apply {
                putParcelableArrayList("selected_medicine", selectedList)
            }
            parentFragmentManager.setFragmentResult("selected_medicine_back", result)
            findNavController().popBackStack()
        }
        binding.layoutAddNew.setOnClickListener {
            val bundle = Bundle().apply {
                putString("relativeID", relativeID)
            }
            findNavController().navigate(R.id.action_selectMedicineFragment_to_addMedicineFragment, bundle)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val REQUEST_KEY_NEW_MEDICINE = "new_medicine_request"
        val BUNDLE_KEY_MEDICINE = "new_medicine_object"

        setFragmentResultListener(REQUEST_KEY_NEW_MEDICINE) { requestKey, bundle ->
            val receivedMedicine: Medicine? = bundle.getParcelable(BUNDLE_KEY_MEDICINE)
            receivedMedicine?.let {
                viewModel.addMedicineToList(receivedMedicine)
            }
        }
    }

    override fun bindData() {
        viewModel.getMedicines.observe(viewLifecycleOwner) { it ->
            binding.txtEmptyList.visibility = View.GONE
            binding.rcvSelectMedicine.visibility = View.VISIBLE
            binding.rcvSelectMedicine.layoutManager = LinearLayoutManager(binding.root.context)
            selectMedicineAdapter.submitList(it)
            binding.rcvSelectMedicine.adapter = selectMedicineAdapter
        }
        listenBackScreen {
            if (relativeID == "") {
                viewModel.getMedicineList(uid)
            } else {
                viewModel.getMedicineList(relativeID)
            }
        }
    }

    private fun onSelectMedicine(medicine: Medicine) {
        viewModel.selectMedicine(medicine)
    }

    private fun onUnSelectMedicine(medicine: Medicine) {
        viewModel.unSelectMedicine(medicine)
    }

    override fun destroy() {

    }
}