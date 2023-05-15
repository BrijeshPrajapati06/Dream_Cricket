package com.diyantech.dreamcricket.team

import androidx.room.*
import com.diyantech.dreamcricket.model.ModelMatch
import com.diyantech.dreamcricket.model.ModelTeam

@Dao
interface TeamDao {

    @Insert
    suspend fun addTeamUser(modelTeam: ModelTeam)

    @Query("SELECT * FROM modelteam ORDER BY id DESC")
    suspend fun getAllTeam() : List<ModelTeam>

    @Update
    suspend fun updateTeamUser(modelTeam: ModelTeam)

    @Delete
    suspend fun deleteUser(modelTeam: ModelTeam)

}