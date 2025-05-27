package com.example.medcare.views.health_advice.chat_doctor_detail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentChatDoctorDetailBinding
import com.example.medcare.extension.getData
import com.example.medcare.models.Account
import com.example.medcare.models.ChatMessage
import com.example.medcare.models.DoctorChat
import com.example.medcare.utils.Constants
import com.example.medcare.utils.TimeUtils
import com.example.medcare.views.health_advice.chat_doctor.ChatDoctorListAdapter
import com.example.medcare.views.health_advice.chat_doctor.ChatDoctorViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ChatDoctorDetailFragment : BaseFragment<FragmentChatDoctorDetailBinding>(FragmentChatDoctorDetailBinding::inflate) {
    private lateinit var doctorID: String
    private lateinit var doctorName: String
    private lateinit var doctorAvatar: String
    private lateinit var account: Account
    private val chatMessageAdapter by lazy {
        ChatListAdapter(uid)
    }
    override val viewModel by viewModel<ChatDoctorViewModel>()
    override fun initData() {
        doctorID = arguments?.getString("doctorID") ?: ""
        doctorName = arguments?.getString("doctorName") ?: ""
        doctorAvatar = arguments?.getString("doctorAvatar") ?: ""
        val json = sharedPreferences.getData(Constants.SHARED_USER)
        account = gson.fromJson(json, Account::class.java)
        viewModel.getChatDetail(uid, doctorID)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgSend.setOnClickListener {
            val comment = binding.edtComment.text.toString()
            val timestamp: Long = System.currentTimeMillis()
            val formattedTime = TimeUtils.formatTimestamp(timestamp)
            if ( comment != "") {
                val chatMessage = ChatMessage(
                    UUID.randomUUID().toString(),
                    uid, comment, formattedTime, timestamp, "", false
                )
                val doctorChat = DoctorChat(
                    doctorID,
                    uid,
                    account.fullName,
                    account.avatar,
                    doctorID,
                    doctorName,
                    doctorAvatar,
                    comment,
                    formattedTime,
                    false,
                    "",
                    account.id,
                )
                viewModel.insertChatMessage(uid, doctorID, chatMessage, doctorChat)
                binding.edtComment.setText("")
            }
        }

        binding.rcvDoctorList.setOnTouchListener { v, _ ->
            hideKeyboard(v)
            false
        }
        view?.setOnTouchListener { v, _ ->
            hideKeyboard(v)
            false
        }
    }

    override fun bindData() {
        binding.txtLabel.text = doctorName
        viewModel.getChatDetail.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.txtLabelDoctorChatEmpty.visibility = View.VISIBLE
                binding.rcvDoctorList.visibility = View.GONE
            } else {
                binding.txtLabelDoctorChatEmpty.visibility = View.GONE

                binding.rcvDoctorList.layoutManager = LinearLayoutManager(binding.root.context)
                chatMessageAdapter.setList(it)
                binding.rcvDoctorList.adapter = chatMessageAdapter
                binding.rcvDoctorList.scrollToPosition(it.size - 1)
                binding.rcvDoctorList.visibility = View.VISIBLE
            }
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            binding.txtLabelDoctorChatEmpty.text = it
        }
        listenMessage()
    }

    private fun listenMessage() {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("chat_doctors")
            .document(doctorID)
            .collection("chat_messages")
            .orderBy("timeMessageLong")
            .addSnapshotListener { snapshots, e ->
                for (docChange in snapshots!!.documentChanges) {
                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            val chatMessage = docChange.document.toObject(ChatMessage::class.java)
                            viewModel.addChatMessage(chatMessage)
                        }
                        DocumentChange.Type.MODIFIED -> {

                        }
                        DocumentChange.Type.REMOVED -> {

                        }
                    }
                }
            }
    }
    override fun destroy() {

    }
}