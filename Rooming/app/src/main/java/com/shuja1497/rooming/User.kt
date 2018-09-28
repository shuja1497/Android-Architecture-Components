package com.shuja1497.rooming

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class User(var username:String,
                @PrimaryKey()var id: Int = 0,
                var location: String)