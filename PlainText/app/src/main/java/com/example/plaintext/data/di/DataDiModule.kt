package com.example.plaintext.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.plaintext.data.PlainTextDatabase
import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.ui.screens.hello.dbSimulator // Add this import
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

    // ADD THIS FUNCTION
    @Provides
    @Singleton
    fun provideDBSimulator(): dbSimulator {
        return dbSimulator()
    }
}