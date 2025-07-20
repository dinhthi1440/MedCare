package com.example.medcare.views.my_medicine.add_medicine

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.example.medcare.BuildConfig
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response
import com.example.medcare.utils.FolderS3
import com.example.medcare.utils.S3UploaderUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AddMedicineViewModel(private val iMedicineRepos: IMedicineRepos) : BaseViewModel() {

    private val _setInsertStatus = MutableLiveData<Response<Any>> ()
    val  getInsertStatus: LiveData<Response<Any>> get() = _setInsertStatus
    fun insertMedicine(uid: String, medicine: Medicine, file: File?) {
        setIsLoading(true)
        viewModelScope.launch {
            if (file != null) {
                try {
                    val imageUrl = S3UploaderUtils.uploadFileToS3(FolderS3.MEDICINES.folderName, file)
                    if (imageUrl != null) {
                        medicine.image = imageUrl
                    } else {
                        setIsLoading(false)
                        withContext(Dispatchers.Main) {
                            _setInsertStatus.value = Response(500, "Có lỗi khi tải ảnh lên", null)
                        }
                        return@launch
                    }
                } catch (e: Exception) {
                    Log.e("S3Uploader", "Upload failed", e)
                    setIsLoading(false)
                    withContext(Dispatchers.Main) {
                        _setInsertStatus.value = Response(500, "Có lỗi khi tải ảnh lên", null)
                    }
                    return@launch
                }
            }
            val result = iMedicineRepos.insertMedicineRemote(uid, medicine)
            withContext(Dispatchers.Main) {
                setIsLoading(false)
                when (result.statusCode) {
                    200 -> {
                        _setInsertStatus.value =  result
                    }
                    else -> {
                        iMedicineRepos.insertMedicine(medicine)
                        _setInsertStatus.value = Response(500, "Lỗi khi tạo dữ liệu, hãy thử lại", null)
                    }
                }

            }
        }
    }


    fun updateMedicine(uid: String, medicine: Medicine, file: File?) {

        executeTask(
            request = {iMedicineRepos.updateMedicineRemote(uid, medicine)},
            onSuccess = {
                _setInsertStatus.value = it
            },
            onError = {
                _setInsertStatus.value = Response(
                    500,
                    "Lỗi khi cập nhật dữ liệu, hãy thử lại",
                    null
                )
            }
        )
    }

}