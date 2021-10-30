package com.ramnarayan.mvvm.ui.cocktail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ramnarayan.commons.Data
import com.ramnarayan.commons.Status
import com.ramnarayan.commons.UIConstants
import com.ramnarayan.domain.entities.CocktailList
import com.ramnarayan.domain.entities.CocktailListData
import com.ramnarayan.mvvm.R
import org.koin.android.viewmodel.ext.android.viewModel

class CocktailListActivity : AppCompatActivity() {
    private val viewModel by viewModel<CocktailListViewModel>()
    private lateinit var cocktailListAdapter: CocktailListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var editFilter: EditText


    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        recyclerView = findViewById(R.id.rvCocktailList)
        editFilter = findViewById(R.id.editFilter)

        editFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filter(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                // TODO("Not yet implemented")
            }

        })

        viewModel.cocktailList(CocktailListData(""))
        viewModel.cocktailListResponse.observe(this, ::cocktailListResponse)
    }

    private fun cocktailListResponse(data: Data<List<CocktailList>>) {
        when (data.responseType) {
            Status.LOADING -> {

            }

            Status.SUCCESSFUL -> {
                data.data?.let {
                    loadAdapter(data.data!!)
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

    private fun loadAdapter(list: List<CocktailList>) {
        viewModel.cocktailList = list
        cocktailListAdapter = CocktailListAdapter(list)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@CocktailListActivity, 2)
            adapter = cocktailListAdapter
        }

        recyclerView.setHasFixedSize(true)
        filter(editFilter.text.toString())
    }

    private fun filter(filter: String) {
        if (this::cocktailListAdapter.isInitialized)
            cocktailListAdapter.filter(viewModel.filter(filter))
    }
}