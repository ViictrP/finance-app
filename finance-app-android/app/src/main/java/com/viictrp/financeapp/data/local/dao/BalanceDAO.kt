package com.viictrp.financeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.viictrp.financeapp.data.local.entity.BalanceEntity

@Dao
interface BalanceDAO {
    @Query("SELECT * FROM balance WHERE yearMonth = :yearMonth LIMIT 1")
    suspend fun getBalance(yearMonth: String): BalanceEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveBalance(balance: BalanceEntity)

    @Query("DELETE FROM balance")
    suspend fun clearBalance()
}