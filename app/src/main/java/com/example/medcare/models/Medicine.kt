package com.example.medcare.models

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medcare.data.database.local.DataBaseLocal
import java.io.Serializable
@Entity(tableName = DataBaseLocal.TABLE_MEDICINE)
data class Medicine(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var image: String = "",
    var expirationDate: String = "",
    var quantity: Int = 0,
    var realQuantity: Int = 0,
    var dosage: Int = 0,
    var unit: String = "",
    var note: String = "",
    var createAt: String = "",
    var updateAt: String = "",
    var creatorID: String = "",
    var creatorName: String = ""
) : Serializable, Parcelable {
    constructor() : this("", "", "", "", 0, 0, 0, "", "", "", "", "", "")
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<Medicine>() {
            override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
                oldItem == newItem
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<Medicine> {
            override fun createFromParcel(parcel: Parcel): Medicine {
                return Medicine(
                    id = parcel.readString() ?: "",
                    name = parcel.readString() ?: "",
                    image = parcel.readString() ?: "",
                    expirationDate = parcel.readString() ?: "",
                    quantity = parcel.readInt(),
                    realQuantity = parcel.readInt(),
                    dosage = parcel.readInt(),
                    unit = parcel.readString() ?: "",
                    note = parcel.readString() ?: "",
                    createAt = parcel.readString() ?:"",
                    updateAt = parcel.readString() ?:""
                )
            }

            override fun newArray(size: Int): Array<Medicine?> = arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(expirationDate)
        parcel.writeInt(quantity)
        parcel.writeInt(realQuantity)
        parcel.writeInt(dosage)
        parcel.writeString(unit)
        parcel.writeString(note)
        parcel.writeString(createAt)
        parcel.writeString(updateAt)
    }
}
