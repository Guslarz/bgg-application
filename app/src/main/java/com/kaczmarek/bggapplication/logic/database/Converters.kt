package com.kaczmarek.bggapplication.logic.database

import androidx.room.TypeConverter
import com.kaczmarek.bggapplication.entities.database.BoardGameType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset


class Converters {

    companion object {
        private val ZONE_OFFSET = ZoneOffset.UTC
    }

    @TypeConverter
    fun localDateToEpochDay(localDate: LocalDate): Long {
        return localDate.toEpochDay()
    }

    @TypeConverter
    fun epochDayToLocalDate(epochDay: Long): LocalDate {
        return LocalDate.ofEpochDay(epochDay)
    }

    @TypeConverter
    fun localDateTimeToEpochSecond(localDateTime: LocalDateTime): Long {
        return localDateTime.toEpochSecond(ZONE_OFFSET)
    }

    @TypeConverter
    fun epochSecondToLocalDateTime(epochSecond: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(epochSecond, 0, ZONE_OFFSET)
    }

    @TypeConverter
    fun boardGameTypeToOrdinal(boardGameType: BoardGameType): Int {
        return boardGameType.ordinal
    }

    @TypeConverter
    fun ordinalToBoardGameType(ordinal: Int): BoardGameType {
        return BoardGameType.values()[ordinal]
    }
}