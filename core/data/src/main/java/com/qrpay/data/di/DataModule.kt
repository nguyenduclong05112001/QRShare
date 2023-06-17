package com.qrpay.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.qrpay.data.database.AppDatabase
import com.qrpay.data.preference.AppSharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @DatabaseName
    @Provides
    @Singleton
    fun provideDatabaseName() = "qrpay.db"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context, @DatabaseName dbName: String
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}