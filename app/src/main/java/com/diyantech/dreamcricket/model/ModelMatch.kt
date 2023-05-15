package com.diyantech.dreamcricket.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ModelMatch(
    var title : String,
    var teamone : String,
    var teamtwo : String,
    var date : String,
    var time : String
): Serializable {
    @PrimaryKey(autoGenerate = true) var id:Int = 0
}