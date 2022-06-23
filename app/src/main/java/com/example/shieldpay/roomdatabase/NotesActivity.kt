package com.example.shieldpay.roomdatabase

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import com.example.shieldpay.R
import com.example.shieldpay.basesetup.BaseActivity
import com.example.shieldpay.databinding.ActivityNotesBinding
import java.util.Date

class NotesActivity : BaseActivity<ActivityNotesBinding, NoteViewModel>(
    ActivityNotesBinding::inflate,
    NoteViewModel::class.java
), onItemClick, View.OnClickListener {

    private lateinit var dao: NoteDAO
    private lateinit var noteRepository: NoteRepository
    private lateinit var adapter: NoteAdapter
    private lateinit var mDialogView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.click = this
        dao = NoteDatabase.getDatabase(this).getDAO()
        noteRepository = NoteRepository(dao)
        getNotes()
    }

    private fun getNotes() {
        noteRepository.allNotes.observe(this, Observer { list ->
            list?.let {
                setNotesList(it as ArrayList<Note>)
            }
        })
    }

    private fun setNotesList(data: ArrayList<Note>) {
        adapter = NoteAdapter(data, listener = this)
        binding.rvNotes.adapter = adapter
    }

    override fun delete(note: Note) {
        vm.deleteNote(note)
        Toast.makeText(this, getString(R.string.delete_msg) + "${note.id}", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("InflateParams", "CutPasteId")
    override fun showPopup(id: Int, noteText: String) {
        mDialogView = layoutInflater.inflate(R.layout.popup_window, null)
        mDialogView.findViewById<AppCompatEditText>(R.id.dialog_edt_note).text =
            Editable.Factory.getInstance().newEditable(noteText)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.findViewById<AppCompatButton>(R.id.btn_save).setOnClickListener {
            val note =
                mDialogView.findViewById<AppCompatEditText>(R.id.dialog_edt_note).text.toString()
            vm.updateNote(Note(id, note, Date()))
            Toast.makeText(this, getString(R.string.update_msg) + "$id", Toast.LENGTH_LONG).show()
            mAlertDialog.dismiss()
            binding.edtNote.text = null
        }
        mDialogView.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnAddNote.id -> {
                val note = binding.edtNote.text.toString()
                if (note.isNotEmpty()) {
                    vm.insertNote(Note(0, note, Date()))
                    Toast.makeText(this, getString(R.string.insert_msg), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}