package com.shuja1497.rooming

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class User(var username:String) {

    @ColumnInfo(name = "id")

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}