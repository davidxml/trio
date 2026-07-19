package com.trio.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val textScale: Float = 1.0f,
    val hapticIntensity: Float = 1.0f,
    val isCurrentUser: Boolean = false
)
