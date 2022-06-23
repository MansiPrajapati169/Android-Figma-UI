package com.example.shieldpay.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shieldpay.R

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getDAO(): NoteDAO

    companion object {

        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            synchronized(this) {
                if (INSTANCE == null)
                    INSTANCE =
                        Room.databaseBuilder(context, NoteDatabase::class.java, context.getString(R.string.note_database))
                            .build()
            }
            return INSTANCE as NoteDatabase
        }

    }

}