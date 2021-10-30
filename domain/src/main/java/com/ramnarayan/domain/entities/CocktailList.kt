package com.ramnarayan.domain.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CocktailList(
    @SerializedName("idDrink")
    val idDrink: String?,
    @SerializedName("strDrink")
    val strDrink: String?,
    @SerializedName("strDrinkAlternate")
    val strDrinkAlternate: String?,
    @SerializedName("strCategory")
    val strCategory: String?,
    @SerializedName("strIBA")
    val strIBA: String?,
    @SerializedName("strAlcoholic")
    val strAlcoholic: String?,
    @SerializedName("strGlass")
    val strGlass: String?,
    @SerializedName("strInstructions")
    val strInstructions: String?,
    @SerializedName("strDrinkThumb")
    val strDrinkThumb: String?,
    @SerializedName("strIngredient1")
    val strIngredient1: String?,
    @SerializedName("strIngredient2")
    val strIngredient2: String?,
    @SerializedName("strIngredient3")
    val strIngredient3: String?,
    @SerializedName("strIngredient4")
    val strIngredient4: String?,
    @SerializedName("strIngredient5")
    val strIngredient5: String?,
    @SerializedName("strMeasure1")
    val strMeasure1: String?,
    @SerializedName("strMeasure2")
    val strMeasure2: String?,
    @SerializedName("strMeasure3")
    val strMeasure3: String?,
    @SerializedName("strMeasure4")
    val strMeasure4: String?
) : Serializable
