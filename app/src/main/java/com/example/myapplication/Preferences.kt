package com.example.myapplication

import android.content.Context

class Preferences constructor(context: Context) {

    companion object {
        const val PREFERENCES_NOTE_TITLE = "note_title"
        const val PREFERENCES_NOTE_MESSAGE = "note_message"
        const val PREFERENCES_NOTE_LATITUDE = "note_latitude"
        const val PREFERENCES_NOTE_LONGITUDE = "note_longitude"
        const val PREFERENCES_NOTE_IMAGE = "note_image"
        const val PREFERENCES_NAME = "preferences_note"
    }

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setNoteTitle(noteTitle: String?) {
        preferences.edit().putString(PREFERENCES_NOTE_TITLE, noteTitle).apply()
    }

    fun getNoteTitle(): String? {
        return preferences.getString(PREFERENCES_NOTE_TITLE, null)
    }

    fun setNoteMessage(noteTitle: String?) {
        preferences.edit().putString(PREFERENCES_NOTE_MESSAGE, noteTitle).apply()
    }

    fun getNoteMessage(): String? {
        return preferences.getString(PREFERENCES_NOTE_MESSAGE, null)
    }

    fun setNoteLatitude(noteLatitude: String?) {
        preferences.edit().putString(PREFERENCES_NOTE_LATITUDE, noteLatitude).apply()
    }

    fun getNoteLatitude(): String? {
        return preferences.getString(PREFERENCES_NOTE_LATITUDE, null)
    }

    fun setNoteLongitude(noteLongitude: String?) {
        preferences.edit().putString(PREFERENCES_NOTE_LONGITUDE, noteLongitude).apply()
    }

    fun getNoteLongitude(): String? {
        return preferences.getString(PREFERENCES_NOTE_LONGITUDE, null)
    }

    fun setNoteImage(noteImage: String?) {
        preferences.edit().putString(PREFERENCES_NOTE_IMAGE, noteImage).apply()
    }

    fun getNoteImage(): String? {
        return preferences.getString(PREFERENCES_NOTE_IMAGE, null)
    }
}