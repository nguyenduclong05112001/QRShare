package com.qrpay.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "name")
    var name: String = "You",

    @ColumnInfo(name = "avatar")
    var avatar: String = "",

    @ColumnInfo(name = "numberPhone")
    var numberPhone: String,
)
