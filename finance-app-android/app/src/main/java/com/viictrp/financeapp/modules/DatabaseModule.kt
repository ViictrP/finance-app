package com.viictrp.financeapp.modules

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.viictrp.financeapp.data.local.db.AppDatabase
import com.viictrp.financeapp.data.local.dao.BalanceDAO
import com.viictrp.financeapp.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `user` (
                    `id` TEXT NOT NULL,
                    `name` TEXT NOT NULL,
                    `email` TEXT NOT NULL,
                    `photoUrl` TEXT,
                    PRIMARY KEY(`id`)
                )
            """)
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finance-db"
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Provides
    fun provideBalanceDao(db: AppDatabase): BalanceDAO = db.balanceDAO()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}