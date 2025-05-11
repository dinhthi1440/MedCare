package com.example.medcare.views.connect_relatives.connect_relatives_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.models.Relative

class ConnectRelativesViewModel : BaseViewModel() {
    val relatives = listOf(
        Relative(
            id = "r1",
            name = "Nguyễn Văn An",
            relativeTitle = "Cha",
            avatar = "https://example.com/avatar1.jpg"
        ),
        Relative(
            id = "r2",
            name = "Trần Thị Bình",
            relativeTitle = "Mẹ",
            avatar = "https://example.com/avatar2.jpg"
        ),
        Relative(
            id = "r3",
            name = "Lê Văn Cường",
            relativeTitle = "Anh trai",
            avatar = "https://example.com/avatar3.jpg"
        ),
        Relative(
            id = "r4",
            name = "Phạm Thị Dung",
            relativeTitle = "Chị gái",
            avatar = "https://example.com/avatar4.jpg"
        ),
        Relative(
            id = "r5",
            name = "Đỗ Minh Hằng",
            relativeTitle = "Em gái",
            avatar = "https://example.com/avatar5.jpg"
        )
    )
    private val _setRelativeList = MutableLiveData<MutableList<Relative>>()
    val getRelativeList: LiveData<MutableList<Relative>> get() = _setRelativeList

    private val _setAddRequestList = MutableLiveData<MutableList<Relative>>()
    val getAddRequestList: LiveData<MutableList<Relative>> get() = _setAddRequestList

    fun getRelatives() {
        _setRelativeList.value = relatives.toMutableList()
        _setAddRequestList.value = relatives.toMutableList()
    }
}