package com.example.medcare.views.feedback.feedback_list

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentFeedbackListBinding
import com.example.medcare.models.Feedback
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedbackListFragment : BaseFragment<FragmentFeedbackListBinding>(FragmentFeedbackListBinding::inflate) {
    private var searchString = ""
    private val feedbackAdapter by lazy {
        FeedbackAdapter(::onClickFeedBack)
    }
    override val viewModel by viewModel<FeedbackViewModel>()

    override fun initData() {
        viewModel.getFeedbackList(searchString)
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tilSearch.setEndIconOnClickListener {
            val searchText = binding.edtSearch.text.toString()
            searchString = searchText
            viewModel.getFeedbackList(searchString)
            hideKeyboard(it)
        }
        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                val searchText = binding.edtSearch.text.toString()
                searchString = searchText
                viewModel.getFeedbackList(searchString)
                hideKeyboard(v)
                true
            } else {
                false
            }
        }
    }

    override fun bindData() {
        binding.apply {
            viewModel.getFeedbacks.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) {
                    txtFeedbackEmpty.visibility = View.VISIBLE
                    rcvFeedbackList.visibility = View.GONE
                } else {
                    txtFeedbackEmpty.visibility = View.GONE
                    rcvFeedbackList.visibility = View.VISIBLE
                    rcvFeedbackList.layoutManager = LinearLayoutManager(binding.root.context)
                    feedbackAdapter.submitList(it)
                    rcvFeedbackList.adapter = feedbackAdapter
                }
            }
            viewModel.messageError.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                txtFeedbackEmpty.text = it
            }
            listenBackScreen {
                viewModel.getFeedbackList(searchString)
            }
            viewModel.getUpdateStatus.observe(viewLifecycleOwner) {
                isBackReset = true
                viewModel.getFeedbackList(searchString)
            }
        }

    }

    private fun onClickFeedBack(feedback: Feedback){
        val bundle = Bundle().apply {
            putString("feedbackID", feedback.id)
        }
        findNavController().navigate(R.id.action_feedbackListFragment_to_feedbackDetailFragment, bundle)
    }

    override fun destroy() {

    }
}