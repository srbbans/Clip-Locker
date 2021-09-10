package bans.cliplocker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clip(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "dateTime") val dateTime: String
)
