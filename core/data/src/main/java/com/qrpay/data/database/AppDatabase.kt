package com.qrpay.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qrpay.data.database.dao.DaoUser
import com.qrpay.data.database.entity.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDb(): DaoUser
}