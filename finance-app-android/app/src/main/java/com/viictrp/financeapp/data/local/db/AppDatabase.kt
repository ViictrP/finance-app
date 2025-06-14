package com.viictrp.financeapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.viictrp.financeapp.data.local.dao.BalanceDAO
import com.viictrp.financeapp.data.local.entity.BalanceEntity

@Database(
    entities = [BalanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDAO(): BalanceDAO
}