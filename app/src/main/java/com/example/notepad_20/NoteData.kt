package com.example.notepad_20

import java.util.*

data class NoteData(val id: Int, var date: String, var note: String){

    override fun toString(): String {
        return "NoteData(id=$id, date=$date, note='$note')"
    }
}
