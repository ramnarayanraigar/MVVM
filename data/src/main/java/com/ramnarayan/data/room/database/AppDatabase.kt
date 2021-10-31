package com.ramnarayan.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ramnarayan.data.room.dao.StackOverflowDao
import com.ramnarayan.data.room.entity.StackOverflowEntity


@Database(entities = [StackOverflowEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stackOverflow(): StackOverflowDao
}