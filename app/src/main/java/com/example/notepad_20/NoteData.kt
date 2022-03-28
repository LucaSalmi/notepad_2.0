package com.example.notepad_20

import java.util.*

data class NoteData(val id: Int, val date: String, val note: String){

    override fun toString(): String {
        return "NoteData(id=$id, date=$date, note='$note')"
    }
}
