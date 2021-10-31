package com.ramnarayan.data.room

import android.content.Context
import androidx.room.Room
import com.ramnarayan.data.room.database.AppDatabase

object DatabaseBuilder {
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }

        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "flow_biz"
    ).build()
}
