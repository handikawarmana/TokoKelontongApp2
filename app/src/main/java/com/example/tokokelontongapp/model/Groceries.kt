package com.example.tokokelontongapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "groceries_table")
data class Groceries(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val address: String,
    val number: String
) : Parcelable