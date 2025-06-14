package com.viictrp.financeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balance")
data class BalanceEntity(
    @PrimaryKey val yearMonth: String,
    val content: String,
    val lastUpdated: Long
)