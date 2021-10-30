package com.ramnarayan.mvvm.ui.cocktail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.ramnarayan.domain.entities.CocktailList
import com.ramnarayan.mvvm.R
import com.squareup.picasso.Picasso

class CocktailDetailActivity : AppCompatActivity() {
    private lateinit var imageThumbNail: AppCompatImageView
    private lateinit var textCocktailTitle: TextView
    private lateinit var textCocktailInstruction: TextView
    private lateinit var textCocktailIngredient: TextView
    private lateinit var textCocktailMeasure: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_detail)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        initViews()

        val cocktail = intent.getSerializableExtra("cocktail") as CocktailList
        setValue(cocktail)
    }

    private fun initViews() {
        imageThumbNail = findViewById(R.id.imageCocktail)
        textCocktailTitle = findViewById(R.id.textCocktailTitle)
        textCocktailInstruction = findViewById(R.id.textCocktailInstruction)
        textCocktailIngredient = findViewById(R.id.textCocktailIngredient)
        textCocktailMeasure = findViewById(R.id.textCocktailMeasure)
    }

    private fun setValue(cocktailList: CocktailList) {
        Picasso.get().load(cocktailList.strDrinkThumb)
            .resize(width(), height() / 3)
            .into(imageThumbNail)

        textCocktailTitle.text = cocktailList.strCategory
        textCocktailInstruction.text = cocktailList.strInstructions
        textCocktailIngredient.text = ingredient(cocktailList)
        textCocktailMeasure.text = measure(cocktailList)
    }

    private fun ingredient(cocktailList: CocktailList): String {
        var ingredient = ""

        if (cocktailList.strIngredient1 != null) {
            ingredient = ingredient.plus(cocktailList.strIngredient1)
        }

        if (cocktailList.strIngredient2 != null) {
            ingredient = ingredient.plus(", ").plus(cocktailList.strIngredient2)
        }

        if (cocktailList.strIngredient3 != null) {
            ingredient = ingredient.plus(", ").plus(cocktailList.strIngredient3)
        }

        if (cocktailList.strIngredient4 != null) {
            ingredient = ingredient.plus(", ").plus(cocktailList.strIngredient4)
        }

        if (cocktailList.strIngredient5 != null) {
            ingredient = ingredient.plus(", ").plus(cocktailList.strIngredient5)
        }

        return ingredient
    }

    private fun measure(cocktailList: CocktailList): String {
        var measure = ""

        if (cocktailList.strMeasure1 != null) {
            measure = measure.plus(cocktailList.strMeasure1)
        }

        if (cocktailList.strMeasure2 != null) {
            measure = measure.plus(", ").plus(cocktailList.strMeasure2)
        }

        if (cocktailList.strMeasure3 != null) {
            measure = measure.plus(", ").plus(cocktailList.strMeasure3)
        }

        if (cocktailList.strMeasure4 != null) {
            measure = measure.plus(", ").plus(cocktailList.strMeasure4)
        }

        return measure
    }

    private fun width(): Int {
        return resources.displayMetrics.widthPixels
    }

    private fun height(): Int {
        return resources.displayMetrics.heightPixels
    }

}