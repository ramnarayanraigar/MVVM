package com.ramnarayan.mvvm.ui.cocktail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.ramnarayan.domain.entities.CocktailList
import com.ramnarayan.mvvm.R
import com.squareup.picasso.Picasso

class CocktailListAdapter(private var list: List<CocktailList>) :
    RecyclerView.Adapter<CocktailListAdapter.CocktailListHolder>() {
    private lateinit var context: Context

    class CocktailListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageCockTail: AppCompatImageView = itemView.findViewById(R.id.imageCocktail)
        val textCocktailTitle: TextView = itemView.findViewById(R.id.textCocktailTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailListHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cocktail, parent, false)
        return CocktailListHolder(view)
    }

    override fun onBindViewHolder(holder: CocktailListHolder, position: Int) {
        holder.textCocktailTitle.text = list[position].strCategory

        Picasso.get().load(list[position].strDrinkThumb)
            .resize(width() / 2 - 60, width() / 2 - 60)
            .into(holder.imageCockTail)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CocktailDetailActivity::class.java)
            intent.putExtra("cocktail", list[position])
            context.startActivity(intent)
        }
    }

    fun filter(filterList: List<CocktailList>) {
        list = filterList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun width(): Int {
        return context.resources.displayMetrics.widthPixels
    }
}