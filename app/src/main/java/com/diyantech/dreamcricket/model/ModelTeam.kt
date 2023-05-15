package com.diyantech.dreamcricket.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.hdodenhof.circleimageview.CircleImageView
import java.io.Serializable

@Entity
data class ModelTeam(
    var team: String,
    var total: String,
    var teamPhoto : String
        ): Serializable {

    @PrimaryKey(autoGenerate = true) var id:Int = 0

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray? = null
}