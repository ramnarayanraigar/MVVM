package com.ramnarayan.mvvm.ui.stackoverflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ramnarayan.commons.Data
import com.ramnarayan.commons.Status
import com.ramnarayan.commons.UIConstants
import com.ramnarayan.domain.entities.StackOverflowQuestionListData

import org.koin.android.viewmodel.ext.android.viewModel

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ramnarayan.mvvm.R
import com.ramnarayan.mvvm.utils.Util


class StackOverflowQuestionListActivity : AppCompatActivity(),
    StackOverflowLanguageAdapter.TagListener {
    private var count = 0
    private val viewModel by viewModel<StackOverflowQuestionViewModel>()
    private lateinit var stackOverflowQuestionListAdapter: StackOverflowQuestionListAdapter
    private lateinit var rvQuestionList: RecyclerView
    private lateinit var textAverageViewCount: TextView
    private lateinit var textAverageAnswerCount: TextView
    private lateinit var textNoResultFound: TextView
    private lateinit var linearCount: LinearLayout
    private lateinit var editSearch: EditText
    private lateinit var imageFilter: AppCompatImageView
    private lateinit var listLanguage: ArrayList<String>

    private lateinit var stackOverflowQuestionListData: StackOverflowQuestionListData

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack_overflow_question_list)

        initView()

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        if (Util.isOnline(this)) {
            viewModel.stackOverflowQuestionList("")
        } else {
            viewModel.getDataFromDB("")
        }

        initObserver()


        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (count != 0) {
                    val data = viewModel.filter(char.toString(), stackOverflowQuestionListData)

                    if (data.items.isEmpty()) {
                        textNoResultFound.visibility = View.VISIBLE
                        linearCount.visibility = View.GONE
                    } else {
                        textNoResultFound.visibility = View.GONE
                        linearCount.visibility = View.VISIBLE
                    }

                    viewModel.averageViewCount(data)
                    viewModel.averageAnswerCount(data)
                    stackOverflowQuestionListAdapter.filter(data)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        imageFilter.setOnClickListener {
            if (this::listLanguage.isInitialized)
                loadBottomSheet()
        }
    }

    private fun initView() {
        rvQuestionList = findViewById(R.id.rvQuestionList)
        textAverageViewCount = findViewById(R.id.textAverageViewCount)
        textAverageAnswerCount = findViewById(R.id.textAverageAnswerCount)
        editSearch = findViewById(R.id.editSearch)
        textNoResultFound = findViewById(R.id.textNoResultFound)
        linearCount = findViewById(R.id.linearCount)
        imageFilter = findViewById(R.id.imageFilter)
    }

    private fun initObserver() {
        viewModel.stackOverflowQuestionListResponse.observe(this, ::stackOverflowQuestionList)
        viewModel.averageViewCount.observe(this, ::averageViewCount)
        viewModel.averageAnswerCount.observe(this, ::averageAnswerCount)
        viewModel.dataFromDb.observe(this, ::dataFromDb)
    }

    private fun stackOverflowQuestionList(data: Data<StackOverflowQuestionListData>) {
        when (data.responseType) {
            Status.LOADING -> {

            }

            Status.SUCCESSFUL -> {
                data.data?.let {
                    count++
                    viewModel.averageViewCount(data.data!!)
                    viewModel.averageAnswerCount(data.data!!)
                    stackOverflowQuestionListData = data.data!!
                    loadAdapter(data.data!!)
                    languageList()
                }
            }

            Status.HTTP_UNAVAILABLE -> {
                Toast.makeText(this, UIConstants.serverUnderMaintenance, Toast.LENGTH_LONG).show()
            }

            Status.ERROR -> {
                Toast.makeText(this, UIConstants.somethingWentWrong, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun averageAnswerCount(viewCount: Double) {
        textAverageAnswerCount.text = viewCount.toString()
    }

    private fun averageViewCount(viewCount: Double) {
        textAverageViewCount.text = viewCount.toString()
    }

    /* get data from database when app is offline */
    private fun dataFromDb(data: Data<StackOverflowQuestionListData>) {
        when (data.responseType) {
            Status.LOADING -> {

            }

            Status.SUCCESSFUL -> {
                data.data?.let {
                    data.data?.let {
                        count++
                        viewModel.averageViewCount(data.data!!)
                        viewModel.averageAnswerCount(data.data!!)
                        stackOverflowQuestionListData = data.data!!
                        loadAdapter(data.data!!)
                        languageList()
                    }
                }
            }

            Status.HTTP_UNAVAILABLE -> {
                Toast.makeText(this, UIConstants.serverUnderMaintenance, Toast.LENGTH_LONG).show()
            }

            Status.ERROR -> {
                Toast.makeText(this, UIConstants.somethingWentWrong, Toast.LENGTH_LONG).show()
            }
        }
    }

    /* load question list recycler view */
    private fun loadAdapter(stackOverflowQuestionListData: StackOverflowQuestionListData) {
        stackOverflowQuestionListAdapter =
            StackOverflowQuestionListAdapter(stackOverflowQuestionListData)
        rvQuestionList.apply {
            layoutManager = LinearLayoutManager(this@StackOverflowQuestionListActivity)
            adapter = stackOverflowQuestionListAdapter
        }

        rvQuestionList.setHasFixedSize(true)
    }

    /* open bottom sheet dialog when filter icon clicked */
    private fun loadBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_clear)

        val rvCountry = bottomSheetDialog.findViewById<RecyclerView>(R.id.rvLanguage)
        val textClear = bottomSheetDialog.findViewById<TextView>(R.id.textClear)

        loadLanguageRecyclerView(rvCountry, listLanguage, bottomSheetDialog)
        bottomSheetDialog.show()

        textClear?.setOnClickListener {
            if (editSearch.text.isEmpty()) {
                editSearch.setText(" ")
            }
            editSearch.text.clear()
            bottomSheetDialog.dismiss()
        }
    }

    /* load language recycler view */
    private fun loadLanguageRecyclerView(
        rcView: RecyclerView?,
        list: ArrayList<String>,
        dialog: BottomSheetDialog
    ) {
        rcView?.apply {
            layoutManager = LinearLayoutManager(this@StackOverflowQuestionListActivity)
            adapter = StackOverflowLanguageAdapter(
                list,
                dialog,
                editSearch,
                this@StackOverflowQuestionListActivity
            )
        }

        rcView?.isNestedScrollingEnabled = true
    }

    /* get all language list from question list */
    private fun languageList() {
        listLanguage = ArrayList()
        val hs = HashSet<String>()
        if (this::stackOverflowQuestionListData.isInitialized) {
            for (i in stackOverflowQuestionListData.items.indices) {
                hs.addAll(stackOverflowQuestionListData.items[i].tags)
            }
        }

        listLanguage.addAll(hs)
    }

    override fun tagName(name: String) {
        stackOverflowQuestionListAdapter.filter(
            viewModel.filterBasedOnTag(
                name,
                stackOverflowQuestionListData
            )
        )
        editSearch.text.clear()
    }
}