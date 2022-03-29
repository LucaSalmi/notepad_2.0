package com.example.notepad_20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (private val list: List<NoteData>, private val onItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_layout, parent, false)


        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.textView.text = list[position].note
        holder.dateView.text = list[position].date


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        val textView: TextView = itemView.findViewById(R.id.note_text_view)
        val dateView: TextView = itemView.findViewById(R.id.date_text_view)

        init {
            ItemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            onItemClicked(position)
        }


    }

}