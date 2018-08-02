package com.valery.todo.ui.screens.todos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.valery.todo.R
import com.valery.todo.ui.base.BaseFragment
import com.valery.todo.ui.screens.main.MainRouter
import com.valery.todo.ui.screens.todos.item.SectionTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.TodoItemViewModel
import com.valery.todo.utils.extensions.hide
import com.valery.todo.utils.extensions.show
import kotlinx.android.synthetic.main.fragment_todos.*

class TodosFragment : BaseFragment<TodosViewModel>(TodosViewModel::class.java), TodoCallback {

    companion object {
        fun newInstance () = TodosFragment ()
    }

    private var adapter: TodosAdapter? = null

    private var router: MainRouter? = null

    override val layoutId: Int = R.layout.fragment_todos

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        router = context as MainRouter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareAdapter()
        prepareSubscriptions()
        btnAddSection.setOnClickListener { v ->
            viewModel?.addSection()
        }
        viewModel?.load()
    }

    override fun setDone(todoItemViewModel: TodoItemViewModel) {
        viewModel?.changeStatus(todoItemViewModel)
    }

    override fun liveData(): List<MutableLiveData<*>> {
        return listOfNotNull(viewModel?.itemsLiveData)
    }

    override fun switchExpandState(section: SectionTodoItemViewModel) {
        viewModel?.expandSection(section)
    }

    override fun addTodo(section: SectionTodoItemViewModel) {
        
    }

    private fun prepareSubscriptions() {
        viewModel?.itemsLiveData?.observe(this, Observer { items ->
            items?.let {
                adapter?.updateListItems(it, TodoDifUtillCallback())
                if (items.size == 0 ) {
                    tvSectionsNotFound.show()
                } else {
                    tvSectionsNotFound.hide(true)
                }
            }
        })
    }

    override fun onShowProgress(data: Any?) {
        pbLoading.show()
        rvTodos.hide(true)
    }

    override fun onShowSuccess(data: Any?) {
        pbLoading.hide(true)
        rvTodos.show()
    }

    private fun prepareAdapter() {
        adapter = TodosAdapter(this)
        rvTodos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvTodos.adapter = adapter
    }
}