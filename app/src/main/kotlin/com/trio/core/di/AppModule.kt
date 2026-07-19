package com.trio.core.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.trio.data.local.db.TrioDatabase
import com.trio.data.local.db.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DB_PASSPHRASE = "trio_secure_key_2024"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TrioDatabase {
        val factory = SupportFactory(DB_PASSPHRASE.toByteArray())
        return Room.databaseBuilder(
            context,
            TrioDatabase::class.java,
            "trio_database"
        )
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserProfileDao(database: TrioDatabase): UserProfileDao {
        return database.userProfileDao()
    }
}
