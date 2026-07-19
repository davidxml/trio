package com.trio.core.di

import android.content.Context
import androidx.room.Room
import com.trio.data.local.db.TrioDatabase
import com.trio.data.local.db.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TrioDatabase {
        return Room.databaseBuilder(
            context,
            TrioDatabase::class.java,
            "trio_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserProfileDao(database: TrioDatabase): UserProfileDao {
        return database.userProfileDao()
    }
}
