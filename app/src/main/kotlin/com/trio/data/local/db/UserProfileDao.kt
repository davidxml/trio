package com.trio.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profiles ORDER BY isCurrentUser DESC, name ASC")
    fun observeProfiles(): Flow<List<UserProfileEntity>>

    @Query("SELECT * FROM user_profiles WHERE isCurrentUser = 1 LIMIT 1")
    fun observeCurrentUser(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE id = :id")
    suspend fun getById(id: Long): UserProfileEntity?

    @Insert
    suspend fun insert(profile: UserProfileEntity): Long

    @Update
    suspend fun update(profile: UserProfileEntity)

    @Delete
    suspend fun delete(profile: UserProfileEntity)

    @Query("UPDATE user_profiles SET isCurrentUser = 0")
    suspend fun clearCurrentUser()

    @Query("UPDATE user_profiles SET isCurrentUser = 1 WHERE id = :id")
    suspend fun setCurrentUser(id: Long)
}
