package com.example.notepad_20

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList

class NotesDAO(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {


    val TABLE_NOTES = "userNotes"
    val COL_ID = "id"
    val COL_DATE = "date"
    val COL_NOTE = "noteText"

    constructor(context: Context?): this(context, "NotesDB", null, 1)

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NOTES ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_DATE TEXT, $COL_NOTE TEXT)"
        db?.execSQL(createTableQuery)
    }

    fun saveNote(note: NoteData): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_DATE, note.date)
        cv.put(COL_NOTE, note.note)

        val result = db.insert(TABLE_NOTES, null, cv)
        db.close()
        return result != -1L
    }

    fun deleteNote(note: NoteData): Boolean{

        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NOTES WHERE $COL_ID = ${note.id}"
        val cursor = db.rawQuery(query, null)

        return cursor.moveToFirst() || cursor.count == 0

    }

    fun loadNotes(): List<NoteData>{

        val returnList = ArrayList<NoteData>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NOTES"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()){

            do {

                val noteId = cursor.getInt(0)
                val noteDate = cursor.getString(1)
                val noteText = cursor.getString(2)

                val note = NoteData(noteId, noteDate, noteText)
                returnList.add(note)

            }while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return returnList.reversed()

    }

    fun overwriteNote(note: NoteData): Boolean{

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_DATE, note.date)
        cv.put(COL_NOTE, note.note)

        val result = db.update(TABLE_NOTES, cv, "$COL_ID = ${note.id}", null)
        return result != -1
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}