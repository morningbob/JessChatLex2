package com.bitpunchlab.android.jesschatlex2.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bitpunchlab.android.jesschatlex2.helpers.WhoSaid
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "message_table")
@Parcelize
data class Message(
    @PrimaryKey
    val id: String,
    val whoSaid: WhoSaid,
    val message: String
) : Parcelable