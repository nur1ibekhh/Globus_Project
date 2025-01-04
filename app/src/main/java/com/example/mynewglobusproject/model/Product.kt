package com.example.mynewglobusproject.model

import android.os.Parcel
import android.os.Parcelable

data class ApiResponse(
    val success: Boolean,
    val errMessage: String?,
    val errorCode: String?,
    val data: Data,
    val products: List<Product>
)


data class Product(
    val id: Int,
    val code: String,
    val name: String,
    val description: String,
    val price: Int,
    val discountPrice: Int?,
    val isNew: Boolean,
    val amount: Int,
    val category: Int,
    val images: List<Image>
)

data class Image(
    val id: Int,
    val image: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}

