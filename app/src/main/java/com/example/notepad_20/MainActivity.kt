package com.example.notepad_20

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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notesDao: NotesDAO

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
            parent, view, position, id ->

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.options_menu)
            dialog.show()
            /*
            val selectedNote = parent.getItemAtPosition(position) as NoteData
            val result = notesDao.deleteNote(selectedNote)
            loadNotes()
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
            result

             */
            true
        }
    }

    fun saveNote(){

        val noteText = binding.textInput.text.toString()
        val date = getDate()
        if (noteText != ""){

            val note = NoteData(-1, date, noteText)
            notesDao.saveNote(note)
            binding.textInput.text.clear()
            Toast.makeText(this, "Note saved correctly", Toast.LENGTH_SHORT).show()
            loadNotes()

        }else{

            Toast.makeText(this, "Note text can not be empty", Toast.LENGTH_SHORT).show()

        }
    }

    fun loadNotes(){

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesDao.loadNotes())
        binding.noteListView.adapter = arrayAdapter

    }

    fun getDate(): String{

        var dateString: String = ""

        dateString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
            current.format(formatter)


        } else {

            var date = Date()
            val formatter = SimpleDateFormat("dd MMM yyyy HH:mma")
            formatter.format(date)
        }

        Log.d("answer",dateString)
        return dateString

    }
}