package com.example.noteapp.data.viewmodel

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteData
import com.example.noteapp.data.models.Priorities

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase : MutableLiveData<Boolean> = MutableLiveData(true)
    fun checkIfDatabaseEmpty(noteData:List<NoteData> ){
        emptyDatabase.value = noteData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.red
                        )
                    )
                }
                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.yellow
                        )
                    )
                }
                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.green
                        )
                    )
                }
            }


        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }

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

   fun parsePriorityToInt(priorities: Priorities): Int {
        return when (priorities) {
            Priorities.HIGH -> 0
            Priorities.MEDIUM -> 1
            Priorities.LOW -> 2
        }

    }

}