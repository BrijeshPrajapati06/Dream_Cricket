package com.diyantech.dreamcricket.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class ModelPlayer(
    var playername: String,
    var teamname: String,
    var multiName: String,
    var playerImg: String
): Serializable {
    @PrimaryKey(autoGenerate = true) var id:Int = 0
}