package com.example.medcare.base

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.medcare.extension.getData
import com.example.medcare.extension.openDlLoading
import com.example.medcare.utils.Constants
import com.google.gson.Gson
import java.text.DecimalFormat
import org.koin.android.ext.android.get

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater) -> VB
) : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding as VB
    protected abstract val viewModel: BaseViewModel
    protected val gson = Gson()
    protected var uid: String = ""
    protected var isBackReset: Boolean = false

    protected val sharedPreferences by lazy { get<SharedPreferences>() }
    private val dialog by lazy { context?.let { Dialog(it) } }
    protected val decimalFormat = DecimalFormat("#,###.###")
    protected fun dialog(context1: Context): Dialog {
        return Dialog(context1)
    }
    protected fun listenBackScreen(
        onResult: () -> Unit
    ) {
        parentFragmentManager.setFragmentResultListener("boolean_result_key", viewLifecycleOwner) { _, bundle ->
            val reload = bundle.getBoolean("key_boolean", false)
            if (reload) {
                onResult()
            }
        }
    }
    protected fun backScreenReset() {
        if (isBackReset) {
            val result = Bundle().apply {
                putBoolean("key_boolean", true)
            }
            parentFragmentManager.setFragmentResult("boolean_result_key", result)
            findNavController().popBackStack()
        } else {
            findNavController().popBackStack()
        }

    }

    protected fun showKeyboard(context1: Context) {
        val inputMethodManager =
            context1.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    protected fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    protected fun hideKeyboardAndClearFocus(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = sharedPreferences.getData(Constants.SHARED_USER_ID)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                dialog?.openDlLoading(false)
            } else {
                dialog?.dismiss()
            }
        }
        bindData()
        handleEvent()
        destroy()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backScreenReset()
                }
            }
        )
    }


    abstract fun initData()
    abstract fun handleEvent()
    abstract fun bindData()
    abstract fun destroy()

}