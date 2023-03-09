package com.bitpunchlab.android.jesschatlex2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bitpunchlab.android.jesschatlex2.models.Message
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract val chatDAO: ChatDao

    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null

        @InternalCoroutinesApi
        fun getInstance(context: Context?): ChatDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context!!.applicationContext,
                        ChatDatabase::class.java,
                        "chat_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}