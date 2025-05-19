package com.example.medcare.views.connect_relatives.connect_relatives_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentConnectRelativesBinding
import com.example.medcare.extension.addNoteInRelativeAdd
import com.example.medcare.extension.confirmEvent
import com.example.medcare.extension.getData
import com.example.medcare.extension.selectCustomDate
import com.example.medcare.models.Account
import com.example.medcare.models.Relative
import com.example.medcare.utils.Constants
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.google.gson.Gson

class ConnectRelativesFragment : BaseFragment<FragmentConnectRelativesBinding>(FragmentConnectRelativesBinding::inflate) {
    private val relativesAdapter by lazy {
        RelativeAdapter(::onView, ::onCreateReminder, ::onRemove)
    }
    private val addRelativeAdapter by lazy {
        AddRelativeAdapter(true, ::onClickRequest, ::onAddRelative)
    }
    private val searchAdapter by lazy {
        AddRelativeAdapter(false, ::onClickRequest, ::onAddRelative)
    }
    private var account: Account? = null
    override val viewModel by viewModel<ConnectRelativesViewModel>()
    private fun resetData(){
        viewModel.getAllRelativesRemote(uid)
        viewModel.getAllRelativeRequestRemote(uid)
    }

    override fun initData() {
        val json = sharedPreferences.getData(Constants.SHARED_USER)
        account = gson.fromJson(json, Account::class.java)
        resetData()
    }

    var isInputting = false
    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            cvRequestReminder.setOnClickListener {
                findNavController().navigate(R.id.action_connectRelativesFragment_to_relativeRequestAddFragment)
            }
            edtSearch.addTextChangedListener(object : TextWatcher {
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
                if (searchText == "") {
                    nestedScrollView3.visibility = View.VISIBLE
                    rcvSearchList.visibility = View.GONE
                } else {
                    nestedScrollView3.visibility = View.GONE
                    rcvSearchList.visibility = View.VISIBLE
                    viewModel.getSearchRelatives(searchText)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindData() {
        viewModel.getRelativeList.observe(viewLifecycleOwner) {
            binding.rcvRelativeList.layoutManager = LinearLayoutManager(binding.root.context)
            relativesAdapter.submitList(it)
            binding.rcvRelativeList.adapter = relativesAdapter
        }

        viewModel.getAddRequestList.observe(viewLifecycleOwner) {
            binding.txtNoRequestConnect.text = "(${it.size})"
            binding.rcvRequestAddFriend.layoutManager = LinearLayoutManager(binding.root.context)
            addRelativeAdapter.submitList(it)
            binding.rcvRequestAddFriend.adapter = addRelativeAdapter
        }

        viewModel.acceptStatus.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Chấp nhận thành công", Toast.LENGTH_SHORT).show()
                resetData()
            }
        }

        viewModel.getSearchList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.rcvSearchList.visibility = View.GONE
                binding.txtEmpty.visibility = View.VISIBLE
            } else {
                binding.rcvSearchList.visibility = View.VISIBLE
                binding.txtEmpty.visibility = View.GONE
                binding.rcvSearchList.layoutManager = LinearLayoutManager(binding.root.context)
                searchAdapter.submitList(it)
                binding.rcvSearchList.adapter = searchAdapter
            }
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

    }
    private fun onView(relative: Relative) {
        val json = Gson().toJson(relative)
        val bundle = Bundle().apply {
            putString("relative", json)
        }
        findNavController().navigate(R.id.action_connectRelativesFragment_to_relativeReminderHistoryFragment)
    }

    private fun onCreateReminder(relative: Relative) {
        val json = Gson().toJson(relative)
        val bundle = Bundle().apply {
            putString("relative", json)
        }
        findNavController().navigate(R.id.action_connectRelativesFragment_to_addNewReminderFragment, bundle)
    }

    private fun onRemove(relative: Relative) {
        dialog(requireContext()).confirmEvent("Xác nhận xoá", "Bạn có chắc chắn muốn xoá người này khỏi kết nối?") {

        }
    }

    private fun onClickRequest(relative: Relative) {
        viewModel.acceptRelativeRequestRemote(uid, relative)
    }

    private fun onAddRelative(relative: Relative) {
        dialog(requireContext()).addNoteInRelativeAdd{ relativeLabel ->

            val newRelative = Relative(uid, account?.fullName ?: "", relativeLabel, account?.avatar ?: "")
            viewModel.insertRelativesRequestRemote(relative.id, newRelative)
        }
    }

    override fun destroy() {

    }
}