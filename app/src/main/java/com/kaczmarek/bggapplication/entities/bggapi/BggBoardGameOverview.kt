package com.kaczmarek.bggapplication.entities.bggapi

import android.os.Parcel
import android.os.Parcelable

data class BggBoardGameOverview(
    val id: Long,
    val title: String,
    val yearPublished: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeInt(yearPublished)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BggBoardGameOverview> {
        override fun createFromParcel(parcel: Parcel): BggBoardGameOverview {
            return BggBoardGameOverview(parcel)
        }

        override fun newArray(size: Int): Array<BggBoardGameOverview?> {
            return arrayOfNulls(size)
        }
    }
}
