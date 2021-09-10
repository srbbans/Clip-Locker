package bans.cliplocker.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import bans.cliplocker.model.Clip


@Database(entities = [Clip::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clipDao(): ClipDao

}