package com.nguyenmoclam.simpleapp.database.entiry

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Khóa chính tự động tăng
    val page: Int,
    val index: Long,
    val title: String,
    val description: String,
    val date: String,
    val initials: String,          // Trường mới
    val backgroundColor: Int,      // Trường mới (ARGB)
    val textColor: Int             // Trường mới (ARGB)
)
