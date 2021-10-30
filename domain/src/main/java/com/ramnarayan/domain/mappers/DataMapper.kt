package com.ramnarayan.domain.mappers

import com.ramnarayan.domain.entities.CocktailListData
import org.json.JSONObject

class DataMapper {
    companion object {
        fun cocktailRequest(cocktailListData: CocktailListData): JSONObject {
            val request = JSONObject()
            request.put("cockTailName", cocktailListData.cockTailName)
            return request
        }
    }
}