package com.diyantech.dreamcricket

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.diyantech.dreamcricket.model.Converters
import com.diyantech.dreamcricket.model.ModelMatch
import com.diyantech.dreamcricket.model.ModelPlayer
import com.diyantech.dreamcricket.model.ModelTeam
import com.diyantech.dreamcricket.player.PlayerDao
import com.diyantech.dreamcricket.team.TeamDao

@Database(entities = [ModelMatch::class, ModelTeam::class, ModelPlayer::class], version = 10)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMatchDao() : MatchDao
    abstract fun getTeamDao() : TeamDao
    abstract fun getPlayerDao() : PlayerDao

    companion object {

        @Volatile
        private var instance:AppDatabase? = null
        private val LOCK = Any()

//           operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
//                instance ?: buildDatabase(context).also {
//                    instance = it
//                }
//            }
//
//        private fun buildDatabase(context: Context) = Room.databaseBuilder(
//            context.applicationContext,
//            AppDatabase :: class.java,
//            "app-database"
//        ).fallbackToDestructiveMigration()
//            .build()
        @Synchronized
    fun getInstance(ctx: Context): AppDatabase {
        if(instance == null)
            instance = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java,
                "app-database")
                .fallbackToDestructiveMigration()
                .build()

        return instance!!

    }
    }

//

}