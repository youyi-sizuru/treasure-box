package com.lifefighter.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListParcelable<T : Parcelable>(val list: List<T>?) : Parcelable