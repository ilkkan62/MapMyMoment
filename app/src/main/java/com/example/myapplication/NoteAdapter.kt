package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.provider.MediaStore
import android.content.Intent
import android.app.Activity

private const val PICK_IMAGE_REQUEST = 1

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
            holder.imageView = view.findViewById(R.id.ivNoteImage)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val note = notes[position]
        holder.title.text = note.title
        holder.message.text = note.message
        holder.location.text = "Latitude: ${note.latitude}, Longitude: ${note.longitude}"

        // Image lt. Pfad hinzufügen
        val bitmap = BitmapFactory.decodeFile(note.image)
        holder.imageView.setImageBitmap(bitmap)

        // Für Imagepicker
        holder.imageView.setOnClickListener {
            val pickImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (context as Activity).startActivityForResult(pickImage, PICK_IMAGE_REQUEST)
        }

        return view
    }

    private class ViewHolder {
        lateinit var title: TextView
        lateinit var message: TextView
        lateinit var location: TextView
        lateinit var imageView: ImageView
    }
}