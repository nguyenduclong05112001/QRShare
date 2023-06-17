package com.qrpay.data.database.dao

import androidx.annotation.NonNull
import androidx.room.*
import com.qrpay.data.database.entity.User

@Dao
interface DaoUser {
    @Insert
    suspend fun insertUser(user: User)

    @Query("Select * from user")
    suspend fun getUser(): User

    @Update
    suspend fun updateUser(user: User)
}