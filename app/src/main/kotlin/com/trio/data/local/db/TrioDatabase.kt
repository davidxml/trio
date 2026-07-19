package com.trio.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserProfileEntity::class], version = 1, exportSchema = false)
abstract class TrioDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
}
