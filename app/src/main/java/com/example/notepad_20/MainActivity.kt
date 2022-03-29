package com.example.notepad_20

import android.annotation.SuppressLint
import com.example.notepad_20.R
import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad_20.databinding.ActivityMainBinding
import com.example.notepad_20.databinding.OptionsMenuBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var optionsBinding: OptionsMenuBinding
    private lateinit var notesDao: NotesDAO
    private var isModifying = false
    private lateinit var noteToModify: NoteData
    private lateinit var noteslist: List<NoteData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notesDao = NotesDAO(this)
        loadNotes()

        binding.saveBtn.setOnClickListener {

            saveNote()

        }
    }

    /**
     * shows an options menu when long clicking a saved note to choose if the user wants
     * to delete or modify that note
     */
    private fun optionsMenu(selectedNote: NoteData): Boolean{

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        optionsBinding = OptionsMenuBinding.inflate(layoutInflater)
        dialog.setContentView(optionsBinding.root)
        dialog.show()
        var result = false

        optionsBinding.deleteBtn.setOnClickListener {

            result = notesDao.deleteNote(selectedNote)
            loadNotes()

            if (result){

                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }else{
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }

        optionsBinding.modifyBtn.setOnClickListener {

            binding.textInput.setText(selectedNote.note)
            noteToModify = selectedNote
            isModifying = true
            result = true

            if (result){
                Toast.makeText(this, "modify", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }else{
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            }

        }
        return result
    }

    /**
     * save the new note or overwrites an existing one
     */
    private fun saveNote(){

        val noteText = binding.textInput.text.toString()
        val date = getDate()
        if (noteText != ""){

            if (!isModifying){

                val note = NoteData(-1, date, noteText)
                notesDao.saveNote(note)
                Toast.makeText(this, "Note saved correctly", Toast.LENGTH_SHORT).show()

            }else{

                noteToModify.note = noteText
                noteToModify.date = date
                notesDao.overwriteNote(noteToModify)
                isModifying = false

            }

            binding.textInput.text.clear()
            loadNotes()

        }else{

            Toast.makeText(this, "Note text can not be empty", Toast.LENGTH_SHORT).show()

        }
    }

    /**
     * load all notes in the DataBase
     */
    private fun loadNotes(){

        noteslist = notesDao.loadNotes()
        binding.noteListView.layoutManager = LinearLayoutManager(this)
        val arrayAdapter = MyAdapter(noteslist){ position -> onListItemClick(position) }
        binding.noteListView.adapter = arrayAdapter

    }

    /**
     * returns a string with the actual date and time of the note creation
     * (two different functions based on OS version)
     */
    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String{

        var dateString: String = ""

        dateString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
            current.format(formatter)


        } else {

            val date = Date()
            val formatter = SimpleDateFormat("dd MMM yyyy HH:mma")
            formatter.format(date)
        }

        Log.d("answer",dateString)
        return dateString

    }

    private fun onListItemClick(position: Int){

        val selectedNote = noteslist[position]
        optionsMenu(selectedNote)

    }
}