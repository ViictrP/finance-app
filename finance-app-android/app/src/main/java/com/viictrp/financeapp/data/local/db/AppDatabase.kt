package com.viictrp.financeapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.viictrp.financeapp.data.local.dao.BalanceDAO
import com.viictrp.financeapp.data.local.dao.UserDao
import com.viictrp.financeapp.data.local.entity.BalanceEntity
import com.viictrp.financeapp.data.local.entity.UserEntity

@Database(
    entities = [BalanceEntity::class, UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDAO(): BalanceDAO
    abstract fun userDao(): UserDao
}
