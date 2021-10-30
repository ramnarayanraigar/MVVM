package com.ramnarayan.mvvm.ui.cocktail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ramnarayan.commons.Data
import com.ramnarayan.commons.Result
import com.ramnarayan.commons.Status
import com.ramnarayan.domain.entities.CocktailList
import com.ramnarayan.domain.entities.CocktailListData
import com.ramnarayan.domain.usecase.CocktailListUseCase
import com.ramnarayan.mvvm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CocktailListViewModel(private val cocktailListUseCase: CocktailListUseCase) :
    BaseViewModel() {
    private var mutableCocktailList: MutableLiveData<Data<List<CocktailList>>> = MutableLiveData()
    lateinit var cocktailList: List<CocktailList>


    val cocktailListResponse: LiveData<Data<List<CocktailList>>>
        get() {
            return mutableCocktailList
        }


    @ExperimentalStdlibApi
    fun cocktailList(cocktailListData: CocktailListData) = launch {
        try {
            val cocktailListResponse = withContext(Dispatchers.IO) {
                cocktailListUseCase.execute(cocktailListData)
            }

            when (cocktailListResponse) {
                is Result.Success -> {
                    mutableCocktailList.value =
                        Data(responseType = Status.SUCCESSFUL, data = cocktailListResponse.data)
                }

                is Result.Failure -> {
                    mutableCocktailList.value =
                        Data(responseType = Status.ERROR, error = cocktailListResponse.exception)
                }
            }
        } catch (exception: Exception) {
            mutableCocktailList.value = Data(responseType = Status.ERROR, error = exception)

        }
    }

    fun filter(filter: String): List<CocktailList> {
        val list: ArrayList<CocktailList> = ArrayList()
        for (i in cocktailList.indices) {
            if (cocktailList[i].strCategory.toString().uppercase()
                    .contains(filter.uppercase())
            ) {
                list.add(cocktailList[i])
            }
        }

        return list
    }
}