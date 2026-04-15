package com.example.inventorysystem_crud

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Inventory(
    @PrimaryKey
    val itemId: String,
    val itemName: String,
    val quantity: Int,
    val location: String,
    val status: String
) : java.io.Serializable