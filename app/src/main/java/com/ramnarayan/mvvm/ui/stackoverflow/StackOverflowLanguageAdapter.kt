package com.ramnarayan.mvvm.ui.stackoverflow

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramnarayan.mvvm.R


class StackOverflowLanguageAdapter(private var languageName: ArrayList<String>, private var dialog: BottomSheetDialog, private var  editText: EditText,
private var tagListener: TagListener) :
    RecyclerView.Adapter<StackOverflowLanguageAdapter.StackOverflowLanguageHolder>() {
    private lateinit var context: Context

    class StackOverflowLanguageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textLanguage: TextView = itemView.findViewById(R.id.textLanguage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StackOverflowLanguageHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false)
        return StackOverflowLanguageHolder(view)
    }

    override fun onBindViewHolder(holder: StackOverflowLanguageHolder, position: Int) {

        holder.textLanguage.text = languageName[position]
        holder.textLanguage.setOnClickListener {
            dialog.dismiss()
            editText.text.clear()
            tagListener.tagName(languageName[position])
        }
    }

    override fun getItemCount(): Int {
        return languageName.size
    }

    interface TagListener {
        fun tagName(name : String)
    }
}