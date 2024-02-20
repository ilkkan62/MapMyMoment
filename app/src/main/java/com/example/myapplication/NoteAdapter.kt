package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class NoteAdapter(var context: Context, var notes: List<Note>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return notes.size
    }
    override fun getItem(position: Int): Any {
        return notes[position]
    }
    override fun getItemId(position: Int): Long {
        return notes[position].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_view, parent, false)
            holder = ViewHolder()
            holder.title = view.findViewById(R.id.tvTitle)
            holder.message = view.findViewById(R.id.tvMessage)
            holder.location = view.findViewById(R.id.tvCurrentLocation)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val note = notes[position]
        holder.title.text = note.title
        holder.message.text = note.message
        holder.location.text = "Latitude: ${note.latitude}, Longitude: ${note.longitude}"

        return view
    }
    private class ViewHolder {
        lateinit var title: TextView
        lateinit var message: TextView
        lateinit var location: TextView
    }
}