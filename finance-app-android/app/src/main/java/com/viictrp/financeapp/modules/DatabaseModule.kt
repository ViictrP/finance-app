package com.viictrp.financeapp.modules

import android.content.Context
import androidx.room.Room
import com.viictrp.financeapp.data.local.db.AppDatabase
import com.viictrp.financeapp.data.local.dao.BalanceDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finance-db"
        ).build()
    }

    @Provides
    fun provideBalanceDao(db: AppDatabase): BalanceDAO = db.balanceDAO()
}