package com.ramnarayan.mvvm.ui.stackoverflow

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.ramnarayan.domain.entities.StackOverflowQuestionListData
import com.ramnarayan.mvvm.R
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast

import android.content.ActivityNotFoundException
import android.net.Uri
import android.webkit.URLUtil

import com.ramnarayan.mvvm.utils.Util


class StackOverflowQuestionListAdapter(private var stackOverflowQuestionListData: StackOverflowQuestionListData) :
    RecyclerView.Adapter<StackOverflowQuestionListAdapter.StackOverflowQuestionListHolder>() {
    private lateinit var context: Context

    class StackOverflowQuestionListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUserProfile: AppCompatImageView = itemView.findViewById(R.id.imageUserProfile)
        val textQuestion: TextView = itemView.findViewById(R.id.textQuestion)
        val textUserName: TextView = itemView.findViewById(R.id.textUserName)
        val textCreatedDate: TextView = itemView.findViewById(R.id.textCreatedDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StackOverflowQuestionListHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_question_list, parent, false)
        return StackOverflowQuestionListHolder(view)
    }

    override fun onBindViewHolder(holder: StackOverflowQuestionListHolder, position: Int) {
        Picasso.get()
            .load(stackOverflowQuestionListData.items[position].owner.profile_image)
            .resize(100, 100)
            .transform(CropCircleTransformation())
            .into(holder.imageUserProfile)

        holder.textQuestion.text = stackOverflowQuestionListData.items[position].title
        holder.textUserName.text = stackOverflowQuestionListData.items[position].owner.display_name
        holder.textCreatedDate.text =
            convertDate(stackOverflowQuestionListData.items[position].creation_date)

        holder.itemView.setOnClickListener {
            openWebUrl(stackOverflowQuestionListData.items[position].link)
        }
    }


    override fun getItemCount(): Int {
        return stackOverflowQuestionListData.items.size
    }

    private fun convertDate(milliSecond: Int): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        return "Posted on: " + formatter.format(Date(milliSecond.toLong() * 1000))
    }

    private fun openWebUrl(url: String) {
        if (Util.isOnline(context)) {
            try {
                if (!URLUtil.isValidUrl(url)) {
                    Toast.makeText(context, " This is not a valid link", Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    context.startActivity(intent)
                }
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    " You don't have any browser to open web page",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                context.resources.getString(R.string.no_internet_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun filter(data: StackOverflowQuestionListData) {
        stackOverflowQuestionListData = data
        notifyDataSetChanged()
    }
}