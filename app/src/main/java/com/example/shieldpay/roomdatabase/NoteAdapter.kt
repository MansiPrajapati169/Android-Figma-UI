package com.example.shieldpay.roomdatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shieldpay.databinding.NoteItemBinding

interface onItemClick {
    fun delete(note: Note)
    fun showPopup(id: Int, noteText: String)
}

class NoteAdapter(private val noteData: ArrayList<Note>, private val listener: onItemClick) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteDetails = noteData[position]
        val id = noteDetails.id
        holder.binding.tvNote.text = noteDetails.text
        holder.binding.tvDate.text = noteDetails.createdDate.toString()
        holder.binding.imgUpdate.setOnClickListener {
            listener.showPopup(id, noteDetails.text)
        }
        holder.binding.imgDelete.setOnClickListener {
            listener.delete(noteDetails)
        }
    }

    override fun getItemCount(): Int {
        return noteData.size
    }

}