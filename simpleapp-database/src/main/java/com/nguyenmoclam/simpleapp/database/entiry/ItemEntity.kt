package com.nguyenmoclam.simpleapp.database.entiry

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val page: Int,
    val index: Long,
    val title: String,
    val description: String,
    val date: String,
    val initials: String?,
    val backgroundColor: Int?,
    val textColor: Int?
)
