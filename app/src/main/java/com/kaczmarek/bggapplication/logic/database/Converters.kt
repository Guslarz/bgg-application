package com.kaczmarek.bggapplication.logic.database

import androidx.room.TypeConverter
import java.time.LocalDate


class Converters {
    @TypeConverter
    fun localDateToEpochDay(localDate: LocalDate): Long {
        return localDate.toEpochDay()
    }

    @TypeConverter
    fun epochDayToLocalDate(epochDay: Long): LocalDate {
        return LocalDate.ofEpochDay(epochDay)
    }
}