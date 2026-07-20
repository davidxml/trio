package com.trio.core.di

import android.content.Context
import androidx.room.Room
import com.trio.data.local.db.DatabaseKeyManager
import com.trio.data.local.db.TrioDatabase
import com.trio.data.local.db.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @AppScope
    fun provideAppScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TrioDatabase {
        System.loadLibrary("sqlcipher")
        val passphrase = DatabaseKeyManager.getPassphrase(context)
        val factory = SupportOpenHelperFactory(passphrase)
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
