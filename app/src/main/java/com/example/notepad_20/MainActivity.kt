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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notesDao = NotesDAO(this)
        loadNotes()

        binding.saveBtn.setOnClickListener {

            saveNote()

        }

        binding.noteListView.onItemLongClickListener = AdapterView.OnItemLongClickListener{
            parent, _, position, _ ->

            val selectedNote = parent.getItemAtPosition(position) as NoteData
            optionsMenu(selectedNote)

        }
    }

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

    private fun loadNotes(){

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesDao.loadNotes())
        binding.noteListView.adapter = arrayAdapter

    }

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
}