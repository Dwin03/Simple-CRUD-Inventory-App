package com.example.inventorysystem_crud

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Inventory)

    @Query("SELECT * FROM items")
    suspend fun getAllItems(): List<Inventory>

    @Query("SELECT * FROM items WHERE itemName LIKE :query OR itemId LIKE :query")
    suspend fun searchDatabase(query: String): List<Inventory>

    @Delete
    suspend fun deleteItem(item: Inventory)

    @Update
    suspend fun updateItem(item: Inventory)
}