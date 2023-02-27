package com.example.noteapp.data.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import com.example.noteapp.data.models.Priorities

class SharedViewModel(application: Application):AndroidViewModel(application) {
     fun parsePriority(priority: String): Priorities {
        return when (priority) {
            "High Priority" -> {
                Priorities.HIGH
            }
            "Medium Priority" -> {
                Priorities.MEDIUM
            }
            "Low Priority" -> {
                Priorities.LOW
            }
            else -> Priorities.LOW
        }
    }

   fun verifyDataFromUser(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else !(title.isEmpty() || description.isEmpty())
    }

}