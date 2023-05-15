package com.diyantech.dreamcricket.player

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.diyantech.dreamcricket.model.ModelPlayer
import com.diyantech.dreamcricket.model.ModelTeam

@Dao
interface PlayerDao {


    @Insert
    suspend fun addPlayerUser(modelPlayer: ModelPlayer)

    @Query("SELECT * FROM modelplayer ORDER BY id DESC")
    suspend fun getAllPlayer() : List<ModelPlayer>

    @Update
    suspend fun updatePlayerUser(modelPlayer: ModelPlayer)

    @Delete
    suspend fun deleteUser(modelPlayer: ModelPlayer)
}