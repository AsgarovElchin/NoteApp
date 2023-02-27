package com.example.noteapp.data

import androidx.room.TypeConverter
import com.example.noteapp.data.models.Priorities


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