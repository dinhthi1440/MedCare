package com.example.medcare.views.feedback.feedback_list

import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentFeedbackListBinding
import com.example.medcare.models.Feedback
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedbackListFragment : BaseFragment<FragmentFeedbackListBinding>(FragmentFeedbackListBinding::inflate) {
    private val feedbackAdapter by lazy {
        FeedbackAdapter(::onClickFeedBack)
    }
    override val viewModel by viewModel<FeedbackViewModel>()

    override fun initData() {
        viewModel.getFeedbackList()
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tilSearch.setEndIconOnClickListener {
            val searchText = binding.edtSearch.text.toString()
            Toast.makeText(context, "Đã bấm icon, tìm: $searchText", Toast.LENGTH_SHORT).show()
            hideKeyboard(it)
        }
    }

    override fun bindData() {
        viewModel.getFeedbacks.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.txtLabelFeedbackListEmpty.visibility = View.VISIBLE
                binding.rcvFeedbackList.visibility = View.GONE
            } else {
                binding.txtLabelFeedbackListEmpty.visibility = View.GONE
                binding.rcvFeedbackList.visibility = View.VISIBLE
                binding.rcvFeedbackList.layoutManager = LinearLayoutManager(binding.root.context)
                feedbackAdapter.submitList(it)
                binding.rcvFeedbackList.adapter = feedbackAdapter
            }
        }
    }

    private fun onClickFeedBack(feedback: Feedback){
        findNavController().navigate(R.id.action_feedbackListFragment_to_feedbackDetailFragment)
    }

    override fun destroy() {

    }
}