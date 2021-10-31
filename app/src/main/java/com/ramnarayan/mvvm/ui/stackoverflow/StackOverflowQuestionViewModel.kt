package com.ramnarayan.mvvm.ui.stackoverflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ramnarayan.commons.Data
import com.ramnarayan.commons.Result
import com.ramnarayan.commons.Status
import com.ramnarayan.domain.entities.Item
import com.ramnarayan.domain.entities.StackOverflowQuestionListData
import com.ramnarayan.domain.usecase.GetStackOverflowDataFromDbUseCase
import com.ramnarayan.domain.usecase.StackOverflowQuestionUseCase
import com.ramnarayan.mvvm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StackOverflowQuestionViewModel(
    private val stackOverflowQuestionUseCase: StackOverflowQuestionUseCase,
    private var stackOverflowDataFromDbUseCase: GetStackOverflowDataFromDbUseCase
) :
    BaseViewModel() {
    private var mutableStackOverflowQuestionList: MutableLiveData<Data<StackOverflowQuestionListData>> =
        MutableLiveData()

    private var mutableDataFromDb: MutableLiveData<Data<StackOverflowQuestionListData>> =
        MutableLiveData()


    private var mutableAverageViewCount: MutableLiveData<Double> = MutableLiveData()
    private var mutableAverageAnswerCount: MutableLiveData<Double> = MutableLiveData()

    val dataFromDb: LiveData<Data<StackOverflowQuestionListData>>
        get() {
            return mutableDataFromDb
        }


    val averageViewCount: LiveData<Double>
        get() {
            return mutableAverageViewCount
        }

    val averageAnswerCount: LiveData<Double>
        get() {
            return mutableAverageAnswerCount
        }


    val stackOverflowQuestionListResponse: LiveData<Data<StackOverflowQuestionListData>>
        get() {
            return mutableStackOverflowQuestionList
        }


    /* get question list from stack overflow */
    @ExperimentalStdlibApi
    fun stackOverflowQuestionList(input: String) = launch {
        try {
            val stackOverflowQuestionListResponse = withContext(Dispatchers.IO) {
                stackOverflowQuestionUseCase.execute(input)
            }

            when (stackOverflowQuestionListResponse) {
                is Result.Success -> {
                    mutableStackOverflowQuestionList.value =
                        Data(
                            responseType = Status.SUCCESSFUL,
                            data = stackOverflowQuestionListResponse.data
                        )
                }

                is Result.Failure -> {
                    mutableStackOverflowQuestionList.value =
                        Data(
                            responseType = Status.ERROR,
                            error = stackOverflowQuestionListResponse.exception
                        )
                }
            }
        } catch (exception: Exception) {
            mutableStackOverflowQuestionList.value =
                Data(responseType = Status.ERROR, error = exception)

        }
    }


    /* get data from local database if app is offline */
    @ExperimentalStdlibApi
    fun getDataFromDB(input: String) = launch {
        try {
            val dataFromDb = withContext(Dispatchers.IO) {
                stackOverflowDataFromDbUseCase.execute(input)
            }

            when (dataFromDb) {
                is Result.Success -> {
                    mutableDataFromDb.value =
                        Data(
                            responseType = Status.SUCCESSFUL,
                            data = dataFromDb.data
                        )
                }

                is Result.Failure -> {
                    mutableDataFromDb.value =
                        Data(
                            responseType = Status.ERROR,
                            error = dataFromDb.exception
                        )
                }
            }
        } catch (exception: Exception) {
            mutableDataFromDb.value =
                Data(responseType = Status.ERROR, error = exception)

        }
    }


    /* average view count based on background thread */
    fun averageViewCount(stackOverflowQuestionListData: StackOverflowQuestionListData) {
        launch {
            withContext(Dispatchers.IO) {
                var count = 0

                for (i in stackOverflowQuestionListData.items.indices) {
                    count += stackOverflowQuestionListData.items[i].view_count
                }

                mutableAverageViewCount.postValue(
                    String.format(
                        "%.2f",
                        (count.toDouble() / stackOverflowQuestionListData.items.size)
                    ).toDouble()
                )
            }
        }
    }

    /* average answer count on background thread */
    fun averageAnswerCount(stackOverflowQuestionListData: StackOverflowQuestionListData) {
        launch {
            withContext(Dispatchers.IO) {
                var count = 0

                for (i in stackOverflowQuestionListData.items.indices) {
                    count += stackOverflowQuestionListData.items[i].answer_count
                }

                mutableAverageAnswerCount.postValue(
                    String.format(
                        "%.2f",
                        (count.toDouble() / stackOverflowQuestionListData.items.size)
                    ).toDouble()
                )
            }
        }
    }

    /* filter question list based on search => name, question title */
    fun filter(
        search: String,
        stackOverflowQuestionListData: StackOverflowQuestionListData
    ): StackOverflowQuestionListData {

        val list: ArrayList<Item> = ArrayList()
        for (i in stackOverflowQuestionListData.items.indices) {
            if (stackOverflowQuestionListData.items[i].owner.display_name.uppercase()
                    .contains(search.uppercase())
                || stackOverflowQuestionListData.items[i].title.uppercase()
                    .contains(search.uppercase())
            ) {
                list.add(stackOverflowQuestionListData.items[i])
            }
        }

        return StackOverflowQuestionListData(
            stackOverflowQuestionListData.has_more,
            list,
            stackOverflowQuestionListData.quota_max,
            stackOverflowQuestionListData.quota_remaining
        )
    }

    /* filter question list based on tags*/
    fun filterBasedOnTag(
        tag: String,
        stackOverflowQuestionListData: StackOverflowQuestionListData
    ): StackOverflowQuestionListData {
        val list: ArrayList<Item> = ArrayList()
        for (i in stackOverflowQuestionListData.items.indices) {
            if (stackOverflowQuestionListData.items[i].tags.contains(tag)) {
                list.add(stackOverflowQuestionListData.items[i])

            }
        }

        return StackOverflowQuestionListData(
            stackOverflowQuestionListData.has_more,
            list,
            stackOverflowQuestionListData.quota_max,
            stackOverflowQuestionListData.quota_remaining
        )
    }
}