package bans.cliplocker.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import bans.cliplocker.model.Clip

@Dao
interface ClipDao {

    @Insert
    fun insertClip(clip: Clip)

    @Delete
    fun deleteClip(clip: Clip)

    @Query("SELECT * FROM Clip")
    fun getAll(): List<Clip>

}