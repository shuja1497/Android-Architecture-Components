package com.shuja1497.rooming

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

interface UserDao {

    @Query("select * from user")
    fun getAllUsers(): List<User>

    @Query("select * from user where id = :userId")
    fun getUser(userId: Long): User

    @Insert(onConflict = REPLACE)
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update(onConflict = REPLACE)
    fun updateUser(user: User)
}