package com.example.plaintext.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.plaintext.data.PlainTextDatabase
import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.repository.LocalPasswordDBStore
import com.example.plaintext.data.repository.PasswordDBStore
import com.example.plaintext.ui.screens.hello.dbSimulator
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
            "plaintext_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("PlainTextPreferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDBSimulator(): dbSimulator {
        return dbSimulator()
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