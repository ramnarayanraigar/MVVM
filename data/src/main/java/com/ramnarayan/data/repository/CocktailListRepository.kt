package com.ramnarayan.data.repository

import com.ramnarayan.data.service.CocktailListService
import org.json.JSONObject
import com.ramnarayan.commons.Result
import com.ramnarayan.data.api.API

class CocktailListRepository(private val cocktailListService: CocktailListService) {

    suspend fun cocktailList(request: JSONObject) : Result<JSONObject> {
        val url: String = API.cocktailUrl + request["cockTailName"] as String
        return cocktailListService.cocktailList(request, url)
    }
}