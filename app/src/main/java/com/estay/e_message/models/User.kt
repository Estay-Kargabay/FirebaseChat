package com.estay.e_message.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val proFileImageUrl: String): Parcelable {
    constructor() : this("", "", "")
}