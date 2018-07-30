package com.valery.todo.ui.screens.todos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.valery.todo.R
import com.valery.todo.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_todos.*

class TodosFragment : BaseFragment<TodosViewModel>(TodosViewModel::class.java) {

    private var adapter: TodosAdapter? = null

    override val layoutId: Int = R.layout.fragment_todos

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareAdapter()
        viewModel?.itemsLiveData?.observe(this, Observer { items ->
            items?.let {
                adapter?.updateListItems(it, TodoDifUtillCallback() )
            }
        })
        btnAddTodo.setOnClickListener {
            viewModel?.addValue()
        }
    }

    private fun prepareAdapter () {
        adapter = TodosAdapter()
        rvTodos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvTodos.adapter = adapter

    }

    override fun liveData(): List<MutableLiveData<*>> {
        return listOfNotNull(viewModel?.itemsLiveData)
    }
}