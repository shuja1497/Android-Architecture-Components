package com.shuja1497.rooming

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE

@Dao
interface UserDao {

    @Query("select * from user")
    fun getAllUsers(): List<User>

    @Query("select * from user where id = :userId")
    fun getUser(userId: Int): User

    @Query("delete from user")
    fun deleteAllUser()

    @Insert(onConflict = REPLACE)
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update(onConflict = REPLACE)
    fun updateUser(user: User)

}