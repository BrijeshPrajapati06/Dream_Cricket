package com.diyantech.dreamcricket

import androidx.room.*
import com.diyantech.dreamcricket.model.ModelMatch


@Dao
interface MatchDao {

    @Insert
    suspend fun addUser(modelMatch: ModelMatch)

    @Query("SELECT * FROM modelmatch ORDER BY id DESC")
    suspend fun getAllMatch() : List<ModelMatch>

    @Update
    suspend fun updateUser(modelMatch: ModelMatch)

    @Delete
    suspend fun deleteUser(modelMatch: ModelMatch)
}