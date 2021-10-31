package com.ramnarayan.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ramnarayan.data.room.entity.StackOverflowEntity

@Dao
interface StackOverflowDao {
    @Insert
    suspend fun insertIntoStackOverflow(stackOverflowEntity: StackOverflowEntity)

    @Query("DELETE FROM StackOverflowEntity")
    fun deleteAll()

    @Query("select * from StackOverflowEntity")
    public fun getAllStackOverflowEntity(): List<StackOverflowEntity>
}