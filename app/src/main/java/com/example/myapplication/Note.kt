package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note (
    @ColumnInfo(name="title") var title: String,
    @ColumnInfo(name="message") var message: String,
    @ColumnInfo(name="latitude") var latitude: Double,
    @ColumnInfo(name="longitude") var longitude: Double,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)