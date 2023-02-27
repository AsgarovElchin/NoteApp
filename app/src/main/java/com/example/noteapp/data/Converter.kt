package com.example.noteapp.data

import androidx.room.TypeConverter
import androidx.room.TypeConverters


class Converter {

    @TypeConverter
    fun fromPriority(priority: Priorities): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priorities {
        return Priorities.valueOf(priority)

    }

}