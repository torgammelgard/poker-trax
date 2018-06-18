package se.torgammelgard.pokertrax.model.converters

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun timeStampToDate(timeStamp: Long): Date {
        return Date(timeStamp)
    }
}