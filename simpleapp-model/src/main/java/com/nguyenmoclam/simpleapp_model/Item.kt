package com.nguyenmoclam.simpleapp_model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Item(
    var page: Int = 0,

    @field:Json(name = "index")
    val index: Long,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "date")
    val date: String,

    @field:Json(name = "description")
    val description: String,

    val initials: String? = null,          // Trường mới
    val backgroundColor: Int? = null,      // Trường mới (ARGB)
    val textColor: Int? = null           // Trường mới (ARGB)
) : Parcelable {

    @SuppressLint("DefaultLocale")
    fun getIndexFormatted(): String {
        return String.format("#%02d", index)
    }
}

