package com.ramnarayan.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StackOverflowEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "all_data") val all_data : String,
)