package com.ramnarayan.domain.usecase

import com.google.gson.Gson
import com.ramnarayan.domain.UseCase
import com.ramnarayan.data.repository.CocktailListRepository
import org.json.JSONObject
import com.ramnarayan.commons.Result
import com.ramnarayan.domain.entities.CocktailList
import com.ramnarayan.domain.entities.CocktailListData
import com.ramnarayan.domain.mappers.DataMapper
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class CocktailListUseCase(private val cocktailListRepository: CocktailListRepository) :
    UseCase<CocktailListData, List<CocktailList>> {
    @ExperimentalStdlibApi
    override suspend fun execute(input: CocktailListData): Result<List<CocktailList>> {
        return when (val result =
            cocktailListRepository.cocktailList(DataMapper.cocktailRequest(input))) {
            is Result.Success -> {
                val chapterListType = typeOf<List<CocktailList>>().javaType
                val jsonObj = JSONObject(result.data.toString())
                val arrChapters = jsonObj.getJSONArray("drinks")
                val chapterList =
                    Gson().fromJson(arrChapters.toString(), chapterListType) as List<CocktailList>

                Result.Success(chapterList)


            }

            is Result.Failure -> {
                Result.Failure(result.exception)
            }
        }
    }
}