package com.example.plaintext.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.plaintext.data.PlainTextDatabase
import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.repository.LocalPasswordDBStore
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDiModule {
    // Version number for the database
    private const val DATABASE_VERSION = 1

    @Provides
    fun providePasswordDao(appDatabase: PlainTextDatabase): PasswordDao {
        return appDatabase.passwordDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): PlainTextDatabase {
        return Room.databaseBuilder(
            appContext,
            PlainTextDatabase::class.java,
            "plaintext-db"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("PlainTextPreferences", Context.MODE_PRIVATE)
    }
}

// Create an abstract module for binding interfaces to implementations
@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {
    @Binds
    @Singleton
    abstract fun bindPasswordDBStore(
        localPasswordDBStore: LocalPasswordDBStore
    ): PasswordDBStore
}