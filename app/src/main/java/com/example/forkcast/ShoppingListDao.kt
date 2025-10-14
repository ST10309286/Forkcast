package com.example.forkcast

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItem)

    @Query("SELECT * FROM shopping_list")
    suspend fun getAllItems(): List<ShoppingItem>

    @Query("DELETE FROM shopping_list WHERE id = :id")
    suspend fun deleteItem(id: Int)

    @Update
    suspend fun updateItem(item: ShoppingItem)

    @Query("DELETE FROM shopping_list")
    suspend fun clearAll()
}
